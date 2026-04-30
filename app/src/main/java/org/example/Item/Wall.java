package org.example.Item;

import java.awt.Image;
import java.time.Duration;
import java.time.Instant;

import javax.swing.ImageIcon;

import org.example.Board;
import org.example.StatsBoard.GameStats;

public class Wall extends Item {
    private static final Image IMAGE = new ImageIcon(Wall.class.getResource("/wall.jpg")).getImage();
    private Duration expireDuration = Duration.ofSeconds(Long.MAX_VALUE);

    public Wall(int x, int y, Instant placedTime) {
        super(IMAGE, x, y, placedTime, -1);
    }

    @Override
    public void locateItem(Board board) {
        return;
    }

    @Override
    public void itemEffect(GameStats gameStats) {
        gameStats.decreaseUseableHammer(5);
        if (gameStats.getUseableHammer() < 0) {
            gameStats.decreaseLives();
            gameStats.setUseableHammer(0);
        } else {
            gameStats.increaseWallsDestroyed();
        }
    }

    @Override
    public Duration getExpireDuration() {
        return expireDuration;
    }

    @Override
    public void itemMissed(GameStats gameStats) {
        return;
    }
    
}
