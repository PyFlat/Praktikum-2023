package org.example.gui;

import java.awt.*;

public abstract class CanvasItem {
    protected int x = 0;
    protected int y = 0;
    protected int width = 0;
    protected int height = 0;
    public abstract void paint(Graphics2D g);
}
