package com.zetcode.StatsBoard;

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

    public long getGameTime() {
        return gameStats.getGameTime();
    }

    public int getAppleCollected() {
        return gameStats.getAppleCollected();
    }

    public int getStarCollected() {
        return gameStats.getStarCollected();
    }

    public int getExtraLife() {
        return gameStats.getExtraLife();
    }

    public void decreaseExtraLife() {
        gameStats.decreaseExtraLife();
    }

    public void setEndTime(Instant endTime) {
        gameStats.setEndTime(endTime);
    }

    @Override
    public void updateStats() {
        SwingUtilities.invokeLater(() -> {
            gameStats.updateStats();
        });
    }
}