package org.game.HighScore;

import java.io.*;
import java.util.Properties;

import org.game.SavePaths;

public class HighScoreManager {

    private static final String SAVE_PATH = SavePaths.appSupportFile("highscore.properties");
    private final HighScore record;

    public HighScoreManager() {
        record = load();
    }

    public HighScore getRecord() { return record; }

    // Returns true if any record was beaten
    public boolean update(int apples,
                          long survivalSeconds, long fastestApple, int wallsDestroyed) {
        boolean beaten = false;
        beaten = record.setBestApples(apples, beaten);
        beaten = record.setBestSurvivalSeconds(survivalSeconds, beaten);
        beaten = record.setFastestAppleSeconds(fastestApple, beaten);
        beaten = record.setBestWallsDestroyed(wallsDestroyed, beaten);
        if (beaten) {
            save();
        }
        return beaten;
    }

    private HighScore load() {
        HighScore hs = new HighScore();
        File file = new File(SAVE_PATH);
        if (!file.exists()) return hs;
        try (InputStream in = new FileInputStream(file)) {
            Properties p = new Properties();
            p.load(in);
            int bestApples = Integer.parseInt(p.getProperty("bestApples", "0"));
            long bestSurvivalSeconds = Long.parseLong(p.getProperty("bestSurvival", "0"));
            long fastestAppleSeconds = Long.parseLong(
                p.getProperty("fastestApple", String.valueOf(Long.MAX_VALUE)));
            hs = new HighScore(bestApples, bestSurvivalSeconds, fastestAppleSeconds);
            hs.setBestWallsDestroyed(Integer.parseInt(p.getProperty("bestWallsDestroyed", "0")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hs;
    }

    private void save() {
        try {
            SavePaths.appSupportDirectoryFile().mkdirs();
            Properties p = new Properties();
            p.setProperty("bestApples", String.valueOf(record.getBestApples()));
            p.setProperty("bestSurvival", String.valueOf(record.getBestSurvivalSeconds()));
            p.setProperty("fastestApple", String.valueOf(record.getFastestAppleSeconds()));
            p.setProperty("bestWallsDestroyed", String.valueOf(record.getBestWallsDestroyed()));
            try (OutputStream out = new FileOutputStream(SAVE_PATH)) {
                p.store(out, "Snake High Scores");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
