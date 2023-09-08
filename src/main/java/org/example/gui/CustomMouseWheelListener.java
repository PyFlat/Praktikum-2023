package org.example.gui;

import com.mxgraph.swing.mxGraphComponent;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CustomMouseWheelListener implements MouseWheelListener {
    private mxGraphComponent graphComponent = null;
    public CustomMouseWheelListener(mxGraphComponent graphComponent){
        this.graphComponent = graphComponent;
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()){
            if (e.getWheelRotation() > 0){
                graphComponent.zoomOut();

            }
            else {
                graphComponent.zoomIn();

            }
        }
    }
}
