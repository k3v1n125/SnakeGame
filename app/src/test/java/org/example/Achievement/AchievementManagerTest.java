package org.example.Achievement;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.game.Achievement.Achievement;
import org.game.Achievement.AchievementManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AchievementManagerTest {

    @TempDir
    Path tempDir;

    private AchievementManager createManager() {
        return new AchievementManager(tempDir.resolve("achievements.properties").toString());
    }

    @Test
    void unlocksStarKeeperAfterFiveStarsWithoutMiss() {
        AchievementManager achievementManager = createManager();

        for (int i = 0; i < 5; i++) {
            achievementManager.onStarCollected();
        }

        assertTrue(achievementManager.isUnlocked(Achievement.STAR_KEEPER));
    }

    @Test
    void starMissBreaksStarKeeperStreak() {
        AchievementManager achievementManager = createManager();

        for (int i = 0; i < 4; i++) {
            achievementManager.onStarCollected();
        }
        achievementManager.onStarMissed();
        achievementManager.onStarCollected();

        assertFalse(achievementManager.isUnlocked(Achievement.STAR_KEEPER));
    }

    @Test
    void unlocksBoardFillerOnGameWon() {
        AchievementManager achievementManager = createManager();

        achievementManager.onGameWon();

        assertTrue(achievementManager.isUnlocked(Achievement.BOARD_FILLER));
    }

    @Test
    void doesNotUnlockGameTimeAchievementDuringLiveStatCheck() {
        AchievementManager achievementManager = createManager();

        achievementManager.checkStats(0, 3, 60);

        assertFalse(achievementManager.isUnlocked(Achievement.SURVIVE_60));
    }

    @Test
    void unlocksGameTimeAchievementsWhenGameEnds() {
        AchievementManager achievementManager = createManager();

        achievementManager.onGameEnded(0, 120);

        assertTrue(achievementManager.isUnlocked(Achievement.SURVIVE_30));
        assertTrue(achievementManager.isUnlocked(Achievement.SURVIVE_60));
        assertTrue(achievementManager.isUnlocked(Achievement.SURVIVE_120));
        assertTrue(achievementManager.isUnlocked(Achievement.SURVIVE_60_NO_APPLE));
        assertTrue(achievementManager.isUnlocked(Achievement.SURVIVE_120_NO_APPLE));
    }
}