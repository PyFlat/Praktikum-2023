package org.example.gui.events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PopupListener implements MouseListener {
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showPopup(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showPopup(e);
        }
    }
    private void showPopup(MouseEvent e) {
        JFrame popupFrame = new JFrame();
        popupFrame.setSize(200, 200);
        popupFrame.setResizable(false);
        popupFrame.setLocation(e.getX(), e.getY());
        popupFrame.setLayout(new GridLayout(0, 1));

        // Add your strings to the popup frame
        popupFrame.add(new JLabel("String 1"));
        popupFrame.add(new JLabel("String 2"));
        popupFrame.add(new JLabel("String 3"));

        popupFrame.setVisible(true);
    }
}

