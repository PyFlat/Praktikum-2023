package org.example.gui;

import com.mxgraph.swing.mxGraphComponent;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CustomMouseWheelListener implements MouseWheelListener {
    private mxGraphComponent graphComponent = null;
    public CustomMouseWheelListener(mxGraphComponent graphComponent){this.graphComponent=graphComponent;}
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown() && e.getWheelRotation() < 0 && graphComponent.getGraph().getView().getScale() < 6) {
            System.out.println(graphComponent.getGraph().getView().getScale());
            graphComponent.zoomIn();
        } else if (e.isControlDown() && e.getWheelRotation() > 0 && graphComponent.getGraph().getView().getScale() > 0.25) {
            graphComponent.zoomOut();
        }

    }
}
