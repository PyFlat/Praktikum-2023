package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;
import java.util.Map;

public class GroupTest {
    public static void main(String[] args){
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setDisableEdgeStyle(false);
        layout.setOrientation(7);
        graph.setEdgeLabelsMovable(false);
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false);
        graph.setCellsMovable(false);
        graph.setCellsSelectable(false);


        //graph.set("rounded=1;fillColor=#FFFFFF;fontColor=#000000");
        try {
            mxCell a = (mxCell) graph.insertVertex(parent, null, "test", 0,0,100,50, "fillColor=#FFFFFF");

            mxCell b = (mxCell) graph.insertVertex(parent, null, "xyz", 0,0,100,50, "rounded=1");

            mxCell c1 = (mxCell) graph.insertVertex(parent, null, "test", 0,0,100,50);
            mxCell c2= (mxCell) graph.insertVertex(parent, null, "test", 0,0,100,50);

            mxCell d= (mxCell) graph.insertVertex(parent, null, "test2", 0,0,100,50);

            graph.insertEdge(parent, null, "", b,c1);

            graph.insertEdge(parent, null, "", b,c2);

            graph.insertEdge(b, null, "", c1, d);
            graph.insertEdge(b, null, "", c2, d);

            graph.insertEdge(parent, null, "", a, b);

            Map<String, Object> edgeStyle;
            edgeStyle = graph.getStylesheet().getDefaultEdgeStyle();
            edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_BLOCK);


        } finally {
            layout.execute(parent);
            graph.getModel().endUpdate();
        }


        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setCenterZoom(true);
        graphComponent.requestFocus();

        graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null && graph.getModel().isVertex(cell)){
                    System.out.println("Mouse Entered cell: " + Arrays.toString(graph.getEdges(cell)));
                }
            }
        });



        graphComponent.addMouseWheelListener(new CustomMouseWheelListener(graphComponent));
        GraphFrame frame = new GraphFrame();
        frame.add(graphComponent);
        frame.setTitle("Buggy s**t");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
