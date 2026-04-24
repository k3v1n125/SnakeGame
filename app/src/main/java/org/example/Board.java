package org.example;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.example.Item.Apple;
import org.example.Item.Item;
import org.example.Item.Pineapple;
import org.example.Item.Star;
import org.example.ItemFactory.AppleFactory;
import org.example.ItemFactory.ItemFactory;
import org.example.ItemFactory.PineappleFactory;
import org.example.ItemFactory.StarFactory;
import org.example.StatsBoard.StatsBoard;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = 900;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private final int introduceStar = 5;
    private final int introducePineapple = 25;
    private boolean starIntroduced = false;
    private boolean pineappleIntroduced = false;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean gameWon = false;

    private Timer timer;
    private Image dot;
    private Image head;

    private boolean moved = false;
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
        if (inGame) {
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

            if (paused) {
                String msg = "PAUSED";
                Font font = new Font("Helvetica", Font.BOLD, 50);
                FontMetrics metr = getFontMetrics(font);
                g.setColor(Color.WHITE);
                g.setFont(font);
                g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            drawEndStatus(g);
        }
    }

    private void drawEndStatus(Graphics g) {
        long duration = statsBoard.getGameTime();
        long gameMinutes = duration / 60;
        long gameSeconds = duration % 60;
        String durationMsg = gameSeconds + "s";
        if (gameMinutes >= 1) {
            durationMsg = gameMinutes + "m " + gameSeconds + "s";
        }
        int appleCollected = statsBoard.getAppleCollected();
        int starCollected = statsBoard.getStarCollected();
        
        String msg1 = gameWon ? "You Win" : "Game Over";
        String msg2 = "Duration: " + durationMsg;
        String msg3 = "Apples collected: " + appleCollected;
        String msg4 = "Stars collected: " + starCollected;
        String msg5 = "Click shift to restart or esc to exit";

        Font small = new Font("Helvetica", Font.BOLD, 28);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, B_HEIGHT / 2 - 56);
        g.drawLine(50, B_HEIGHT / 2 - 52, B_WIDTH - 50, B_HEIGHT / 2 - 52);
        g.drawString(msg2, (B_WIDTH - metr.stringWidth(msg2)) / 2, B_HEIGHT / 2 - 28);
        g.drawString(msg3, (B_WIDTH - metr.stringWidth(msg3)) / 2, B_HEIGHT / 2);
        g.drawString(msg4, (B_WIDTH - metr.stringWidth(msg4)) / 2, B_HEIGHT / 2 + 28);
        g.drawString(msg5, (B_WIDTH - metr.stringWidth(msg5)) / 2, B_HEIGHT / 2 + 56);
    }

    private void finishGame(boolean won) {
        gameWon = won;
        inGame = false;
        timer.stop();
        if (statsBoard != null) {
            statsBoard.getGameStats().gameEnded();
            statsBoard.checkHighScore();
            statsBoard.dispose();
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
        for (Item item : removedItems) {
            if (item instanceof Apple) {
                item.locateItem(this);
            }
        }
        for (Item item : removedItems) {
            if (item instanceof Pineapple) {
                item.locateItem(this);
            }
        }
        for (Item item : removedItems) {
            if (item instanceof Star) {
                item.locateItem(this);
            }
        }
    }

    private void refreshItemsAfterBoardChange(List<Item> removedItems) {
        items.removeAll(removedItems);

        if (isWinningState()) {
            items.clear();
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

        ensureAppleAvailability();
    }

    private void checkItem() {
        ArrayList<Item> removeItems = new ArrayList<>();
        for (Item item : items) {
            if ((x[0] == item.getX()) && (y[0] == item.getY())) {
                if (item instanceof Pineapple && statsBoard.getSnakeLength() == 1) {
                    finishGame(false);
                    removeItems.add(item);
                    continue;
                }
                item.itemEffect(statsBoard.getGameStats());
                removeItems.add(item);
            }
        }
        if (!inGame) {
            items.removeAll(removeItems);
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

    private void checkCollision() {
        for (int z = statsBoard.getSnakeLength(); z > 0; z--) {

            if ((x[0] == x[z]) && (y[0] == y[z])) {
                if (statsBoard.getExtraLife() >= 1) {
                    statsBoard.decreaseExtraLife();
                } else {
                    finishGame(false);
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

        if (inGame && !paused) {
            for (Item item : items) {
                if (item.existDuration().getSeconds() >= item.getExpireDuration().toSeconds()) {
                    removeItems.add(item);
                    if (item instanceof Apple) {
                        statsBoard.appleMissed();
                    } else if (item instanceof Star) {
                        statsBoard.starMissed();
                    }
                }
            }
            if (!removeItems.isEmpty()) {
                refreshItemsAfterBoardChange(removeItems);
            }
            checkItem();
            if (inGame) {
                checkCollision();
            }
            if (inGame) {
                move();
                statsBoard.checkStats();
                notifyStatsListener();
            }
            moved = false;
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

            if (moved) {
                return;
            }

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection) && (!leftDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
                if (paused) {
                    resume();
                }
                moved = true;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection) && (!rightDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                if (paused) {
                    resume();
                }
                moved = true;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection) && (!upDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                if (paused) {
                    resume();
                }
                moved = true;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection) && (!downDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                if (paused) {
                    resume();
                }
                moved = true;
            }
        }
    }
}
