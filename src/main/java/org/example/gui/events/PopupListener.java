package org.example.gui.events;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import org.example.gui.PropertyDisplayFrame;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class PopupListener implements MouseListener {
    private final mxGraphComponent graphComponent;
    private final HashMap<Object , Boolean> popupsOpen;
    private final HashMap<Object , JFrame> popups;
    public void discardPopup(Object cell) {
        if (this.popupsOpen.containsKey(cell)) {
            this.popupsOpen.put(cell,false);
            this.popups.put(cell, null);
        }
    }
    public PopupListener(mxGraphComponent graphComponent) {
        this.graphComponent = graphComponent;
        this.popupsOpen = new HashMap<>();
        this.popups = new HashMap<>();
    }
    public  void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    public void mouseClicked(MouseEvent event) {}
    private void openPopup(MouseEvent event) {
        Object cell = this.graphComponent.getCellAt(event.getX(),event.getY());
        if (cell == null) {return;}
        if (!(cell instanceof mxCell)) {return;}
        if (!((mxCell) cell).isVertex()) {return;}
        if (!event.isPopupTrigger()) {return;}
        if (!this.popupsOpen.containsKey(cell)) {this.popupsOpen.put(cell, false);}
        if (this.popupsOpen.get(cell)) {
            this.popups.get(cell).toFront();
            return;
        }
        this.showPopup(event,cell);
    }
    public void mousePressed(MouseEvent event) {this.openPopup(event);}
    public void mouseReleased(MouseEvent event) {this.openPopup(event);}
    private void showPopup(MouseEvent event, Object cell) {
        JFrame popupFrame = new PropertyDisplayFrame(event.getX(), event.getY(), cell);
        popupFrame.addWindowListener(new CustomWindowAdapter(this, cell));
        popupFrame.setVisible(true);
        this.popups.put(cell,popupFrame);
        this.popupsOpen.put(cell, true);
    }
}

