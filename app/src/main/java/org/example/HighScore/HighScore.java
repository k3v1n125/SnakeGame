package org.example.HighScore;

public class HighScore {
    private int bestApples;
    private long bestSurvivalSeconds;
    private long fastestAppleSeconds;   // fastest single apple collection

    public HighScore() {
        fastestAppleSeconds = Long.MAX_VALUE;   // sentinel — no record yet
        bestSurvivalSeconds = Long.MIN_VALUE;
    }

    public HighScore(int bestApples, long bestSurvivalSeconds, long fastestAppleSeconds) {
        setBestApples(bestApples);
        setBestSurvivalSeconds(bestSurvivalSeconds);
        setFastestAppleSeconds(fastestAppleSeconds);
    }

    public void setBestApples(int bestApples) {
        this.bestApples = bestApples;
    }

    public int getBestApples() {
        return bestApples;
    }

    public long getBestSurvivalSeconds() {
        return bestSurvivalSeconds;
    }

    public void setBestSurvivalSeconds(long bestSurvivalSeconds) {
        this.bestSurvivalSeconds = bestSurvivalSeconds;
    }

    public long getFastestAppleSeconds() {
        return fastestAppleSeconds;
    }

    public void setFastestAppleSeconds(long fastestAppleSeconds) {
        this.fastestAppleSeconds = fastestAppleSeconds;
    }
}
