package org.game.ItemFactory;

import java.time.Instant;

import org.game.Item.Apple;
import org.game.Item.Item;

public class AppleFactory implements ItemFactory {
    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Apple(x, y, placedTime);
    }
}
