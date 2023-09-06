package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private static void link() {
        ArrayList<Integer> a = new ArrayList<>();
        //a.add(map.size()-1);
        recurseiveConnect2(dephtmap.getMaxDephtStart(),null,0,a);
    }
    private static void link_once(Object from, Object to) {
        graph.insertEdge(parent,null,"",from,to);
    }
    private static void link_once(NODE from, NODE to, int cidx, int delta) {
        //System.out.println("Connected " + from.getName() + " to " + to.getName());
        link_once(getVertex(from, cidx),getVertex(to, cidx+delta));
    }
    private static Object getVertex(NODE n, int cidx) {
        if (n == null) {return null;}
        for (int x = 0; x < map.size();x++) {
            for (int y=0;y<map.get(x).size(); y++) {
                if (map.get(x).get(y).equals(n.getName()) && x == cidx) {
                    //System.out.println(" " + x + " " + y);
                    return vertices.get(x).get(y);
                }
            }
        }
        return null;
    }
    private static int recurseiveConnect2(NODE current, NODE linkTo, int cidx, ArrayList<Integer> destinations) {
        System.out.println("Called " +cidx+ " " + current.getName());
        //System.out.println(Arrays.toString(destinations.toArray()));
        if (current.type == NODETYPE.BASIC) {
            for (int destination: destinations) {
                if (getVertex(linkTo, destination)!=null) {
                    link_once(current, linkTo, cidx, destination-cidx);
                    return cidx;
                }
            }
            link_once(current, linkTo, cidx, 1);
            return cidx;
        }
        if (current.type == NODETYPE.SUBPATH) {
            System.out.println("Entered subpath " + current.getName());
            ArrayList<Integer> children = ((SUBPATH) current).getChildNodes();
            ArrayList<NODE> node_children = new ArrayList<>();
            children.forEach((e)->node_children.add((NODE) NODE.database.getElement(e)));
            Collections.reverse(node_children);
            link_once(current,node_children.get(0),cidx,1);
            for (int i=0;i<node_children.size();i++) {
                if (i == node_children.size()-1) {
                    cidx = recurseiveConnect2(node_children.get(i),linkTo,cidx+1, destinations);
                    return cidx;
                    //System.out.println("Path "+current.getName() +" failed to end");
                } else {
                    cidx = recurseiveConnect2(node_children.get(i),node_children.get(i+1),cidx+1, destinations);
                }
            }
        }
        if (current.type == NODETYPE.SET) {
            System.out.println("Added set " + current.getName());
            destinations.add(cidx+((SET) current).getLength());
            ArrayList<Integer> children = ((SET) current).getChildNodes();
            Collections.reverse(children);
            //System.out.println("Children of " + current.getName() + ": " + Arrays.toString(children.toArray()));
            for (Integer child : children) {
                link_once(current, (NODE) NODE.database.getElement(child), cidx,1);
                recurseiveConnect2((NODE) NODE.database.getElement(child),linkTo, cidx + 1, destinations);
            }
            destinations.remove(destinations.size()-1);
            System.out.println("Left set " + current.getName());
            cidx += ((SET) current).getLength()-1;
            return cidx;
        }
        return 0;
    }
    private static int recursiveConnect(NODE current, NODE linkTo, int cidx, int indexTo) {
        System.out.print("Connect_rec: " + current.getName() + " " + cidx);
        if (linkTo != null) {
            System.out.println(" to " + linkTo.getName());
        } else {System.out.println("");}
        if (current.type == NODETYPE.BASIC) {
            if (indexTo == -1) {
                link_once(current,linkTo,cidx,1);
                return cidx;
            }
            link_once(current,linkTo,cidx,indexTo-cidx);
            return cidx;
        }
        if (current.type == NODETYPE.SET) {
            ArrayList<Integer> children = ((SET) current).getChildNodes();
            for (Integer child : children) {
                link_once(current,(NODE) NODE.database.getElement(child),cidx,1);
                recursiveConnect((NODE) NODE.database.getElement(child),linkTo, cidx + 1, cidx+((SET) current).getLength());
            }
            cidx += ((SET) current).getLength()-1;
            return cidx;
        }
        if (current.type == NODETYPE.SUBPATH) {
            ArrayList<Integer> children = ((SUBPATH) current).getChildNodes();
            ArrayList<NODE> node_children = new ArrayList<>();
            children.forEach((e)->node_children.add((NODE) NODE.database.getElement(e)));
            Collections.reverse(node_children);
            link_once(current,node_children.get(0),cidx,1);
            for (int i=0;i<node_children.size();i++) {
                if (i == node_children.size()-1) {
                    if (indexTo == -1) { recursiveConnect(node_children.get(i),linkTo,cidx+1, cidx+1);}
                    else {
                        cidx = recursiveConnect(node_children.get(i), linkTo, cidx + 1, indexTo);
                    }
                } else {
                    cidx = recursiveConnect(node_children.get(i),node_children.get(i+1),cidx+1, -1);
                }
            }
        }
        return 0;
    }
}