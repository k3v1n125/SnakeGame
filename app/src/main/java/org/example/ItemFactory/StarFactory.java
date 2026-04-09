package org.example.ItemFactory;

import java.awt.Image;
import java.time.Instant;

import org.example.Item.Star;
import org.example.Item.Item;

public class StarFactory implements ItemFactory {
    private final Image image;

    public StarFactory(Image image) {
        this.image = image;
    }

    @Override
    public Item create(int x, int y, Instant placedTime) {
        return new Star(image, x, y, placedTime);
    }
}
