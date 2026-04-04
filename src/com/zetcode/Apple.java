package com.zetcode;

import java.awt.Graphics;
import java.awt.Image;

public class Apple implements Item {
    private Image image;
    private int x;
    private int y;

    public Apple(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void draw(Graphics g, Board board) {
        g.drawImage(image, x, y, board);
    }
}
