package org.example.ItemFactory;

import java.time.Instant;

import org.example.Item.Hammer;
import org.example.Item.Item;

public class HammerFactory implements ItemFactory {

    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Hammer(x, y, placedTime);
    }
    
}
