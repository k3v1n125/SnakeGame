package org.example.ItemFactory;

import java.time.Instant;

import org.example.Item.Item;
import org.example.Item.Pineapple;

public class PineappleFactory implements ItemFactory {
    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Pineapple(x, y, placedTime);
    }
}
