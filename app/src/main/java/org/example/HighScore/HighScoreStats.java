package org.example.HighScore;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

public class HighScoreStats extends JPanel {
    private static final int WIDTH = 220;
    private static final int HEIGHT = 170;
    private static final int PADDING = 16;
    private static final int LINE_HEIGHT = 28;

    private final HighScoreManager highScoreManager;

    public HighScoreStats(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Keep view in sync when records update after game over.
        new Timer(500, e -> repaint()).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font titleFont = new Font("Helvetica", Font.BOLD, 14);
        Font labelFont = new Font("Helvetica", Font.PLAIN, 12);

        HighScore record = highScoreManager.getRecord();

        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        String title = "High Scores";
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(title, (WIDTH - metrics.stringWidth(title)) / 2, 24);

        g.setColor(Color.GRAY);
        g.drawLine(PADDING, 34, WIDTH - PADDING, 34);

        g.setFont(labelFont);
        int y = 64;

        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Best Apples:", PADDING, y);
        g.setColor(Color.GREEN);
        g.drawString(String.valueOf(record.getBestApples()), WIDTH - 60, y);

        y += LINE_HEIGHT;
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Longest Time:", PADDING, y);
        g.setColor(Color.GREEN);
        g.drawString(record.getBestSurvivalSeconds() + "s", WIDTH - 60, y);

        y += LINE_HEIGHT;
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Fastest Apple:", PADDING, y);
        g.setColor(Color.GREEN);
        long fastestApple = record.getFastestAppleSeconds();
        String fastestText = fastestApple == Long.MAX_VALUE ? "N/A" : fastestApple + "s";
        g.drawString(fastestText, WIDTH - 60, y);
    }
}