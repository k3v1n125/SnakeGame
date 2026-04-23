package org.example.ItemFactory;

import java.time.Instant;

import org.example.Item.Apple;
import org.example.Item.Item;

public class AppleFactory implements ItemFactory {
    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Apple(x, y, placedTime);
    }
}
