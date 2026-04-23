package org.example.ItemFactory;

import java.time.Instant;

import org.example.Item.Star;
import org.example.Item.Item;

public class StarFactory implements ItemFactory {
    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Star(x, y, placedTime);
    }
}
