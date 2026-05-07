package org.game.ItemFactory;

import java.time.Instant;

import org.game.Item.Item;
import org.game.Item.WallRemover;

public class WallRemoverFactory implements ItemFactory {
    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new WallRemover(x, y, placedTime);
    }
}
