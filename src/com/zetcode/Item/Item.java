package com.zetcode.Item;

import java.awt.Graphics;
import java.awt.Image;
import java.time.Instant;

import com.zetcode.Board;

public abstract class Item {
    private final int DOT_SIZE = 20;
    private Image image;
    private int x;
    private int y;
    private Instant placedTime;
    
    public Item(Image image, int x, int y, Instant placedTime) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.placedTime = placedTime;
    }

    public void draw(Graphics g, Board board) {
        g.drawImage(image, x, y, DOT_SIZE, DOT_SIZE, board);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Instant getPlacedTime() {
        return placedTime;
    }

    public abstract void locateItem(Board board);
    public abstract void itemEffect(Board board);
}
