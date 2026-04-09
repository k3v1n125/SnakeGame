package org.example.StatsBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.time.Duration;
import java.time.Instant;

import javax.swing.JPanel;

public class GameStats extends JPanel {
    private static final int WIDTH  = 220;
    private static final int HEIGHT = 160;
    private static final int LINE_HEIGHT = 25;
    private static final int START_Y = 50;
    private static final int LABEL_X = 15;
    private static final int VALUE_X = 175;

    private static final String STAR_NOT_UNLOCK = "Collect 4 apples to unlock stars";

    private int snakeLength;
    private int applesCollected;
    private int starCollected;
    private int extraLife = 0;
    private int extraLifeUsed = 0;

    private Instant startTime;
    private long gameTime;

    private Instant pauseTime = null;
    private Duration pausedDuration = Duration.ZERO;
    private Duration totalPausedDuration = Duration.ZERO;

    public GameStats(Instant startTime) {
        this.startTime = startTime;
        this.snakeLength = 3;
        this.applesCollected = 0;
        this.starCollected = 0;
        this.extraLife = 0;
        this.gameTime = 0;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public long getGameTime() {
        return gameTime;
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public int getStarCollected() {
        return starCollected;
    }

    public int getAppleCollected() {
        return applesCollected;
    }

    public int getExtraLife() {
        return extraLife;
    }

    public void decreaseExtraLife() {
        extraLifeUsed = extraLifeUsed + 1;
    }

    public void increaseSnakeLength() {
        snakeLength = snakeLength + 1;
    }

    public void increaseApplesCollected() {
        applesCollected = applesCollected + 1;
    }

    public void increaseStarCollected() {
        starCollected = starCollected + 1;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    public void pause(Instant pauseTime) {
        this.pauseTime = pauseTime;
    }

    public Duration getPausedDuration() {
        return pausedDuration;
    }

    public void resume(Instant resumeTime) {
        pausedDuration = Duration.between(pauseTime, resumeTime);
        totalPausedDuration = totalPausedDuration.plus(pausedDuration);
    }

    public void updateStats() {
        extraLife = (starCollected / 5) - extraLifeUsed;
        Duration gameDuration = Duration.between(startTime, Instant.now()).minus(totalPausedDuration);
        gameTime = gameDuration.toSeconds();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font titleFont = new Font("Helvetica", Font.BOLD, 14);
        Font labelFont = new Font("Helvetica", Font.PLAIN, 12);
        FontMetrics titleMetrics = getFontMetrics(titleFont);

        //title
        String title = "Game Stats";
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString(title, (WIDTH - titleMetrics.stringWidth(title)) / 2, 22);

        //line
        g.setColor(Color.GRAY);
        g.drawLine(LABEL_X, 30, WIDTH - LABEL_X, 30);

        //stats
        g.setFont(labelFont);

        String[][] rows = {
            { "Snake Length:", String.valueOf(snakeLength) },
            { "Apples Collected:", String.valueOf(applesCollected) },
            { "Stars Collected:", String.valueOf(starCollected) },
            { "Extra Lives:", String.valueOf(extraLife) },
            { "Duration:", gameTime + "s" }
        };

        int lines = 0;
        for (int i = 0; i < rows.length; i++) {
            int y = START_Y + lines * LINE_HEIGHT;
            g.setColor(Color.LIGHT_GRAY);
            if ((i == 2 || i == 3) && applesCollected < 4) {
                continue;
            }
            g.drawString(rows[i][0], LABEL_X, y);   // label
            g.setColor(Color.GREEN);
            g.drawString(rows[i][1], VALUE_X, y);   // value
            lines = lines + 1;
        }
        if (applesCollected < 4) {
            g.setColor(Color.YELLOW);
            g.drawString(STAR_NOT_UNLOCK, LABEL_X, START_Y + lines * LINE_HEIGHT);
        }
    }
}
