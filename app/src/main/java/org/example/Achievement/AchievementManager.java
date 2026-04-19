package org.example.Achievement;

import java.io.*;
import java.util.*;

public class AchievementManager {

    private static final String SAVE_PATH = "saves/achievements.properties";

    private final String savePath;
    private final Set<Achievement> unlocked = new HashSet<>();
    private AchievementListener listener;

    // Per-game counters (reset on restart)
    private int consecutiveCollected = 0;   // for PERFECTIONIST
    private int consecutiveStarsCollected = 0;

    public AchievementManager() {
        this(SAVE_PATH);
    }

    public AchievementManager(String savePath) {
        this.savePath = savePath;
        load();
    }

    public void setListener(AchievementListener listener) {
        this.listener = listener;
    }

    public Set<Achievement> getUnlocked() {
        return Collections.unmodifiableSet(unlocked);
    }

    public boolean isUnlocked(Achievement a) {
        return unlocked.contains(a);
    }

    // Called each game tick with current stats
    public void checkStats(int apples, int length, long elapsedSeconds) {
        if (apples >= 1) {
            tryUnlock(Achievement.FIRST_APPLE);
        }
        if (apples >= 10) {
            tryUnlock(Achievement.TEN_APPLES);
        }
        if (apples >= 20) {
            tryUnlock(Achievement.TWENTY_APPLES);
        }
        if (length >= 10) {
            tryUnlock(Achievement.LENGTH_TEN);
        }
        if (length >= 20) {
            tryUnlock(Achievement.LENGTH_TWENTY);
        }
    }

    public void onGameEnded(int apples, long elapsedSeconds) {
        if (elapsedSeconds >= 30) {
            tryUnlock(Achievement.SURVIVE_30);
        }
        if (elapsedSeconds >= 60) {
            if (apples == 0) {
                tryUnlock(Achievement.SURVIVE_60_NO_APPLE);
            }
            tryUnlock(Achievement.SURVIVE_60);
        }
        if (elapsedSeconds >= 120) {
            if (apples == 0) {
                tryUnlock(Achievement.SURVIVE_120_NO_APPLE);
            }
            tryUnlock(Achievement.SURVIVE_120);
        }
    }

    // Called when an apple is collected — pass seconds taken to collect it
    public void onAppleCollected(long secondsTaken) {
        consecutiveCollected++;
        if (secondsTaken < 2) {
            tryUnlock(Achievement.SPEED_DEMON);
        }
        if (consecutiveCollected >= 5) {
            tryUnlock(Achievement.PERFECTIONIST);
        }
    }

    // Called when an apple is missed
    public void onAppleMissed() {
        consecutiveCollected = 0;   // streak broken
    }

    public void onStarCollected() {
        consecutiveStarsCollected++;
        if (consecutiveStarsCollected >= 5) {
            tryUnlock(Achievement.STAR_KEEPER);
        }
    }

    public void onStarMissed() {
        consecutiveStarsCollected = 0;
    }

    public void onGameWon() {
        tryUnlock(Achievement.BOARD_FILLER);
    }

    // Reset per-game counters on restart (permanent unlocks stay)
    public void resetGame() {
        consecutiveCollected = 0;
        consecutiveStarsCollected = 0;
    }

    private void tryUnlock(Achievement a) {
        if (unlocked.contains(a)) return;   // already unlocked
        unlocked.add(a);
        save();
        if (listener != null) listener.onAchievementUnlocked(a);
    }

    private void load() {
        File file = new File(savePath);
        if (!file.exists()) return;
        try (InputStream in = new FileInputStream(file)) {
            Properties p = new Properties();
            p.load(in);
            for (Achievement a : Achievement.values()) {
                if ("true".equals(p.getProperty(a.name()))) {
                    unlocked.add(a);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            File file = new File(savePath);
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            Properties p = new Properties();
            for (Achievement a : Achievement.values()) {
                p.setProperty(a.name(), String.valueOf(unlocked.contains(a)));
            }
            try (OutputStream out = new FileOutputStream(file)) {
                p.store(out, "Snake Achievements");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
