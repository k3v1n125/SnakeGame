package org.example.Achievement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ToolTipManager;

public class AchievementStats extends JPanel {
    private static final int WIDTH = 300;
    private static final int ROW_HEIGHT = 36;
    private static final int PADDING = 14;
    private static final int TOAST_H = 28;
    private static final int CATEGORY_HEADER_HEIGHT = 24;
    private static final int CATEGORY_GAP = 8;

    private final AchievementManager manager;

    private String toastMessage = null;
    private long toastExpiry = 0;

    // Timer to repaint while toast is visible
    private final Timer toastTimer;

    public AchievementStats(AchievementManager manager) {
        this.manager = manager;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, getContentHeight()));
        setToolTipText("");
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().registerComponent(this);

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

    private int getContentHeight() {
        int categoryCount = AchievementCategory.values().length;
        return TOAST_H
                + PADDING * 2
                + 6
                + categoryCount * CATEGORY_HEADER_HEIGHT
                + Achievement.values().length * ROW_HEIGHT
                + categoryCount * CATEGORY_GAP;
    }

    private Map<AchievementCategory, List<Achievement>> getGroupedAchievements() {
        Map<AchievementCategory, List<Achievement>> grouped = new LinkedHashMap<>();
        for (AchievementCategory cat : AchievementCategory.values()) {
            grouped.put(cat, new ArrayList<>());
        }
        for (Achievement achievement : Achievement.values()) {
            grouped.get(achievement.category).add(achievement);
        }
        return grouped;
    }

    private String getDisplayDescription(Graphics g, String description, int maxWidth) {
        FontMetrics metrics = g.getFontMetrics();
        if (metrics.stringWidth(description) <= maxWidth) {
            return description;
        }

        String ellipsis = "...";
        int ellipsisWidth = metrics.stringWidth(ellipsis);
        int allowedWidth = maxWidth - ellipsisWidth;

        if (allowedWidth <= 0) {
            return ellipsis;
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < description.length(); index++) {
            char ch = description.charAt(index);
            if (metrics.stringWidth(builder.toString() + ch) > allowedWidth) {
                break;
            }
            builder.append(ch);
        }
        return builder + ellipsis;
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        int y = TOAST_H + PADDING * 2 + 6;

        for (Map.Entry<AchievementCategory, List<Achievement>> entry : getGroupedAchievements().entrySet()) {
            y += CATEGORY_HEADER_HEIGHT;

            for (Achievement achievement : entry.getValue()) {
                int rowTop = y - 14;
                int rowBottom = rowTop + ROW_HEIGHT - 4;
                if (event.getY() >= rowTop && event.getY() <= rowBottom) {
                    return achievement.description;
                }
                y += ROW_HEIGHT;
            }

            y += CATEGORY_GAP;
        }

        return null;
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

        // ── Achievement rows grouped by category ──────────
        int y = TOAST_H + PADDING * 2 + 6;

        // Group achievements by category
        Map<AchievementCategory, List<Achievement>> grouped = getGroupedAchievements();

        // Draw categories with their achievements
        for (Map.Entry<AchievementCategory, List<Achievement>> entry : grouped.entrySet()) {
            // Category header
            g.setFont(new Font("Helvetica", Font.BOLD, 12));
            g.setColor(new Color(255, 215, 0));
            g.drawString(entry.getKey().displayName, PADDING, y);
            y += CATEGORY_HEADER_HEIGHT;

            // Achievements in this category
            for (Achievement a : entry.getValue()) {
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
                int descriptionX = PADDING + 20;
                int descriptionMaxWidth = WIDTH - descriptionX - PADDING;
                String displayDescription = getDisplayDescription(g, a.description, descriptionMaxWidth);
                g.drawString(displayDescription, descriptionX, y + 13);

                y += ROW_HEIGHT;
            }

            y += CATEGORY_GAP;
        }
    }
}
