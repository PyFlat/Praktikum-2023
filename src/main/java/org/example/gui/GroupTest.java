package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import org.example.gui.events.PopupListener;
import org.example.gui.events.highlightListener;
import org.example.gui.events.mouseEventProcessor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
@Deprecated
public class GroupTest {
    public static void main(String[] args) throws IOException {
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
        mouseEventProcessor p = new mouseEventProcessor(new highlightListener() {
            @Override
            public void highlightStart(Object cell) {
                for (Object edges : graph.getEdges(cell)){
                    mxCell edge = (mxCell) edges;
                    String newStyle = mxUtils.setStyle(edge.getStyle(), mxConstants.STYLE_STROKEWIDTH, "2");
                    newStyle = mxUtils.setStyle(newStyle, mxConstants.STYLE_STROKECOLOR, "#FF0000");
                    edge.setStyle(newStyle);
                    graph.refresh();
                }

            }

            @Override
            public void highlightStop(Object cell) {
                for (Object edges : graph.getEdges(cell)){
                    mxCell edge = (mxCell) edges;
                    String newStyle = mxUtils.setStyle(edge.getStyle(), mxConstants.STYLE_STROKEWIDTH, "1");
                    newStyle = mxUtils.setStyle(newStyle, mxConstants.STYLE_STROKECOLOR, "#6482b9");
                    edge.setStyle(newStyle);
                    graph.refresh();
                }
            }
        }, graphComponent);
        graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                p.processEvent(e);
            }
        });

        graphComponent.getGraphControl().addMouseListener(new PopupListener());

        graphComponent.addMouseWheelListener(new CustomMouseWheelListener(graphComponent));
        GraphFrame frame = new GraphFrame();

        //BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
        //ImageIO.write(image, "PNG", new File("graph.png"));

        frame.add(graphComponent);
        frame.setTitle("Buggy s**t");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
