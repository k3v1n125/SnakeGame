package com.zetcode.ItemFactory;

import java.time.Instant;

import com.zetcode.Item.Item;

public interface ItemFactory {
    Item create(int x, int y, Instant placedTime);
}
