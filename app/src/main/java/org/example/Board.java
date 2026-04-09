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
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.example.Item.Item;
import org.example.ItemFactory.AppleFactory;
import org.example.ItemFactory.ItemFactory;
import org.example.ItemFactory.StarFactory;
import org.example.StatsBoard.StatsBoard;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private final int introduceStar = 4;
    private boolean starIntroduced = false;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image dot;
    private Image appleImage;
    private Image starImage;
    private Image head;

    private boolean moved = false;
    private boolean paused = false;

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

        ImageIcon iia = new ImageIcon(getClass().getResource("/apple.png"));
        appleImage = iia.getImage();
        ImageIcon iis = new ImageIcon(getClass().getResource("/star.png"));
        starImage = iis.getImage();

        ImageIcon iih = new ImageIcon(getClass().getResource("/head.png"));
        head = iih.getImage();
    }

    private void initGame() {

        for (int z = 0; z < statsBoard.getSnakeLength(); z++) {
            x[z] = 4 * DOT_SIZE - z * DOT_SIZE;
            y[z] = 2 * DOT_SIZE;
        }
        
        locateItem(new AppleFactory(appleImage));

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
            if (paused) {
                String msg = "PAUSED";
                Font font = new Font("Helvetica", Font.BOLD, 14);
                FontMetrics metr = getFontMetrics(font);
                g.setColor(Color.WHITE);
                g.setFont(font);
                g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
            }

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

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        long duration = statsBoard.getGameTime();
        int appleCollected = statsBoard.getAppleCollected();
        
        String msg1 = "Game Over";
        String msg2 = "Duration: " + duration + " seconds";
        String msg3 = "Apples collected: " + appleCollected;
        String msg4 = "Stars collected: " + statsBoard.getStarCollected();
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

    private void checkItem() {
        ArrayList<Item> removeItems = new ArrayList<>();
        for (Item item : items) {
            if ((x[0] == item.getX()) && (y[0] == item.getY())) {
                item.itemEffect(statsBoard.getGameStats());
                removeItems.add(item);
            }
        }
        items.removeAll(removeItems);
        for (Item item : removeItems) {
            item.locateItem(this);
        }
        if (statsBoard.getAppleCollected() == introduceStar && !starIntroduced) {
            locateItem(new StarFactory(starImage));
            starIntroduced = true;
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
                    inGame = false;
                }
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
            if (statsBoard != null) {
                statsBoard.dispose();
            }
        }
    }

    public void locateItem(ItemFactory factory) {
        int r = (int) (Math.random() * RAND_POS);
        int item_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        int item_y = ((r * DOT_SIZE));
        items.add(factory.create(item_x, item_y, Instant.now()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Item> removeItems = new ArrayList<Item>();

        if (inGame && !paused) {
            for (Item item : items) {
                if (item.existDuration().getSeconds() >= item.getExpireDuration().toSeconds()) {
                    removeItems.add(item);
                }
            }
            items.removeAll(removeItems);
            for (Item item : removeItems) {
                item.locateItem(this);
            }
            checkItem();
            checkCollision();
            move();
            moved = false;
            notifyStatsListener();
        }

        repaint();
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
                } else {
                    Duration pauseDuration = statsBoard.resume();
                    for (Item item : items) {
                        item.setPauseDuration(pauseDuration);
                    }
                }
                return;
            }

            if (moved) {
                return;
            }

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            moved = true;
        }
    }
}
