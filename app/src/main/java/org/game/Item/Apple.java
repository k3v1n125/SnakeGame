package org.game.Item;

import java.awt.Graphics;
import java.awt.Image;
import java.time.Duration;
import java.time.Instant;

import javax.swing.ImageIcon;

import org.game.Board;
import org.game.ItemFactory.AppleFactory;
import org.game.StatsBoard.GameStats;

public class Apple extends Item {
    private static final Image IMAGE_STAGE_1 = new ImageIcon(Apple.class.getResource("/apple/1.png")).getImage();
    private static final Image IMAGE_STAGE_2 = new ImageIcon(Apple.class.getResource("/apple/2.png")).getImage();
    private static final Image IMAGE_STAGE_3 = new ImageIcon(Apple.class.getResource("/apple/3.png")).getImage();
    private static final long STAGE_1_END_SECONDS = 7;
    private static final long STAGE_2_END_SECONDS = 4;
    private AppleFactory factory;
    private Duration expireDuration = Duration.ofSeconds(10);

    public Apple(int x, int y, Instant applePlacedTime) {
        super(IMAGE_STAGE_1, x, y, applePlacedTime, 0);
        factory = new AppleFactory();
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
        gameStats.increaseSnakeLength();
        gameStats.increaseApplesCollected();
        Duration collectTime = Duration.between(getPlacedTime(), Instant.now());
        gameStats.setFastestAppleCollected(collectTime.toSeconds());
    }

    @Override
    public void itemMissed(GameStats gameStats) {
        gameStats.appleMissed();
    }
}
