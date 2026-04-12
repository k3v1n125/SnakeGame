package org.example.Achievement;

import java.io.*;
import java.util.*;

public class AchievementManager {

    private static final String SAVE_PATH = "saves/achievements.properties";

    private final Set<Achievement> unlocked = new HashSet<>();
    private AchievementListener listener;

    // Per-game counters (reset on restart)
    private int consecutiveCollected = 0;   // for PERFECTIONIST

    public AchievementManager() {
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
        if (elapsedSeconds >= 30) {
            tryUnlock(Achievement.SURVIVE_30);
        }
        if (elapsedSeconds >= 60) {
            tryUnlock(Achievement.SURVIVE_60);
        }
        if (elapsedSeconds >= 120) {
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

    // Reset per-game counters on restart (permanent unlocks stay)
    public void resetGame() {
        consecutiveCollected = 0;
    }

    private void tryUnlock(Achievement a) {
        if (unlocked.contains(a)) return;   // already unlocked
        unlocked.add(a);
        save();
        if (listener != null) listener.onAchievementUnlocked(a);
    }

    private void load() {
        File file = new File(SAVE_PATH);
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
            new File("saves").mkdirs();
            Properties p = new Properties();
            for (Achievement a : Achievement.values()) {
                p.setProperty(a.name(), String.valueOf(unlocked.contains(a)));
            }
            try (OutputStream out = new FileOutputStream(SAVE_PATH)) {
                p.store(out, "Snake Achievements");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
