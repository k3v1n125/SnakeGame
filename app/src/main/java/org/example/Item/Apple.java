package org.example.Item;

import java.awt.Image;
import java.time.Duration;
import java.time.Instant;

import org.example.Board;
import org.example.ItemFactory.AppleFactory;
import org.example.StatsBoard.GameStats;

public class Apple extends Item {
    private AppleFactory factory;
    private Duration expireDuration = Duration.ofSeconds(5);

    public Apple(Image image, int x, int y, Instant applePlacedTime) {
        super(image, x, y, applePlacedTime);
        factory = new AppleFactory(image);
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
    }
}
