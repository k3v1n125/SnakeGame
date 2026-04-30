package org.game.HighScore;

public class HighScore {
    private int bestApples;
    private long bestSurvivalSeconds;
    private long fastestAppleSeconds;   // fastest single apple collection
    private int bestWallsDestroyed;

    public HighScore() {
        fastestAppleSeconds = Long.MAX_VALUE;   // sentinel — no record yet
        bestSurvivalSeconds = 0;
    }

    public HighScore(int bestApples, long bestSurvivalSeconds, long fastestAppleSeconds) {
        setBestApples(bestApples);
        setBestSurvivalSeconds(bestSurvivalSeconds);
        setFastestAppleSeconds(fastestAppleSeconds);
    }

    public void setBestApples(int bestApples) {
        this.bestApples = bestApples;
    }

    public boolean setBestApples(int bestApples, boolean beaten) {
        if (bestApples > this.bestApples) {
            setBestApples(bestApples);
            beaten = true;
        }
        return beaten;
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

    public boolean setBestSurvivalSeconds(long bestSurvivalSeconds, boolean beaten) {
        if (bestSurvivalSeconds > this.bestSurvivalSeconds) {
            setBestSurvivalSeconds(bestSurvivalSeconds);
            beaten = true;
        }
        return beaten;
    }

    public long getFastestAppleSeconds() {
        return fastestAppleSeconds;
    }

    public void setFastestAppleSeconds(long fastestAppleSeconds) {
        this.fastestAppleSeconds = fastestAppleSeconds;
    }

    public boolean setFastestAppleSeconds(long fastestAppleSeconds, boolean beaten) {
        if (fastestAppleSeconds < this.fastestAppleSeconds) {
            setFastestAppleSeconds(fastestAppleSeconds);
            beaten = true;
        }
        return beaten;
    }

    public int getBestWallsDestroyed() {
        return bestWallsDestroyed;
    }

    public void setBestWallsDestroyed(int bestWallsDestroyed) {
        this.bestWallsDestroyed = bestWallsDestroyed;
    }

    public boolean setBestWallsDestroyed(int bestWallsDestroyed, boolean beaten) {
        if (bestWallsDestroyed > this.bestWallsDestroyed) {
            setBestWallsDestroyed(bestWallsDestroyed);
            beaten = true;
        }
        return beaten;
    }
}
