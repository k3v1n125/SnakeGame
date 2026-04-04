package com.zetcode;

import java.awt.Graphics;
import java.awt.Image;
import java.time.Instant;

public class Apple implements Item {
    private Image image;
    private int x;
    private int y;
    private Instant applePlacedTime;

    public Apple(Image image, int x, int y, Instant applePlacedTime) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.applePlacedTime = applePlacedTime;
    }
    
    @Override
    public void draw(Graphics g, Board board) {
        g.drawImage(image, x, y, board);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Instant getPlacedTime() {
        return applePlacedTime;
    }
}
