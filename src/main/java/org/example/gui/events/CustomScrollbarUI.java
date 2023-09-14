package org.example.gui.events;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollbarUI extends BasicScrollBarUI {

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(117, 117, 117); // Customize thumb color
        this.trackColor = new Color(28, 28, 28); // Customize track color
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return this.createEmptyButton(); // Customize the decrease button
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return this.createEmptyButton(); // Customize the increase button
    }

    private JButton createEmptyButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}