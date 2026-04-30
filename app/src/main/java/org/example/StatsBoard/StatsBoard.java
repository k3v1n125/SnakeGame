package org.example.StatsBoard;

import java.time.Duration;
import java.time.Instant;

import javax.swing.*;

public class StatsBoard extends JFrame implements GameStatsListener {

    private GameStats gameStats;

    public StatsBoard(GameStats gameStats) {
        setTitle("Stats");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        this.gameStats = gameStats;
        add(gameStats);
        pack();
    }

    public GameStats getGameStats() {
        return gameStats;
    }

    public int getSnakeLength() {
        return gameStats.getSnakeLength();
    }

    public void pause() {
        gameStats.pause(Instant.now());
    }

    public Duration resume() {
        gameStats.resume(Instant.now());
        return gameStats.getPausedDuration();
    }

    public long getGameTime() {
        return gameStats.getGameTime();
    }

    public int getAppleCollected() {
        return gameStats.getAppleCollected();
    }

    public int getStarCollected() {
        return gameStats.getStarCollected();
    }

    public int getLives() {
        return gameStats.getLives();
    }

    public void setLives(int lives) {
        gameStats.setLives(lives);
    }

    public void decreaseLives() {
        gameStats.decreaseLives();
    }

    public void appleMissed() {
        gameStats.appleMissed();
    }

    public void starMissed() {
        gameStats.starMissed();
    }

    public void gameWon() {
        gameStats.gameWon();
    }

    public void checkStats() {
        gameStats.checkStats();
    }

    public void checkHighScore() {
        gameStats.checkHighScore();
    }

    @Override
    public void updateStats() {
        SwingUtilities.invokeLater(() -> {
            gameStats.updateStats();
        });
    }
}