package org.example.HighScore;

public class HighScore {
    public int bestApples;
    public int bestLength;
    public long bestSurvivalSeconds;
    public long fastestAppleSeconds;   // fastest single apple collection

    public HighScore() {
        fastestAppleSeconds = Long.MAX_VALUE;   // sentinel — no record yet
    }
}
