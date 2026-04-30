package org.game.ItemFactory;

import java.time.Instant;

import org.game.Item.Item;
import org.game.Item.Star;

public class StarFactory implements ItemFactory {
    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Star(x, y, placedTime);
    }
}
