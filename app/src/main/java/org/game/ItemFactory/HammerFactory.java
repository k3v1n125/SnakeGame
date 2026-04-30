package org.game.ItemFactory;

import java.time.Instant;

import org.game.Item.Hammer;
import org.game.Item.Item;

public class HammerFactory implements ItemFactory {

    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Hammer(x, y, placedTime);
    }
    
}
