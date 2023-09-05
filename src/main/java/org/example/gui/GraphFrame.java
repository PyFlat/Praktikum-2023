package org.example.gui;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.swing.handler.mxConnectionHandler;
import org.example.data.NODE;

import javax.swing.*;
import java.util.ArrayList;

public class GraphFrame extends JFrame {
    public static void visualize(ArrayList<ArrayList<String>> map) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        graph.setEdgeLabelsMovable(false);
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false);
        graph.setCellsMovable(false);
        graph.setCellsSelectable(false);

        try {
            for (int x=0;x<map.size();x++) {
                for (int y=0;y<map.get(x).size();y++) {
                    graph.insertVertex(parent, null, map.get(x).get(y),x*100+(x-1)*100+100,y*50+(y-1)*30+30,100,50);
                }
            }/*
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,30);
            Object v2 = graph.insertVertex(parent, null, "World!", 24, 150,80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);*/
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