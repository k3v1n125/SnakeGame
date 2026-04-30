package org.game.ItemFactory;

import java.time.Instant;

import org.game.Item.Item;
import org.game.Item.Pineapple;

public class PineappleFactory implements ItemFactory {
    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Pineapple(x, y, placedTime);
    }
}
