package org.example.gui.events;

import java.awt.event.MouseEvent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

public class mouseEventProcessor {
    private Object cell_current;
    private mxGraphComponent g;

    private highlightListener l;

    public mouseEventProcessor(highlightListener listener, mxGraphComponent component) {
        this.g = component;
        this.l = listener;
    }
    public void processEvent(MouseEvent event) {
        Object cell = g.getCellAt(event.getX(),event.getY());
        if (!(cell instanceof mxCell)) {cell = null;}
        if (!((mxCell) cell).isVertex()) {cell = null;}
        if (cell == null && cell_current != null) {
            stopHighlight();
            cell_current = null;
        }
        if (cell != null && cell != cell_current) {
            stopHighlight();
            cell_current = cell;
            startHighlight();
        }

    }
    public void startHighlight() {
        l.highlightStart(cell_current);
    }
    public void stopHighlight() {
        l.highlightStop(cell_current);
    }
}
