package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.example.data.*;

import javax.swing.*;
import java.util.ArrayList;

public class GraphFrame extends JFrame {
    public static void visualize(ArrayList<ArrayList<String>> map) {
        mxGraph graph = new mxGraph() {
            @Override
            public boolean isCellConnectable(Object cell) {
                return false;
            }
        };
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(7);
        graph.setEdgeLabelsMovable(false);
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false);
        //graph.setCellsMovable(false);
        graph.setCellsSelectable(false);
        ArrayList<ArrayList<Object>> vertices = new ArrayList<ArrayList<Object>>();
        ArrayList<ArrayList<NODE>> nodes = new ArrayList<ArrayList<NODE>>();
        try {
            for (int x=0;x<map.size();x++) {
                vertices.add(new ArrayList<Object>());
                nodes.add(new ArrayList<NODE>());
                for (int y=0;y<map.get(x).size();y++) {
                    vertices.get(x).add(graph.insertVertex(parent, null, map.get(x).get(y),x*100+(x-1)*100+100,y*50+(y-1)*30+30,100,50));
                    NODE_LIKE n = NODE.database.getElementByKey(map.get(x).get(y));
                    nodes.get(x).add((NODE) n);
                }
            }
            for (int x=0;x<map.size();x++) {
                System.out.println(x);
                for (int y=0;y<map.get(x).size();y++) {
                    ArrayList<Integer> children = new ArrayList<Integer>();
                    if (nodes.get(x).get(y).type== NODETYPE.BASIC) {continue;}
                    else if (nodes.get(x).get(y).type == NODETYPE.SET) {children = ((SET) nodes.get(x).get(y)).getChildNodes();}
                    else if (nodes.get(x).get(y).type == NODETYPE.SUBPATH) {children = ((SUBPATH) nodes.get(x).get(y)).getChildNodes();}
                    else {continue;}
                    for (int c = 0; c < children.size(); c++) {
                        String name = NODE.database.getKeyByIndex(children.get(c));
                        for (int x1=0;x1<map.size();x1++) {
                            for (int y1=0;y1<map.get(x1).size();y1++) {
                                if (map.get(x1).get(y1).equals(name)) {
                                    graph.insertEdge(parent,null,"",vertices.get(x).get(y),vertices.get(x1).get(y1));
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            layout.execute(parent);
            graph.getModel().endUpdate();
        }
        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        GraphFrame frame = new GraphFrame();
        frame.add(graphComponent);
        frame.pack();
        frame.setVisible(true);
    }
}