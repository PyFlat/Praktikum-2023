package org.example.gui;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.swing.handler.mxConnectionHandler;

import javax.swing.*;

public class GraphFrame extends JFrame {
    public static void main(String[] args) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        graph.setEdgeLabelsMovable(false);
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false);
        graph.setCellsMovable(false);
        graph.setCellsSelectable(false);

        try {
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,30);
            Object v2 = graph.insertVertex(parent, null, "World!", 24, 150,80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        } finally {
            graph.getModel().endUpdate();
        }
        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        GraphFrame frame = new GraphFrame();
        frame.add(graphComponent);
        frame.pack();
        frame.setVisible(true);
    }
}