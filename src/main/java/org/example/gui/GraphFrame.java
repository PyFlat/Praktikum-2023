package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.example.data.*;
import org.example.data.analysis.dephtmap;

import javax.swing.*;
import java.util.*;

public class GraphFrame extends JFrame {
    private static Object parent;
    private static mxGraph graph;
    private static ArrayList<ArrayList<Object>> vertices;
    private static ArrayList<ArrayList<NODE>> nodes;

    private static  ArrayList<ArrayList<String>> map;
    public static void visualize(ArrayList<ArrayList<String>> newmap) {
        map = newmap;
        graph = new mxGraph() {
            @Override
            public boolean isCellConnectable(Object cell) {
                return false;
            }
        };
        parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(7);
        graph.setEdgeLabelsMovable(false);
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false);
        //graph.setCellsMovable(false);
        graph.setCellsSelectable(false);
        vertices = new ArrayList<>();
        nodes = new ArrayList<>();
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
            link();
            /**/
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
    private static void link() {
        recursiveConnect(dephtmap.getMaxDephtStart(),null);
    }
    private static void link_once(Object from, Object to) {
        graph.insertEdge(parent,null,"",from,to);
    }
    private static void link_once(NODE from, NODE to) {
        //System.out.println("Connected " + from.getName() + " to " + to.getName());
        link_once(getVertex(from),getVertex(to));
    }
    private static Object getVertex(NODE n) {
        if (n == null) {return null;}
        for (int x = 0; x < map.size();x++) {
            for (int y=0;y<map.get(x).size(); y++) {
                if (map.get(x).get(y).equals(n.getName())) {
                    System.out.println(" " + x + " " + y);
                    return vertices.get(x).get(y);
                }
            }
        }
        return null;
    }
    private static void recursiveConnect(NODE current, NODE linkTo) {
        System.out.print("Connect_rec: " + current.getName());
        if (linkTo != null) {
            System.out.println(" to " + linkTo.getName());
        } else {System.out.println("");}
        if (current.type == NODETYPE.BASIC) {
            link_once(current,linkTo);
        }
        if (current.type == NODETYPE.SET) {
            ArrayList<Integer> children = ((SET) current).getChildNodes();
            for (Integer child : children) {
                link_once(current,(NODE) NODE.database.getElement(child));
                recursiveConnect((NODE) NODE.database.getElement(child),linkTo);
            }
        }
        if (current.type == NODETYPE.SUBPATH) {
            ArrayList<Integer> children = ((SUBPATH) current).getChildNodes();
            ArrayList<NODE> node_children = new ArrayList<>();
            children.forEach((e)->node_children.add((NODE) NODE.database.getElement(e)));
            Collections.reverse(node_children);
            link_once(current,node_children.get(0));
            for (int i=0;i<node_children.size();i++) {
                if (i == node_children.size()-1) {
                    recursiveConnect(node_children.get(i),linkTo);
                } else {
                    recursiveConnect(node_children.get(i),node_children.get(i+1));
                }
            }
        }
    }
}