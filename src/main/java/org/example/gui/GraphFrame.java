package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
//import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import org.example.data.*;
import org.example.data.analysis.depthMap;
import org.example.gui.events.highlightListener;
import org.example.gui.events.mouseEventProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GraphFrame extends JFrame {
    private static Object parent;
    private static CustomGraph graph;
    private static ArrayList<ArrayList<Object>> vertices;

    private static  ArrayList<ArrayList<String>> map;

    private static ArrayList<ArrayList<Node_abstract>> nodes;

    private static ArrayList<ArrayList<Node_abstract>> parents;
    private static ArrayList<ArrayList<Integer>> acceptMultipleInputs;

    private static ArrayList<ArrayList<Node_abstract>> goals;

    public static void visualize(ArrayList<ArrayList<String>> newmap) {
        //System.out.println(Arrays.toString(newmap.toArray()));
        map = newmap;
        graph = new CustomGraph();
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
        parents = new ArrayList<>();
        try {
            System.out.println("Initialized precalc");
            for (int x=0;x<map.size();x++) {
                vertices.add(new ArrayList<>());
                nodes.add(new ArrayList<>());
                acceptMultipleInputs.add(new ArrayList<>());
                parents.add(new ArrayList<>());
                for (int y=0;y<map.get(x).size();y++) {


                    Node_abstract n = Database.t.getElementByKey(map.get(x).get(y));
                    nodes.get(x).add((Node_abstract) n);
                    NODETYPE type = ((Node_abstract) n).type;
                    String style = "fillColor=#3c3c3c;strokeColor=#ccd0d9;fontColor=#ffffff;fontSize=12;";
                    if (type == NODETYPE.SET) {style += "rounded=1;arcSize:10";}
                    if (type == NODETYPE.SUBPATH) {style += "rounded=1;arcSize:25";}
                    if (type == NODETYPE.BASIC) {style += "";}
                    acceptMultipleInputs.get(x).add(1);
                    parents.get(x).add(null);
                    mxCell vertex = (mxCell) graph.insertVertex(parent, null, map.get(x).get(y),x*100+(x-1)*100+100,y*50+(y-1)*30+30,100,50, style);
                    vertices.get(x).add(vertex);
                }
            }
            graph.map = map;
            graph.nodes = nodes;
            graph.vertices = vertices;
            System.out.println("Finished precalc, started linking");
            link();
            /**/
        } finally {
            layout.execute(parent);
            graph.getModel().endUpdate();
        }
        graph.addListener(mxEvent.FOLD_CELLS, (sender, evt) -> {
            try {
                layout.execute(graph.getDefaultParent());
            } catch (StackOverflowError e) {System.out.println("WARNING! LAYOUT DISABLED BECAUSE OF OVERFLOW! REDUCE COMPLEXITY!");}
        });
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
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
                    newStyle = mxUtils.setStyle(newStyle, mxConstants.STYLE_STROKECOLOR, "#ccd0d9");
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

        graphComponent.addMouseWheelListener(new CustomMouseWheelListener(graphComponent));
        graphComponent.getViewport().setBackground(new Color(30, 30, 30));
        //graphComponent.setZoomFactor(1);
        //graphComponent.zoomOut();
        GraphFrame frame = new GraphFrame();
        frame.getContentPane().add(graphComponent);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void fillGoals(Node_abstract current, Node_abstract to) {

    }
    private static void link() {
        ArrayList<Integer> a = new ArrayList<>();
        //a.add(map.size()-1);
        System.out.println("Looking for parents");
        findParents(depthMap.getMaxDepthStart(), null, 0,0);
        graph.parents = parents;
        System.out.println("Found parents, start input calc");
        for (int x = 0; x < map.size();x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                acceptMultipleInputs.get(x).set(y,findMaxConnections(nodes.get(x).get(y),x,y));
                //acceptMultipleInputs.get(x).set(y,10000);
            }
        }
        System.out.println("Finished input calc, started collect");
        //System.out.println(Arrays.toString(acceptMultipleInputs.toArray()));
        recurseiveConnect2(depthMap.getMaxDepthStart(),null,0,a, false);

        System.out.println("Finished connect");
    }
    private static void link_once(Object from, Object to) {
        graph.insertEdge(parent,null,"",from,to, "strokeColor=#ccd0d9;");
    }
    private static void link_once(Node_abstract from, Node_abstract to, int cidx, int delta) {
        //System.out.println("Connected " + from.getName() + " to " + to.getName());
        link_once(getVertex(from, cidx,false),getVertex(to, cidx+delta, true));
    }
    private static Object getVertex(Node_abstract n, int cidx, boolean to) {
        if (n == null) {return null;}
        //for (int x = 0; x < map.size();x++) {
            for (int y=0;y<map.get(cidx).size(); y++) {
                //System.out.println(""+ x + " "+ y + " " + graph.getIncomingEdges(vertices.get(x).get(y)).length);
                if (map.get(cidx).get(y).equals(n.getName())) { //Muss gelten für alles
                    if (to) {
                        if (graph.getIncomingEdges(vertices.get(cidx).get(y)).length < acceptMultipleInputs.get(cidx).get(y)) {
                            //System.out.println("Created " + x + " " + y);
                            return vertices.get(cidx).get(y);
                        }
                    } else {
                        mxCell vertex = (mxCell) vertices.get(cidx).get(y);
                        Node_abstract node = nodes.get(cidx).get(y);
                        int limit = 1;
                        if (node.type == NODETYPE.SET) {
                            limit = ((advancedNode)node).getChildNodes().size();
                        }
                        //System.out.println("Node: "+node.getName());
                        if (graph.getOutgoingEdges(vertex).length< limit) {
                            return vertices.get(cidx).get(y);
                        }

                    }
                }

            }
        //}
        return null;
    }
    private static int findMaxConnections(Node_abstract current, int x, int y) {
        if (parents.get(x).get(y) == null) {return 1;}
        ArrayList<Integer> children = ((advancedNode) parents.get(x).get(y)).getChildNodes();
        ArrayList<Node_abstract> nh = new ArrayList<>();
        children.forEach((i)->nh.add(0,Database.t.getElement(i)));
        if (parents.get(x).get(y).type == NODETYPE.SET) {
            return 1;
        }
        SubPath parent = (SubPath) parents.get(x).get(y);
        /*
        int _x = 0; // TODO allow for parallel branches.
        for (Node_abstract n : nh) {
            if (n == current) {
                break;
            }
            _x += 1;
        }*/
        int _x = current.getPathLoc(parent);
        //System.out.println("HEY: " + current.getName() + " has a location in " + parent.getName() + " that is " + _x);
        if (_x == 0) {return 1;}
        Node_abstract prev = nh.get(_x-1);
        if (prev.type == NODETYPE.BASIC) {return 1;}
        else {
            return ((advancedNode) prev).getOpenEnds();
        }
    }
    private static int[] findParents(Node_abstract current, Node_abstract parent, int x, int y) {
        //System.out.println("Coords: " + x+ ","+y + ":" + current.getName() + ":" + (parent != null ? parent.getName() : "null"));
        if (x >= map.size()) {
            //System.out.println("Failed for "+ x + "," + y + ":" + current.getName());
            return new int[]{x,y};
        }
        if (y >= map.get(x).size())  {System.out.println("Failed for "+ x + "," + y + ":" + current.getName());return new int[]{x,y};}
        if (current.type == NODETYPE.BASIC) {
            int i = y;
            while (parents.get(x).get(y) != null && y < parents.get(x).size()) {
                y += 1;
            }
            parents.get(x).set(y,parent);
            //System.out.println("Coords: " + x+ ","+y + ":" + current.getName() + ":" + (parent != null ? parent.getName() : "null"));
            return new int[]{x,i};
        }
        if (current.type == NODETYPE.SUBPATH) {
            int i = y;
            while (parents.get(x).get(y) != null) {
                y += 1;
            }

            parents.get(x).set(y,parent);
            //System.out.println("Coords: " + x+ ","+y + ":" + current.getName() + ":" + (parent != null ? parent.getName() : "null"));
            y = i;
            ArrayList<Node_abstract> children= new ArrayList<>();
            ((advancedNode) current).getChildNodes().forEach((j)->children.add(Database.t.getElement(j)));
            Collections.reverse(children);
            for (Node_abstract child : children) {
                 int[] cd = findParents(child,current, x+1, y);
                 x = cd[0];
                 //y = cd[1];
                //return new int[]{x,y};
            }
        }
        if (current.type == NODETYPE.SET) {
            int  j= y;
            while (parents.get(x).get(y) != null) {
                y += 1;
            }
            parents.get(x).set(y,parent);
            //System.out.println("Coords: " + x+ ","+y + ":" + current.getName() + ":" + (parent != null ? parent.getName() : "null"));
            y = j;
            ArrayList<Node_abstract> children= new ArrayList<>();
            ((advancedNode) current).getChildNodes().forEach((i)->children.add(Database.t.getElement(i)));
            Collections.reverse(children);
            int i = 0;
            for (Node_abstract child : children) {
                int[] cd = findParents(child,current, x+1, y);
                i += 1;
            }
            return new int[]{x+current.getLength()-1,y};
        }
        return new int[]{x,y};
    }
    private static int recurseiveConnect2(Node_abstract current, Node_abstract linkTo, int cidx, ArrayList<Integer> destinations, boolean destinationOverride) {
        //System.out.println("Called " +cidx+ " " + current.getName());
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
            //System.out.println("Entered subpath " + current.getName());
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
            //System.out.println("Added set " + current.getName());
            destinations.add(cidx+ current.getLength());
            ArrayList<Integer> children = ((advancedNode) current).getChildNodes();
            Collections.reverse(children);
            //System.out.println("Children of " + current.getName() + ": " + Arrays.toString(children.toArray()));
            for (Integer child : children) {
                link_once(current, Database.t.getElement(child), cidx,1);
                recurseiveConnect2(Database.t.getElement(child),linkTo, cidx + 1, destinations, false);
            }
            destinations.remove(destinations.size()-1);
            //System.out.println("Left set " + current.getName());
            cidx += current.getLength()-1;
            return cidx;
        }
        return 0;
    }

}