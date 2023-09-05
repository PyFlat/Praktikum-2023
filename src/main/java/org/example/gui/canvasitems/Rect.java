package org.example.gui.canvasitems;

import org.example.gui.CanvasItem;

import java.awt.*;

public class Rect extends CanvasItem {
    private int arcWidth = 0;
    private int arcHeight = 0;
    public Rect(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.arcWidth = 0;
        this.arcHeight = 0;
    }
    public Rect(int x,int y,int width,int height, int arcWidth, int arcHeight){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }
    @Override
    public void paint(Graphics2D g) {
        g.drawRoundRect(this.x, this.y, this.width, this.height, this.arcWidth, this.arcHeight);
    }
}
