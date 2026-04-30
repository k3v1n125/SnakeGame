package org.game.ItemFactory;

import java.time.Instant;

import org.game.Item.Item;

public interface ItemFactory {
    Item create(int x, int y, Instant placedTime);
}
