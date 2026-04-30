package org.game.Achievement;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class AchievementBoard extends JFrame implements AchievementListener {
    private final AchievementStats stats;

    public AchievementBoard(AchievementManager achievementManager) {
        setTitle("Achievements");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        stats = new AchievementStats(achievementManager);
        JScrollPane scrollPane = new JScrollPane(stats);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        // Hides horizontal scrollbar but keeps it functional
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane);
        scrollPane.setPreferredSize(new java.awt.Dimension(300, 600));
        pack();
    }

    @Override
    public void onAchievementUnlocked(Achievement achievement) {
        SwingUtilities.invokeLater(() -> {
            stats.showToast(achievement.title + " unlocked");
            stats.repaint();
        });
    }
}