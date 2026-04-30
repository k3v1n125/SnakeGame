package org.example.Item;

import java.awt.Graphics;
import java.awt.Image;
import java.time.Duration;
import java.time.Instant;

import javax.swing.ImageIcon;

import org.example.Board;
import org.example.ItemFactory.PineappleFactory;
import org.example.StatsBoard.GameStats;

public class Pineapple extends Item {
    private static final Image IMAGE_STAGE_1 = new ImageIcon(Pineapple.class.getResource("/pineapple/1.png")).getImage();
    private static final Image IMAGE_STAGE_2 = new ImageIcon(Pineapple.class.getResource("/pineapple/2.png")).getImage();
    private static final Image IMAGE_STAGE_3 = new ImageIcon(Pineapple.class.getResource("/pineapple/3.png")).getImage();
    private static final long STAGE_1_END_SECONDS = 7;
    private static final long STAGE_2_END_SECONDS = 4;
    private PineappleFactory factory;
    private Duration expireDuration = Duration.ofSeconds(10);

    public Pineapple(int x, int y, Instant pineapplePlacedTime) {
        super(IMAGE_STAGE_1, x, y, pineapplePlacedTime, 1);
        factory = new PineappleFactory();
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
        gameStats.increasePineappleCollected();
        gameStats.decreaseSnakeLength();
    }

    @Override
    public void itemMissed(GameStats gameStats) {
        return;
    }
}
