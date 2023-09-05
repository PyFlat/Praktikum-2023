package org.example.gui;

import javax.swing.*;
import java.awt.*;

public class RECT {

    public RECT(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Rectangle(x, y, width, height));
    }
}