package org.example.Achievement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AchievementStats extends JPanel {
    private static final int WIDTH = 260;
    private static final int ROW_HEIGHT = 36;
    private static final int PADDING = 14;
    private static final int TOAST_H = 28;

    private final AchievementManager manager;

    private String toastMessage = null;
    private long toastExpiry = 0;

    // Timer to repaint while toast is visible
    private final Timer toastTimer;

    public AchievementStats(AchievementManager manager) {
        this.manager = manager;
        setBackground(Color.BLACK);

        int height = TOAST_H + PADDING
                + Achievement.values().length * ROW_HEIGHT
                + PADDING;
        setPreferredSize(new Dimension(WIDTH, height));

        // Repaint every 500ms so toast expires cleanly without an event
        toastTimer = new Timer(500, e -> {
            if (toastMessage != null
                    && System.currentTimeMillis() >= toastExpiry) {
                toastMessage = null;
                repaint();
            }
        });
        toastTimer.start();
    }

    public void showToast(String msg) {
        toastMessage = msg;
        toastExpiry = System.currentTimeMillis() + 3000;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font titleFont = new Font("Helvetica", Font.BOLD, 13);
        Font rowFont = new Font("Helvetica", Font.PLAIN, 11);
        Font subFont = new Font("Helvetica", Font.ITALIC, 10);

        // ── Toast ──────────────────────────────────────────
        if (toastMessage != null
                && System.currentTimeMillis() < toastExpiry) {

            g.setFont(titleFont);
            FontMetrics toastFm = getFontMetrics(titleFont);

            String toastText = toastMessage + " 🏆"; // no emoji

            int textWidth = toastFm.stringWidth(toastText);
            int textHeight = toastFm.getAscent(); // height above baseline

            // Padding around the text
            int toastPadX = 10;
            int toastPadY = 6;

            // Expand rectangle to fit text if needed
            int rectWidth = Math.max(WIDTH - 12, textWidth + toastPadX * 2);
            int rectHeight = textHeight + toastPadY * 2;

            g.setColor(new Color(255, 215, 0));
            g.fillRoundRect(6, 4, rectWidth, rectHeight, 8, 8);

            g.setColor(Color.BLACK);
            g.drawString(toastText, 6 + toastPadX, 4 + toastPadY + textHeight);
        }

        // ── Title ──────────────────────────────────────────
        int unlockCount = manager.getUnlocked().size();
        int total = Achievement.values().length;
        String heading = "Achievements  " + unlockCount + " / " + total;

        g.setColor(Color.WHITE);
        g.setFont(titleFont);
        FontMetrics fm = getFontMetrics(titleFont);
        g.drawString(heading,
                (WIDTH - fm.stringWidth(heading)) / 2,
                TOAST_H + PADDING);

        // ── Divider ────────────────────────────────────────
        g.setColor(Color.DARK_GRAY);
        g.drawLine(PADDING, TOAST_H + PADDING + 6,
                WIDTH - PADDING, TOAST_H + PADDING + 6);

        // ── Achievement rows ───────────────────────────────
        int y = TOAST_H + PADDING * 2 + 6;

        for (Achievement a : Achievement.values()) {
            boolean earned = manager.isUnlocked(a);

            // Row background for unlocked achievements
            if (earned) {
                g.setColor(new Color(30, 60, 30));
                g.fillRoundRect(PADDING - 4, y - 14,
                        WIDTH - (PADDING - 4) * 2, ROW_HEIGHT - 4,
                        6, 6);
            }

            // Icon
            g.setFont(new Font("Helvetica", Font.BOLD, 14));
            g.setColor(earned ? new Color(80, 200, 80) : Color.DARK_GRAY);
            g.drawString(earned ? "✓" : "○", PADDING, y);

            // Title
            g.setFont(rowFont);
            g.setColor(earned ? Color.WHITE : Color.GRAY);
            g.drawString(a.title, PADDING + 20, y);

            // Description
            g.setFont(subFont);
            g.setColor(earned ? new Color(150, 200, 150) : new Color(80, 80, 80));
            g.drawString(a.description, PADDING + 20, y + 13);

            y += ROW_HEIGHT;
        }
    }
}
