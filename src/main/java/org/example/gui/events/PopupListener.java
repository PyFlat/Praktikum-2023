package org.example.gui.events;

import org.example.gui.PropertyDisplayFrame;

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
        JFrame popupFrame = new PropertyDisplayFrame();
        popupFrame.setResizable(false);
        popupFrame.setLocation(e.getX(), e.getY());

        popupFrame.setVisible(true);
    }
}

