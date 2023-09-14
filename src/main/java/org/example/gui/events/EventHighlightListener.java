package org.example.gui.events;

import java.awt.event.MouseEvent;

public abstract class EventHighlightListener implements highlightListener {
    public void highlightStart(MouseEvent event , Object cell) {
        this.highlightStart(cell);
    }
    public void highlightStop(MouseEvent event , Object cell) {
        this.highlightStop(cell);
    }
    @Override
    public void highlightStart(Object cell) {}
    @Override
    public void highlightStop(Object cell) {}
}
