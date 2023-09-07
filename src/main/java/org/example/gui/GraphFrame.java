package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
//import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.example.data.*;
import org.example.data.analysis.depthMap;

import javax.swing.*;
import java.util.*;

public class GraphFrame extends JFrame {
    private static Object parent;
    private static mxGraph graph;
    private static ArrayList<ArrayList<Object>> vertices;

    private static  ArrayList<ArrayList<String>> map;

    private static ArrayList<ArrayList<Node_abstract>> nodes;


    private static ArrayList<ArrayList<Integer>> acceptMultipleInputs;
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
        acceptMultipleInputs = new ArrayList<>();
        try {
            for (int x=0;x<map.size();x++) {
                vertices.add(new ArrayList<>());
                nodes.add(new ArrayList<>());
                acceptMultipleInputs.add(new ArrayList<>());
                for (int y=0;y<map.get(x).size();y++) {
                    vertices.get(x).add(graph.insertVertex(parent, null, map.get(x).get(y),x*100+(x-1)*100+100,y*50+(y-1)*30+30,100,50));
                    Node_abstract n = Database.t.getElementByKey(map.get(x).get(y));
                    nodes.get(x).add((Node_abstract) n);
                    acceptMultipleInputs.get(x).add(1);
                }
            }
            link();
            /**/
        } finally {
            layout.execute(parent);
            graph.getModel().endUpdate();
        }
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setZoomFactor(1);
        graphComponent.zoomOut();
        GraphFrame frame = new GraphFrame();
        frame.add(graphComponent);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private static void link() {
        ArrayList<Integer> a = new ArrayList<>();
        //a.add(map.size()-1);
        recurseiveConnect2(depthMap.getMaxDepthStart(),null,0,a, false);
    }
    private static void link_once(Object from, Object to) {
        graph.insertEdge(parent,null,"",from,to);
    }
    private static void link_once(Node_abstract from, Node_abstract to, int cidx, int delta) {
        //System.out.println("Connected " + from.getName() + " to " + to.getName());
        link_once(getVertex(from, cidx,false),getVertex(to, cidx+delta, true));
    }
    private static Object getVertex(Node_abstract n, int cidx, boolean to) {
        if (n == null) {return null;}
        for (int x = 0; x < map.size();x++) {
            for (int y=0;y<map.get(x).size(); y++) {
                //System.out.println(""+ x + " "+ y + " " + graph.getIncomingEdges(vertices.get(x).get(y)).length);
                if (map.get(x).get(y).equals(n.getName()) && x == cidx) { //Muss gelten für alles
                    if (to) {
                        if (graph.getIncomingEdges(vertices.get(x).get(y)).length < acceptMultipleInputs.get(x).get(y)) {
                            //System.out.println("Created " + x + " " + y);
                            return vertices.get(x).get(y);
                        }
                    } else {
                        mxCell vertex = (mxCell) vertices.get(x).get(y);
                        Node_abstract node = nodes.get(x).get(y);
                        int limit = 1;
                        if (node.type == NODETYPE.SET) {
                            limit = ((advancedNode)node).getChildNodes().size();
                        }
                        System.out.println("Node: "+node.getName());
                        if (graph.getOutgoingEdges(vertex).length< limit) {
                            return vertices.get(x).get(y);
                        }

                    }
                }

            }
        }
        return null;
    }
    private static int recurseiveConnect2(Node_abstract current, Node_abstract linkTo, int cidx, ArrayList<Integer> destinations, boolean destinationOverride) {
        System.out.println("Called " +cidx+ " " + current.getName());
        //System.out.println(Arrays.toString(destinations.toArray()));
        if (current.type == NODETYPE.BASIC) {
            if (destinationOverride) {
                link_once(current, linkTo, cidx, 1);
                return cidx;
            }
            if (destinations.size()>0) {
                if (getVertex(linkTo, destinations.get(destinations.size()-1), true) != null) {
                    link_once(current, linkTo, cidx, destinations.get(destinations.size()-1)-cidx);
                    return cidx;
                }
            }

            for (int destination: destinations) {
                if (getVertex(linkTo, destination, true)!=null) {
                    link_once(current, linkTo, cidx, destination-cidx);
                    return cidx;
                }
            }
            link_once(current, linkTo, cidx, 1);
            return cidx;
        }
        if (current.type == NODETYPE.SUBPATH) {
            System.out.println("Entered subpath " + current.getName());
            ArrayList<Integer> children = ((advancedNode) current).getChildNodes();
            ArrayList<Node_abstract> node_children = new ArrayList<>();
            children.forEach((e)->node_children.add((Node_abstract) Database.t.getElement(e)));
            Collections.reverse(node_children);
            link_once(current,node_children.get(0),cidx,1);
            for (int i=0;i<node_children.size();i++) {
                if (i == node_children.size()-1) {
                    cidx = recurseiveConnect2(node_children.get(i),linkTo,cidx+1, destinations, false);
                    return cidx;
                    //System.out.println("Path "+current.getName() +" failed to end");
                } else {
                    cidx = recurseiveConnect2(node_children.get(i),node_children.get(i+1),cidx+1, destinations, true);
                }
            }
        }
        if (current.type == NODETYPE.SET) {
            System.out.println("Added set " + current.getName());
            destinations.add(cidx+ current.getLength());
            ArrayList<Integer> children = ((advancedNode) current).getChildNodes();
            Collections.reverse(children);
            //System.out.println("Children of " + current.getName() + ": " + Arrays.toString(children.toArray()));
            for (Integer child : children) {
                link_once(current, Database.t.getElement(child), cidx,1);
                recurseiveConnect2(Database.t.getElement(child),linkTo, cidx + 1, destinations, false);
            }
            destinations.remove(destinations.size()-1);
            System.out.println("Left set " + current.getName());
            cidx += current.getLength()-1;
            return cidx;
        }
        return 0;
    }

}