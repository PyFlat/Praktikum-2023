package org.example.gui.events;

import java.awt.event.MouseEvent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

public class mouseEventProcessor {
    private Object cell_current;
    private final mxGraphComponent g;

    private final EventHighlightListener l;

    public mouseEventProcessor(EventHighlightListener listener, mxGraphComponent component) {
        this.g = component;
        this.l = listener;
    }
    public void processEvent(MouseEvent event) {
        Object cell = g.getCellAt(event.getX(),event.getY());
        if (!(cell instanceof mxCell)) {cell = null;}
        if (cell != null && !((mxCell) cell).isVertex()) {cell = null;}
        if (cell_current == null) {
            if (cell != null) {
                cell_current = cell;
                l.highlightStart(event, cell_current);
            }
        } else {
            if (cell != null && cell != cell_current) {
                l.highlightStop(event, cell_current);
                cell_current = cell;
                l.highlightStart(event, cell_current);
            } else if (cell != cell_current){
                l.highlightStop(event, cell_current);
                cell_current = null;
            }
        }

    }
}
