package org.example.gui.canvasitems;

import org.example.gui.CanvasItem;

import java.awt.*;

public class Text extends CanvasItem {
    private String text;
    public Text(int x,int y, String text){
        this.x = x;
        this.y = y;
        this.text = text;
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawString(this.text, this.x, this.y);
    }
}
