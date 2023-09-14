package org.example.gui;

import com.mxgraph.swing.mxGraphComponent;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CustomMouseWheelListener implements MouseWheelListener {
    private final mxGraphComponent graphComponent;
    public CustomMouseWheelListener(mxGraphComponent graphComponent){this.graphComponent=graphComponent;}
    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        if (event.isControlDown() && event.getWheelRotation() < 0 && this.graphComponent.getGraph().getView().getScale() < 6) {
            this.graphComponent.zoomIn();
        } else if (event.isControlDown() && event.getWheelRotation() > 0 && this.graphComponent.getGraph().getView().getScale() > 0.25) {
            this.graphComponent.zoomOut();
        }
    }
}
