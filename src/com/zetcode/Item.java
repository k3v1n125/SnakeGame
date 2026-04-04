package com.zetcode;

import java.awt.Graphics;
import java.time.Instant;

public interface Item {
    public void draw(Graphics g, Board board);

    public int getX();
    public int getY();
    public Instant getPlacedTime();
}
