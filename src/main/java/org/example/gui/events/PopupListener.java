package org.example.gui.events;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import org.example.gui.GraphFrame;
import org.example.gui.PropertyDisplayFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class PopupListener implements MouseListener {
    private mxGraphComponent g;
    private HashMap<Object , Boolean> popupsOpen;
    public void discardPopup(Object cell) {
        if (popupsOpen.containsKey(cell)) {
            popupsOpen.put(cell,false);
        }
    }
    public PopupListener(mxGraphComponent g) {
        this.g = g;
        popupsOpen = new HashMap<>();
    }
    public  void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {

    }
    private void openPopup(MouseEvent event) {
        Object cell = g.getCellAt(event.getX(),event.getY());
        if (cell == null) {return;}
        if (!(cell instanceof mxCell)) {return;}
        if (!((mxCell) cell).isVertex()) {return;}
        if (!event.isPopupTrigger()) {return;}
        if (!popupsOpen.containsKey(cell)) {popupsOpen.put(cell, false);}
        if (popupsOpen.get(cell)) {return;}
        showPopup(event,cell);
        popupsOpen.put(cell, true);
    }
    public void mousePressed(MouseEvent event) {openPopup(event);}
    public void mouseReleased(MouseEvent event) {openPopup(event);}
    private void showPopup(MouseEvent e, Object cell) {
        JFrame popupFrame = new PropertyDisplayFrame(e.getX(), e.getY(), cell);
        popupFrame.addWindowListener(new CustomWindowAdapter(this, cell));
        popupFrame.setVisible(true);
    }
}

