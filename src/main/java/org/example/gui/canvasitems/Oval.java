package org.example.gui.canvasitems;

import org.example.gui.CanvasItem;

import java.awt.*;

public class Oval extends CanvasItem {
    public Oval(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawOval(this.x, this.y, this.width, this.height);
    }
}
