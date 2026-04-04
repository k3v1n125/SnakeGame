package com.zetcode.Item;

import java.awt.Graphics;
import java.time.Instant;

import com.zetcode.Board;

public interface Item {
    public void draw(Graphics g, Board board);

    public int getX();
    public int getY();
    public Instant getPlacedTime();
}
