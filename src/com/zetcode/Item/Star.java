package com.zetcode.Item;

import java.awt.Image;
import java.time.Instant;

import com.zetcode.Board;
import com.zetcode.ItemFactory.StarFactory;
import com.zetcode.StatsBoard.GameStats;

public class Star extends Item {
    private StarFactory factory;

    public Star(Image image, int x, int y, Instant applePlacedTime) {
        super(image, x, y, applePlacedTime);
        factory = new StarFactory(image);
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
