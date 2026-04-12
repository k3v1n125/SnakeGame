package org.example.HighScore;

import java.io.*;
import java.util.Properties;

public class HighScoreManager {

    private static final String SAVE_PATH = "saves/highscore.properties";
    private final HighScore record;

    public HighScoreManager() {
        record = load();
    }

    public HighScore getRecord() { return record; }

    // Returns true if any record was beaten
    public boolean update(int apples, int length,
                          long survivalSeconds, long fastestApple) {
        boolean beaten = false;
        if (apples > record.bestApples) {
            record.bestApples = apples; beaten = true;
        }
        if (length > record.bestLength) {
            record.bestLength = length; beaten = true;
        }
        if (survivalSeconds > record.bestSurvivalSeconds) {
            record.bestSurvivalSeconds = survivalSeconds; beaten = true;
        }
        if (fastestApple > 0 && fastestApple < record.fastestAppleSeconds) {
            record.fastestAppleSeconds = fastestApple; beaten = true;
        }
        if (beaten) save();
        return beaten;
    }

    private HighScore load() {
        HighScore hs = new HighScore();
        File file = new File(SAVE_PATH);
        if (!file.exists()) return hs;
        try (InputStream in = new FileInputStream(file)) {
            Properties p = new Properties();
            p.load(in);
            hs.bestApples          = Integer.parseInt(p.getProperty("bestApples", "0"));
            hs.bestLength          = Integer.parseInt(p.getProperty("bestLength", "0"));
            hs.bestSurvivalSeconds = Long.parseLong(p.getProperty("bestSurvival", "0"));
            hs.fastestAppleSeconds = Long.parseLong(
                p.getProperty("fastestApple", String.valueOf(Long.MAX_VALUE)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hs;
    }

    private void save() {
        try {
            new File("saves").mkdirs();   // create saves/ folder if missing
            Properties p = new Properties();
            p.setProperty("bestApples",   String.valueOf(record.bestApples));
            p.setProperty("bestLength",   String.valueOf(record.bestLength));
            p.setProperty("bestSurvival", String.valueOf(record.bestSurvivalSeconds));
            p.setProperty("fastestApple", String.valueOf(record.fastestAppleSeconds));
            try (OutputStream out = new FileOutputStream(SAVE_PATH)) {
                p.store(out, "Snake High Scores");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
