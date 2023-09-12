package org.example.gui.events;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomWindowAdapter extends WindowAdapter {
    private Object target;
    private PopupListener parent;
    public CustomWindowAdapter(PopupListener p, Object targetCell) {
        this.target = targetCell;
        this.parent = p;
    }
    @Override
    public void windowClosing(WindowEvent e) {
        this.parent.discardPopup(target);
    }
}
