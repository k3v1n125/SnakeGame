package org.example.Item;

import java.awt.Image;
import java.time.Duration;
import java.time.Instant;

import org.example.Board;
import org.example.ItemFactory.StarFactory;
import org.example.StatsBoard.GameStats;

public class Star extends Item {
    private StarFactory factory;
    private Duration expireDuration = Duration.ofSeconds(5);

    public Star(Image image, int x, int y, Instant applePlacedTime) {
        super(image, x, y, applePlacedTime);
        factory = new StarFactory(image);
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
    }
}
