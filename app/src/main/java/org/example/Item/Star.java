package org.example.Item;

import java.awt.Graphics;
import java.awt.Image;
import java.time.Duration;
import java.time.Instant;

import javax.swing.ImageIcon;

import org.example.Board;
import org.example.ItemFactory.StarFactory;
import org.example.StatsBoard.GameStats;

public class Star extends Item {
    private static final Image IMAGE_STAGE_1 = new ImageIcon(Star.class.getResource("/star/1.png")).getImage();
    private static final Image IMAGE_STAGE_2 = new ImageIcon(Star.class.getResource("/star/2.png")).getImage();
    private static final Image IMAGE_STAGE_3 = new ImageIcon(Star.class.getResource("/star/3.png")).getImage();
    private static final long STAGE_1_END_SECONDS = 4;
    private static final long STAGE_2_END_SECONDS = 2;
    private StarFactory factory;
    private Duration expireDuration = Duration.ofSeconds(6);

    public Star(int x, int y, Instant starPlacedTime) {
        super(IMAGE_STAGE_1, x, y, starPlacedTime);
        factory = new StarFactory();
    }

    @Override
    public void draw(Graphics g, Board board) {
        long remainingSeconds = getExpireDuration().minus(existDuration()).getSeconds();
        if (remainingSeconds >= STAGE_1_END_SECONDS) {
            setImage(IMAGE_STAGE_1);
        } else if (remainingSeconds >= STAGE_2_END_SECONDS) {
            setImage(IMAGE_STAGE_2);
        } else {
            setImage(IMAGE_STAGE_3);
        }
        super.draw(g, board);
    }

    @Override
    public Duration getExpireDuration() {
        return expireDuration;
    }

    @Override
    public void locateItem(Board board) {
        board.locateItem(factory);
    }
    
    @Override
    public void itemEffect(GameStats gameStats) {
        gameStats.increaseStarCollected();
        Duration collectTime = Duration.between(getPlacedTime(), Instant.now());
        gameStats.setFastestStarCollected(collectTime.toSeconds());
    }
}
