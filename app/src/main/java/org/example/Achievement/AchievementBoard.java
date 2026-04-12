package org.example.Achievement;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class AchievementBoard extends JFrame implements AchievementListener {
    private final AchievementStats stats;

    public AchievementBoard(AchievementManager achievementManager) {
        setTitle("Achievements");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        stats = new AchievementStats(achievementManager);
        add(stats);
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