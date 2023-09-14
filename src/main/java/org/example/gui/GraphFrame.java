package org.example.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;
import org.example.Main;
import org.example.data.*;
import org.example.data.analysis.depthMap;
import org.example.gui.events.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GraphFrame extends JFrame {
    private static Object parent;
    private static mxHierarchicalLayout layout;
    private static CustomGraph graph;
    private static ArrayList<ArrayList<Object>> vertices;

    private static  ArrayList<ArrayList<String>> map;

    private static ArrayList<ArrayList<Node_abstract>> nodes;

    private static ArrayList<ArrayList<Node_abstract>> parents;
    private static ArrayList<ArrayList<Integer>> acceptMultipleInputs;

    private static Node_abstract findNode(Object cell) {
        int[] d = findCoords(cell);
        if (d == null) return null;
        try {
            return nodes.get(d[0]).get(d[1]);
        } catch (NullPointerException e) {
            return null;
        }
    }
    private static int[] findCoords(Object cell) {
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (vertices.get(x).get(y).equals(cell)) {
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }

    public static void visualize(ArrayList<ArrayList<String>> newmap) {
        //System.out.println(Arrays.toString(newmap.toArray()));
        map = newmap;
        graph = new CustomGraph();
        parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        layout = new mxHierarchicalLayout(graph);
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
                    nodes.get(x).add(n);
                    NODETYPE type = n.type;
                    String style = "fillColor=#3c3c3c;strokeColor=#ccd0d9;fontColor=#ffffff;fontSize=12;";
                    if (type == NODETYPE.SET) {style += "rounded=1";}
                    if (type == NODETYPE.SUBPATH) {style += "";}
                    if (type == NODETYPE.BASIC) {style += "shape=ellipse";}
                    acceptMultipleInputs.get(x).add(1);
                    parents.get(x).add(null);
                    mxCell vertex = (mxCell) graph.insertVertex(parent, null, map.get(x).get(y),x*100+(x-1)*100+100,y*50+(y-1)*30+30,100,50, style);
                    vertices.get(x).add(vertex);
                }
            }
            graph.map = map;
            graph.nodes = nodes;
            graph.vertices = vertices;
            PropertyDisplayFrame.nodes = nodes;
            PropertyDisplayFrame.vertices = vertices;
            System.out.println("Finished precalc, started linking");
            link();
            /**/
        } finally {
            if (Main.config.get("START_COLLAPSED").equals("true")) {
                collapseAll(false);
            }
            if (Main.config.get("USE_LAYOUT").equals("true")) {
                layout.execute(parent);
            }
            graph.getModel().endUpdate();
        }
        graph.addListener(mxEvent.FOLD_CELLS, (sender, evt) -> {
            try {
                if (Main.config.get("USE_LAYOUT").equals("true")) {
                    layout.execute(parent);
                }
            } catch (StackOverflowError e) {System.out.println("WARNING! LAYOUT DISABLED BECAUSE OF OVERFLOW! REDUCE COMPLEXITY!");}
        });
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        mouseEventProcessor p = new mouseEventProcessor(new EventHighlightListener() {
            @Override
            public void highlightStart(MouseEvent event, Object cell) {
                if (findNode(cell) == depthMap.getMaxDepthStart()) {return;}
                if (event.isShiftDown()) {

                    int[] coords = findCoords(cell);
                    assert coords != null;
                    Node_abstract PARENT = parents.get(coords[0]).get(coords[1]);
                    ObjectContainer parent_vertex = new ObjectContainer();
                    graph.traverse(cell, false, (v, e)->{
                        if (parent_vertex.get() != null) {return false;}
                        if (e == null) {return true;}
                        for (Object _e : graph.getOutgoingEdges(cell)) {if (_e.equals(e)) {return false;}}
                        if (Objects.equals(findNode(v), PARENT)) {
                            parent_vertex.set(v);
                            return false;
                        }
                        return true;
                    });
                    List<Object> cellsAffected = new ArrayList<>();
                    Node_abstract parent = findNode(parent_vertex.get());
                    int parent_x = Objects.requireNonNull(findCoords(parent_vertex.get()))[0];
                    ArrayList<mxCell> goal  = new ArrayList<>();
                    graph.traverse(parent_vertex.get(), true, (vertex, edge) -> {

                        int[] c = findCoords(vertex);
                        if ((c != null ? c[0] : 0) >= parent_x+ (parent != null ? parent.getLength() : 0)) {
                            goal.add((mxCell) vertex);
                            //return false;
                        }
                        boolean hasPassed = false;
                        if (edge == null) {return true;}
                        if (((mxCell)edge).getSource().isCollapsed() && ((mxCell)edge).getSource() != parent_vertex.get() && !((mxCell)edge).getValue().equals(" ")) {
                            return false;
                        }
                        if(vertex != parent_vertex.get() && !goal.contains((mxCell) vertex) /*&& (!graph.isCellCollapsed(((mxCell)edge).getSource()) || ((mxCell)edge).getSource() == cellSelected)*/)
                        {
                            cellsAffected.add(vertex);
                            hasPassed = true;
                        }
                        //System.out.println("Called strange return");
                        return vertex == parent_vertex.get() || hasPassed;
                        //return true;
                    });
                    Object[] edges = graph.addAllEdges(cellsAffected.toArray());
                    for (Object e: edges) {((mxCell)e).setStyle(((mxCell)e).getStyle().replace("strokeColor=#ccd0d9","strokeColor=#FF0000"));((mxCell)e).setStyle(((mxCell)e).getStyle().replace("strokeWidth=1", "strokeWidth=2"));}
                    ((mxCell)parent_vertex.get()).setStyle(((mxCell)parent_vertex.get()).getStyle().replace("strokeColor=#ccd0d9","strokeColor=#FF0000"));
                    graph.refresh();
                    return;
                }
                for (Object edges : graph.getEdges(cell)){
                    mxCell edge = (mxCell) edges;
                    String existingStyle = edge.getStyle();
                    String updatedStyle = existingStyle.replaceAll("strokeColor=#ccd0d9", "strokeColor=#FF0000");
                    updatedStyle = updatedStyle.replaceAll("strokeWidth=1", "strokeWidth=2");
                    edge.setStyle(updatedStyle);
                    graph.refresh();
                }

            }

            @Override
            public void highlightStop(MouseEvent event, Object cell) {
                if (findNode(cell) == depthMap.getMaxDepthStart()) {return;}
                int[] coords = findCoords(cell);
                assert coords != null;
                Node_abstract PARENT = parents.get(coords[0]).get(coords[1]);
                ObjectContainer parent_vertex = new ObjectContainer();
                graph.traverse(cell, false, (v, e) -> {
                    if (parent_vertex.get() != null) {
                        return false;
                    }
                    if (e == null) {
                        return true;
                    }
                    for (Object _e : graph.getOutgoingEdges(cell)) {
                        if (_e.equals(e)) {
                            return false;
                        }
                    }
                    //System.out.println("Passed by " + vertex);
                    if (Objects.equals(findNode(v), PARENT)) {
                        parent_vertex.set(v);
                        return false;
                    }
                    return true;
                });
                List<Object> cellsAffected = new ArrayList<>();
                Node_abstract parent = findNode(parent_vertex.get());
                int parent_x = Objects.requireNonNull(findCoords(parent_vertex.get()))[0];
                ArrayList<mxCell> goal = new ArrayList<>();
                graph.traverse(parent_vertex.get(), true, (vertex, edge) -> {

                    //System.out.println(vertex);
                    int[] c = findCoords(vertex);
                    if ((c != null ? c[0] : 0) >= parent_x + (parent != null ? parent.getLength() : 0)) {
                        goal.add((mxCell) vertex);
                        //return false;
                    }
                    boolean hasPassed = false;
                    if (edge == null) {
                        return true;
                    }
                    if (((mxCell) edge).getSource().isCollapsed() && ((mxCell) edge).getSource() != parent_vertex.get() && !((mxCell) edge).getValue().equals(" ")) {
                        return false;
                    }
                    if (vertex != parent_vertex.get() && !goal.contains((mxCell) vertex) /*&& (!graph.isCellCollapsed(((mxCell)edge).getSource()) || ((mxCell)edge).getSource() == cellSelected)*/) {
                        cellsAffected.add(vertex);
                        hasPassed = true;
                    }
                    //System.out.println("Called strange return");
                    return vertex == parent_vertex.get() || hasPassed;
                    //return true;
                });
                Object[] edges = graph.addAllEdges(cellsAffected.toArray());
                for (Object e : edges) {
                    ((mxCell) e).setStyle(((mxCell) e).getStyle().replace("strokeColor=#FF0000", "strokeColor=#ccd0d9"));
                    ((mxCell)e).setStyle(((mxCell)e).getStyle().replace("strokeWidth=2", "strokeWidth=1"));
                }
                ((mxCell) parent_vertex.get()).setStyle(((mxCell) parent_vertex.get()).getStyle().replace("strokeColor=#FF0000", "strokeColor=#ccd0d9"));
                graph.refresh();
            }
        }, graphComponent);
        graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                p.processEvent(e);
            }

            @Override
            public void mouseDragged(MouseEvent e){
                p.processEvent(e);
            }
        });

        graphComponent.addMouseWheelListener(new CustomMouseWheelListener(graphComponent));
        graphComponent.getGraphControl().addMouseListener(new PopupListener(graphComponent));
        graphComponent.getViewport().setBackground(new Color(30, 30, 30));
        graphComponent.getVerticalScrollBar().setUI(new CustomScrollbarUI());
        graphComponent.getVerticalScrollBar().setUnitIncrement(20);
        graphComponent.getHorizontalScrollBar().setUI(new CustomScrollbarUI());
        graphComponent.getHorizontalScrollBar().setUnitIncrement(20);
        GraphFrame frame = new GraphFrame();
        frame.getContentPane().add(graphComponent);
        frame.setJMenuBar(createMenuBar(frame, graph));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private static void collapseAll(boolean reversed) {
        for (int x = map.size()-1;x>-1;x--) {
            for (int y = map.get(x).size()-1;y>-1;y--) {
                if (graph.isCellFoldable(vertices.get(x).get(y),!reversed)) {
                    graph.toggleSubtree(graph,vertices.get(x).get(y),reversed);
                    graph.getModel().setCollapsed(vertices.get(x).get(y),!reversed);
                }
            }
        }
    }
    private static JMenuBar createMenuBar(GraphFrame frame, mxGraph graph) {
        ActionListener listener = e -> {
            JFileChooser fd = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Png Files (.png)", "png");
            fd.setFileFilter(filter);
            int returnValue = fd.showSaveDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION){
                String filePath = fd.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".png")){
                    filePath += ".png";
                }
                BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, new Color(30,30,30), true, null);
                try {
                    ImageIO.write(image, "PNG", new File(filePath));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        JMenuBar mb = new JMenuBar();
        JMenu menu1 = new JMenu("Tools");
        JMenuItem exportPng = new JMenuItem("Export as PNG");
        exportPng.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
        exportPng.addActionListener(listener);
        menu1.add(exportPng);

        JMenuItem collapseAll = new JMenuItem("Collapse All");
        collapseAll.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_DOWN_MASK));
        collapseAll.addActionListener(e -> {
            collapseAll(false);
            if (Main.config.get("USE_LAYOUT").equals("true")) {
                layout.execute(parent);
            }

        });
        menu1.add(collapseAll);

        JMenuItem expandAll = new JMenuItem("Expand All");
        expandAll.setAccelerator(KeyStroke.getKeyStroke('E',KeyEvent.CTRL_DOWN_MASK));
        expandAll.addActionListener(e -> {
            collapseAll(true);
            if (Main.config.get("USE_LAYOUT").equals("true")) {
                layout.execute(parent);
            }
        });
        menu1.add(expandAll);
        mb.add(menu1);
        return mb;
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
        graph.insertEdge(parent,null,"",from,to, "strokeColor=#ccd0d9;strokeWidth=1;");
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
        int _x = current.getPathLoc(parent);
        //System.out.println("HEY: " + current.getName() + " has a location in " + parent.getName() + " that is " + _x);
        if (_x == 0) {return 1;}
        Node_abstract prev = nh.get(_x-1);
        if (prev.type == NODETYPE.BASIC) {return 1;}
        else {
            return prev.getOpenEnds();
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
            for (Node_abstract child : children) {
                findParents(child, current, x + 1, y);
            }
            return new int[]{x+current.getLength()-1,y};
        }
        return new int[]{x,y};
    }
    private static int recurseiveConnect2(Node_abstract current, Node_abstract linkTo, int cidx, ArrayList<Integer> destinations, boolean destinationOverride) {
        if (current.type == NODETYPE.BASIC) {
            if (destinationOverride) {
                link_once(current, linkTo, cidx, 1);
                return cidx;
            }
            if (!destinations.isEmpty()) {
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
            children.forEach((e)->node_children.add(Database.t.getElement(e)));
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