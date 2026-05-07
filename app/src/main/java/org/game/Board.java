package org.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.game.Item.Apple;
import org.game.Item.Hammer;
import org.game.Item.Item;
import org.game.Item.Pineapple;
import org.game.Item.Star;
import org.game.Item.Wall;
import org.game.Item.WallRemover;
import org.game.ItemFactory.AppleFactory;
import org.game.ItemFactory.HammerFactory;
import org.game.ItemFactory.ItemFactory;
import org.game.ItemFactory.PineappleFactory;
import org.game.ItemFactory.StarFactory;
import org.game.ItemFactory.WallRemoverFactory;
import org.game.StatsBoard.StatsBoard;

public class Board extends JPanel implements ActionListener {

    private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = 900;
    private final int DELAY = 140;
    private final int BORDER_THICKNESS = 3;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private final int introduceStar = 5;
    private final int introducePineapple = 25;
    private boolean starIntroduced = false;
    private boolean pineappleIntroduced = false;
    private boolean hammerIntroduced = false;
    private boolean wallRemoverIntroduced = false;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private Direction currentDirection = Direction.RIGHT;
    private final Deque<Direction> directionQueue = new ArrayDeque<>();
    private boolean inGame = true;
    private boolean gameWon = false;

    private Timer timer;
    private Image dot;
    private Image head;

    private boolean paused = false;

    private int newAppleIntroduced = 0;

    private Snake snake;
    private StatsBoard statsBoard;

    private ArrayList<Item> items = new ArrayList<Item>();

    public Board(Snake snake, StatsBoard statsBoard) {
        this.snake = snake;
        this.statsBoard = statsBoard;
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void notifyStatsListener() {
        if (statsBoard == null) return;
        statsBoard.updateStats();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon(getClass().getResource("/dot.png"));
        dot = iid.getImage();

        ImageIcon iih = new ImageIcon(getClass().getResource("/head.png"));
        head = iih.getImage();
    }

    private void initGame() {

        for (int z = 0; z < statsBoard.getSnakeLength(); z++) {
            x[z] = 4 * DOT_SIZE - z * DOT_SIZE;
            y[z] = 2 * DOT_SIZE;
        }
        
        locateItem(new AppleFactory());

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        for (Item item : items) {
            item.draw(g, this);
        }

        for (int z = 0; z < statsBoard.getSnakeLength(); z++) {
            if (z == 0) {
                g.drawImage(head, x[z], y[z], DOT_SIZE, DOT_SIZE, this);
            } else {
                g.drawImage(dot, x[z], y[z], DOT_SIZE, DOT_SIZE, this);
            }
        }

        if (paused && inGame) {
            String msg = "PAUSED";
            Font font = new Font("Helvetica", Font.BOLD, 50);
            FontMetrics metr = getFontMetrics(font);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        }

        if (!inGame) {
            drawEndStatus(g);
        }

        g.setColor(Color.GREEN);
        for (int i = 0; i < BORDER_THICKNESS; i++) {
            g.drawRect(i, i, B_WIDTH - 1 - (2 * i), B_HEIGHT - 1 - (2 * i));
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawEndStatus(Graphics g) {
        String message = gameWon ? "You Win" : "Game Over";
        String hint = "Click shift to restart or esc to exit";

        g.setColor(new Color(0, 0, 0, 140));
        g.fillRect(0, 0, B_WIDTH, B_HEIGHT);

        Font titleFont = new Font("Helvetica", Font.BOLD, 48);
        Font hintFont = new Font("Helvetica", Font.BOLD, 22);
        FontMetrics titleMetrics = getFontMetrics(titleFont);
        FontMetrics hintMetrics = getFontMetrics(hintFont);

        g.setColor(Color.WHITE);
        g.setFont(titleFont);
        g.drawString(message, (B_WIDTH - titleMetrics.stringWidth(message)) / 2, B_HEIGHT / 2 - 20);

        g.setFont(hintFont);
        g.drawString(hint, (B_WIDTH - hintMetrics.stringWidth(hint)) / 2, B_HEIGHT / 2 + 24);
    }

    private void finishGame(boolean won) {
        if (!won) {
            statsBoard.setLives(0);
        }
        gameWon = won;
        inGame = false;
        timer.stop();
        if (statsBoard != null) {
            statsBoard.getGameStats().gameEnded();
            statsBoard.checkHighScore();
        }
    }

    private boolean isWinningState() {
        return statsBoard.getSnakeLength() >= ALL_DOTS;
    }

    private boolean isOccupiedBySnake(int itemX, int itemY) {
        for (int z = 0; z < statsBoard.getSnakeLength(); z++) {
            if (x[z] == itemX && y[z] == itemY) {
                return true;
            }
        }
        return false;
    }

    private boolean isOccupiedByItem(int itemX, int itemY) {
        for (Item item : items) {
            if (item.getX() == itemX && item.getY() == itemY) {
                return true;
            }
        }
        return false;
    }

    private List<int[]> getFreeCells() {
        List<int[]> freeCells = new ArrayList<>();
        int columns = B_WIDTH / DOT_SIZE;
        int rows = B_HEIGHT / DOT_SIZE;

        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                int itemX = column * DOT_SIZE;
                int itemY = row * DOT_SIZE;
                if (!isOccupiedBySnake(itemX, itemY) && !isOccupiedByItem(itemX, itemY)) {
                    freeCells.add(new int[] { itemX, itemY });
                }
            }
        }

        return freeCells;
    }

    private boolean hasItemOfType(Class<? extends Item> itemType) {
        for (Item item : items) {
            if (itemType.isInstance(item)) {
                return true;
            }
        }
        return false;
    }

    private boolean removeFirstItemOfType(Class<? extends Item> itemType) {
        for (int index = 0; index < items.size(); index++) {
            if (itemType.isInstance(items.get(index))) {
                items.remove(index);
                return true;
            }
        }
        return false;
    }

    private void ensureAppleAvailability() {
        if (isWinningState() || hasItemOfType(Apple.class)) {
            return;
        }

        while (hasItemOfType(Star.class)) {
            removeFirstItemOfType(Star.class);
            if (locateItem(new AppleFactory())) {
                return;
            }
        }

        while (hasItemOfType(Pineapple.class)) {
            removeFirstItemOfType(Pineapple.class);
            if (locateItem(new AppleFactory())) {
                return;
            }
        }

        locateItem(new AppleFactory());
    }

    private void respawnItemsByPriority(List<Item> removedItems) {
        removedItems.stream()
                .sorted((a, b) -> Integer.compare(a.getSpawnPriority(), b.getSpawnPriority()))
                .forEach(item -> item.locateItem(this));
    }

    private void refreshItemsAfterBoardChange(List<Item> removedItems) {
        items.removeAll(removedItems);

        if (isWinningState()) {
            statsBoard.gameWon();
            finishGame(true);
            return;
        }

        respawnItemsByPriority(removedItems);

        if (statsBoard.getStarCollected() % 5 == 0 && statsBoard.getStarCollected() > newAppleIntroduced) {
            if (locateItem(new AppleFactory())) {
                newAppleIntroduced = statsBoard.getStarCollected();
                if (newAppleIntroduced == 30) {
                    newAppleIntroduced = Integer.MAX_VALUE;
                }
            }
        }

        if (statsBoard.getAppleCollected() == introduceStar && !starIntroduced) {
            if (locateItem(new StarFactory())) {
                starIntroduced = true;
            }
        }

        if (statsBoard.getStarCollected() == introducePineapple && !pineappleIntroduced) {
            if (locateItem(new PineappleFactory())) {
                pineappleIntroduced = true;
            }
        }

        if (statsBoard.getHammerCollected() == 10 && !wallRemoverIntroduced && hammerIntroduced) {
            if (locateItem(new WallRemoverFactory())) {
                wallRemoverIntroduced = true;
            }
        }

        ensureAppleAvailability();
    }

    private void checkItem() {
        ArrayList<Item> removeItems = new ArrayList<>();
        ArrayList<Item> noRefresh = new ArrayList<>();
        for (Item item : items) {
            if ((x[0] == item.getX()) && (y[0] == item.getY())) {
                if (item instanceof WallRemover) {
                    wallRemoverIntroduced = false;
                    hammerIntroduced = false;
                    for (Item wallItem : items) {
                        if (wallItem instanceof Wall || wallItem instanceof Hammer) {
                            noRefresh.add(wallItem);
                            statsBoard.increaseWallsDestroyed();
                        }
                    }
                    noRefresh.add(item);
                } else {
                    if (item instanceof Pineapple && statsBoard.getSnakeLength() == 1) {
                        finishGame(false);
                        removeItems.add(item);
                        continue;
                    }
                    removeItems.add(item);
                }
                item.itemEffect(statsBoard.getGameStats());
                if (statsBoard.getLives() <= 0) {
                    finishGame(false);
                    break;
                }
            }
        }
        items.removeAll(noRefresh);
        if (!inGame) {
            return;
        }
        if (!removeItems.isEmpty()) {
            refreshItemsAfterBoardChange(removeItems);
        }
    }

    private void move() {
        for (int z = statsBoard.getSnakeLength(); z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private boolean isOpposite(Direction first, Direction second) {
        return (first == Direction.LEFT && second == Direction.RIGHT)
                || (first == Direction.RIGHT && second == Direction.LEFT)
                || (first == Direction.UP && second == Direction.DOWN)
                || (first == Direction.DOWN && second == Direction.UP);
    }

    private void applyDirection(Direction direction) {
        leftDirection = direction == Direction.LEFT;
        rightDirection = direction == Direction.RIGHT;
        upDirection = direction == Direction.UP;
        downDirection = direction == Direction.DOWN;
        currentDirection = direction;
    }

    private boolean enqueueDirection(Direction direction) {
        if (directionQueue.size() >= 3) {
            return false;
        }
        Direction referenceDirection = directionQueue.isEmpty() ? currentDirection : directionQueue.peekLast();
        if (direction == referenceDirection || isOpposite(referenceDirection, direction)) {
            return false;
        }
        directionQueue.offerLast(direction);
        return true;
    }

    private boolean enqueueDirectionFromKey(int key) {
        if (key == KeyEvent.VK_LEFT) {
            return enqueueDirection(Direction.LEFT);
        }
        if (key == KeyEvent.VK_RIGHT) {
            return enqueueDirection(Direction.RIGHT);
        }
        if (key == KeyEvent.VK_UP) {
            return enqueueDirection(Direction.UP);
        }
        if (key == KeyEvent.VK_DOWN) {
            return enqueueDirection(Direction.DOWN);
        }
        return false;
    }

    private void applyNextQueuedDirection() {
        if (!directionQueue.isEmpty()) {
            applyDirection(directionQueue.pollFirst());
        }
    }

    private void checkCollision() {
        for (int z = statsBoard.getSnakeLength(); z > 0; z--) {

            if ((x[0] == x[z]) && (y[0] == y[z])) {
                if (statsBoard.getLives() >= 1) {
                    statsBoard.decreaseLives();
                    if (statsBoard.getLives() == 0) {
                        finishGame(false);
                    }
                }
            }
        }

        if (y[0] >= B_HEIGHT) {
            finishGame(false);
        }

        if (y[0] < 0) {
            finishGame(false);
        }

        if (x[0] >= B_WIDTH) {
            finishGame(false);
        }

        if (x[0] < 0) {
            finishGame(false);
        }
    }

    public boolean locateItem(ItemFactory factory) {
        List<int[]> freeCells = getFreeCells();
        if (freeCells.isEmpty()) {
            return false;
        }

        int randomIndex = (int) (Math.random() * freeCells.size());
        int[] position = freeCells.get(randomIndex);
        items.add(factory.create(position[0], position[1], Instant.now()));
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Item> removeItems = new ArrayList<Item>();
        boolean addHammer = false;

        if (inGame && !paused) {
            ArrayList<Item> wallsToAdd = new ArrayList<>();
            for (Item item : items) {
                if (item.existDuration().getSeconds() >= item.getExpireDuration().toSeconds()) {
                    removeItems.add(item);
                    item.itemMissed(statsBoard.getGameStats());
                    if (item instanceof Star && statsBoard.getStarsMissed() % 5 == 0) {
                        wallsToAdd.add(new Wall(item.getX(), item.getY(), Instant.now()));
                        if (!hammerIntroduced) {
                            hammerIntroduced = true;
                            addHammer = true;
                        }
                    }
                }
            }
            if (addHammer) {
                locateItem(new HammerFactory()); // first hammer
                statsBoard.unlockHammer();
            }
            items.addAll(wallsToAdd);
            if (!removeItems.isEmpty()) {
                refreshItemsAfterBoardChange(removeItems);
            }
            checkItem();
            if (inGame) {
                checkCollision();
            }
            if (inGame) {
                applyNextQueuedDirection();
                move();
                statsBoard.checkStats();
                notifyStatsListener();
            }
        }

        repaint();
    }

    private void resume() {
        paused = false;
        Duration pauseDuration = statsBoard.resume();
        for (Item item : items) {
            item.setPauseDuration(pauseDuration);
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SHIFT) {
                snake.restart();
                return;
            }

            if (key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
                return;
            }


            if (key == KeyEvent.VK_SPACE) {
                paused = !paused;
                if (paused) {
                    statsBoard.pause();
                    for (Item item : items) {
                        item.startPause();
                    }
                } else {
                    resume();
                }
                return;
            }

            boolean queuedDirection = enqueueDirectionFromKey(key);
            if (queuedDirection && paused) {
                    resume();
            }
        }
    }
}
