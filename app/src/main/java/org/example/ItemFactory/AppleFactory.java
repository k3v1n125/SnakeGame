package org.example.ItemFactory;

import java.awt.Image;
import java.time.Instant;

import org.example.Item.Apple;
import org.example.Item.Item;

public class AppleFactory implements ItemFactory {
    private final Image image;

    public AppleFactory(Image image) {
        this.image = image;
    }

    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Apple(image, x, y, placedTime);
    }
}
