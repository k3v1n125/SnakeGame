package org.example.ItemFactory;

import java.time.Instant;

import org.example.Item.Item;

public interface ItemFactory {
    Item create(int x, int y, Instant placedTime);
}
