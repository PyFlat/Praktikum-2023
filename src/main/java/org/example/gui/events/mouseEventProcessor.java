package org.example.gui.events;

import java.awt.event.MouseEvent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

public class mouseEventProcessor {
    private Object cell_current;
    private final mxGraphComponent graphComponent;

    private final EventHighlightListener highlightListener;

    public mouseEventProcessor(EventHighlightListener listener, mxGraphComponent component) {
        this.graphComponent = component;
        this.highlightListener = listener;
    }
    public void processEvent(MouseEvent event) {
        Object cell = this.graphComponent.getCellAt(event.getX(),event.getY());
        if (!(cell instanceof mxCell)) {cell = null;}
        if (cell != null && !((mxCell) cell).isVertex()) {cell = null;}
        if (this.cell_current == null) {
            if (cell != null) {
                this.cell_current = cell;
                this.highlightListener.highlightStart(event, this.cell_current);
            }
            return;
        }
        if (cell != null && cell != this.cell_current) {
            this.highlightListener.highlightStop(event, this.cell_current);
            this.cell_current = cell;
            this.highlightListener.highlightStart(event, this.cell_current);
        } else if (cell != this.cell_current){
            this.highlightListener.highlightStop(event, this.cell_current);
            this.cell_current = null;
        }
    }
}
