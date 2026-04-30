package org.example.Item;

import java.awt.Graphics;
import java.awt.Image;
import java.time.Duration;
import java.time.Instant;

import javax.swing.ImageIcon;

import org.example.Board;
import org.example.ItemFactory.HammerFactory;
import org.example.ItemFactory.ItemFactory;
import org.example.StatsBoard.GameStats;

public class Hammer extends Item {
    private static final Image IMAGE_STAGE_1 = new ImageIcon(Hammer.class.getResource("/hammer/1.png")).getImage();
    private static final Image IMAGE_STAGE_2 = new ImageIcon(Hammer.class.getResource("/hammer/2.png")).getImage();
    private static final Image IMAGE_STAGE_3 = new ImageIcon(Hammer.class.getResource("/hammer/3.png")).getImage();
    private ItemFactory factory;
    private static final long STAGE_1_END_SECONDS = 6;
    private static final long STAGE_2_END_SECONDS = 3;
    private Duration expireDuration = Duration.ofSeconds(9);
    
    public Hammer(int x, int y, Instant placedTime) {
        super(IMAGE_STAGE_1, x, y, placedTime, 3);
        factory = new HammerFactory();
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
    public void locateItem(Board board) {
        board.locateItem(factory);
    }

    @Override
    public void itemEffect(GameStats gameStats) {
        gameStats.increaseHammerCollected();
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
