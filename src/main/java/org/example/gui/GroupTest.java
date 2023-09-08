package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;

import javax.swing.*;
import java.awt.*;

public class GroupTest {
    private static CustomGraph graph;
    public static void main(String[] args){
        graph = new CustomGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(7);
        graph.setEdgeLabelsMovable(false);
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false);
        graph.setCellsMovable(false);
        graph.setCellsSelectable(false);

        try {
            mxCell a = (mxCell)graph.insertVertex(parent, null, "test", 0,0,100,50);

            mxCell b = (mxCell)graph.insertVertex(parent, null, "test", 0,0,100,50);

            mxCell c1 = (mxCell)graph.insertVertex(parent, null, "test", 0,0,100,50);
            mxCell c2= (mxCell)graph.insertVertex(parent, null, "test", 0,0,100,50);

            mxCell d= (mxCell)graph.insertVertex(parent, null, "test2", 0,0,100,50);

            graph.insertEdge(parent, null, "", b,c1);

            graph.insertEdge(parent, null, "", b,c2);

            graph.insertEdge(b, null, "", c1, d);
            graph.insertEdge(b, null, "", c2, d);



            //System.out.println(b.getChildCount());

            graph.insertEdge(parent, null, "", a, b);
        } finally {
            layout.execute(parent);
            graph.getModel().endUpdate();
        }


        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setCenterZoom(true);
        graphComponent.requestFocus();



        graphComponent.addMouseWheelListener(new CustomMouseWheelListener(graphComponent));
        GraphFrame frame = new GraphFrame();
        frame.add(graphComponent);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
