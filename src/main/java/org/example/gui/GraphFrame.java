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

import static javax.swing.SwingConstants.WEST;

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
    private static int[] findCoords(Object cell) { //TODO move to utils
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (vertices.get(x).get(y).equals(cell)) {
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }
    private static void changeCellColor(Object cell, String newColor, int newWidth) {
        int[] coords = findCoords(cell);
        assert coords != null;
        Node_abstract PARENT = parents.get(coords[0]).get(coords[1]);
        ObjectContainer<Object> parentVertexContainer = new ObjectContainer<>();

        findParentVertex(cell, PARENT, parentVertexContainer);

        List<Object> cellsAffected = new ArrayList<>();
        Node_abstract parent = findNode(parentVertexContainer.get());
        int parent_x = Objects.requireNonNull(findCoords(parentVertexContainer.get()))[0];
        ArrayList<mxCell> goal  = new ArrayList<>();
        graph.traverse(parentVertexContainer.get(), true, (vertex, edge) -> {
            int[] currentCoords = findCoords(vertex);
            if ((currentCoords != null ? currentCoords[0] : 0) >= parent_x+ (parent != null ? parent.getLength() : 0)) {
                goal.add((mxCell) vertex);
            }
            boolean hasPassed = false;
            if (edge == null) {return true;}
            if (((mxCell)edge).getSource().isCollapsed() && ((mxCell)edge).getSource() != parentVertexContainer.get() && !((mxCell)edge).getValue().equals(" ")) {
                return false;
            }
            if(vertex != parentVertexContainer.get() && !goal.contains((mxCell) vertex))
            {
                cellsAffected.add(vertex);
                hasPassed = true;
            }
            return vertex == parentVertexContainer.get() || hasPassed;
        });
        Object[] edges = graph.addAllEdges(cellsAffected.toArray());
        for (Object edge: edges) {
            ((mxCell) edge).setStyle(((mxCell)edge).getStyle().replaceAll("strokeColor=#[0-9a-fA-F]{6}","strokeColor=" + newColor));
            ((mxCell) edge).setStyle(((mxCell)edge).getStyle().replaceAll("strokeWidth=-?[0-9]+;","strokeWidth=" + newWidth + ";"));
        }
        ((mxCell) parentVertexContainer.get()).setStyle(((mxCell)parentVertexContainer.get()).getStyle().replaceAll("strokeColor=#[0-9a-fA-F]{6}","strokeColor=" + newColor));
        ((mxCell) parentVertexContainer.get()).setStyle(((mxCell)parentVertexContainer.get()).getStyle().replaceAll("strokeWidth=-?[0-9]+;","strokeWidth=" + newWidth + ";"));
        graph.refresh();
    }
    public static void visualize() {
        map = depthMap.mapHorizontal();
        graph = new CustomGraph();
        parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();

        layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(WEST);

        graph.setEdgeLabelsMovable(false);
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false);
        graph.setCellsSelectable(false);

        vertices = new ArrayList<>();
        nodes = new ArrayList<>();
        acceptMultipleInputs = new ArrayList<>();
        parents = new ArrayList<>();

        try {
            System.out.println("[LOG] PreConnect tasks started");
            for (int x=0;x<map.size();x++) {

                vertices.add(new ArrayList<>());
                nodes.add(new ArrayList<>());
                acceptMultipleInputs.add(new ArrayList<>());
                parents.add(new ArrayList<>());

                for (int y=0;y<map.get(x).size();y++) {
                    Node_abstract node = Database.t.getElementByKey(map.get(x).get(y));
                    nodes.get(x).add(node);
                    NODETYPE type = node.type;

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
            System.out.println("[LOG] Finished PreConnect tasks");
            System.out.println("[LOG] Started connection tasks");
            link();
        } finally {
            if (Main.config.get("START_COLLAPSED").equals("true")) {
                collapseAll(false);
            }
            if (Main.config.get("USE_LAYOUT").equals("true")) {
                layout.execute(parent);
            }
            graph.getModel().endUpdate();
        }
        graph.addListener(mxEvent.FOLD_CELLS, (sender, event) -> {
            try {
                if (Main.config.get("USE_LAYOUT").equals("true")) {
                    layout.execute(parent);
                }
            } catch (StackOverflowError e) {System.out.println("[WARNING] WARNING! LAYOUT DISABLED BECAUSE OF OVERFLOW! REDUCE COMPLEXITY!");}
        });
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        mouseEventProcessor mouseEventHandler = new mouseEventProcessor(new EventHighlightListener() {
            @Override
            public void highlightStart(MouseEvent event, Object cell) {
                if (findNode(cell) == depthMap.getMaxDepthStart()) {return;}
                if (event.isShiftDown()) {
                    changeCellColor(cell,"#FF0000",2);
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
                changeCellColor(cell,"#ccd0d9",1);
            }
        }, graphComponent);
        graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseEventHandler.processEvent(e);
            }

            @Override
            public void mouseDragged(MouseEvent e){
                mouseEventHandler.processEvent(e);
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

    private static void findParentVertex(Object origin, Node_abstract parent, ObjectContainer<Object> parentVertexContainer) {
        graph.traverse(origin, false, (vertex, edge)->{
            if (parentVertexContainer.get() != null) {return false;}
            if (edge == null) {return true;}
            for (Object outgoingEdge : graph.getOutgoingEdges(origin)) {
                if (outgoingEdge.equals(edge)) {return false;}
            }
            if (Objects.equals(findNode(vertex), parent)) {
                parentVertexContainer.set(vertex);
                return false;
            }
            return true;
        });
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
        System.out.println("[LOG] Started parent mapping");
        findParents(depthMap.getMaxDepthStart(), null, 0,0);
        graph.parents = parents;
        System.out.println("[LOG] Finished parent mapping");
        System.out.println("[LOG] Calculating maximum incoming connections");
        for (int x = 0; x < map.size();x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                acceptMultipleInputs.get(x).set(y,findMaxConnections(x,y));
            }
        }
        System.out.println("[LOG] Finished maximum incoming edge calculation");
        System.out.println("[LOG] Connecting edges");
        recursiveConnect();
        System.out.println("[LOG] Finished edge connection");
    }
    private static void link_once(Object from, Object to) {
        graph.insertEdge(parent,null,"",from,to, "strokeColor=#ccd0d9;strokeWidth=1;");
    }
    private static void link_once(Node_abstract from, Node_abstract to, int cidx, int delta) {
        link_once(getVertex(from, cidx,false),getVertex(to, cidx+delta, true));
    }
    private static Object getVertex(Node_abstract n, int cidx, boolean to) {
        if (n == null) {return null;}
            for (int y=0;y<map.get(cidx).size(); y++) {
                if (map.get(cidx).get(y).equals(n.getName())) {
                    if (to) {
                        if (graph.getIncomingEdges(vertices.get(cidx).get(y)).length < acceptMultipleInputs.get(cidx).get(y)) {
                            return vertices.get(cidx).get(y);
                        }
                    } else {
                        mxCell vertex = (mxCell) vertices.get(cidx).get(y);
                        Node_abstract node = nodes.get(cidx).get(y);
                        int limit = 1;
                        if (node.type == NODETYPE.SET) {
                            limit = ((advancedNode)node).getChildNodes().size();
                        }
                        if (graph.getOutgoingEdges(vertex).length< limit) {
                            return vertices.get(cidx).get(y);
                        }

                    }
                }

            }
        return null;
    }
    private static int findMaxConnections(int x, int y) {
        if (parents.get(x).get(y) == null) {return 1;}
        Node_abstract current = nodes.get(x).get(y);
        ArrayList<Integer> childIndices = ((advancedNode) parents.get(x).get(y)).getChildNodes();
        ArrayList<Node_abstract> childNodes = new ArrayList<>();
        childIndices.forEach((index)->childNodes.add(0,Database.t.getElement(index)));
        if (parents.get(x).get(y).type == NODETYPE.SET) return 1;
        SubPath parent = (SubPath) parents.get(x).get(y);
        int indexOnParentChildrenList = current.getPathLoc(parent);
        if (indexOnParentChildrenList == 0) return 1;
        Node_abstract previousChild = childNodes.get(indexOnParentChildrenList-1);
        if (previousChild.type == NODETYPE.BASIC) return 1;
        return previousChild.getOpenEnds();
    }
    private static int[] findParents(Node_abstract current, Node_abstract parent, int x, int y) {
        if (x >= map.size()) return new int[]{x,y};
        if (current.type == NODETYPE.BASIC) {
            int returnYValue = y;
            while (parents.get(x).get(y) != null && y < parents.get(x).size()) {
                y += 1;
            }
            parents.get(x).set(y,parent);
            return new int[]{x,returnYValue};
        }
        if (current.type == NODETYPE.SUBPATH) {
            int tempYValue = y;
            while (parents.get(x).get(y) != null) {
                y += 1;
            }
            parents.get(x).set(y,parent);
            y = tempYValue;
            ArrayList<Node_abstract> children= new ArrayList<>();
            ((advancedNode) current).getChildNodes().forEach((j)->children.add(0,Database.t.getElement(j)));
            //Collections.reverse(children);
            for (Node_abstract child : children) {
                 int[] coords = findParents(child,current, x+1, y);
                 x = coords[0];
            }
        }
        if (current.type == NODETYPE.SET) {
            int  tempYValue= y;
            while (parents.get(x).get(y) != null) {
                y += 1;
            }
            parents.get(x).set(y,parent);
            y = tempYValue;
            ArrayList<Node_abstract> children= new ArrayList<>();
            ((advancedNode) current).getChildNodes().forEach((i)->children.add(0,Database.t.getElement(i)));
            for (Node_abstract child : children) {
                findParents(child, current, x + 1, y);
            }
            return new int[]{x+current.getLength()-1,y};
        }
        return new int[]{x,y};
    }
    private static void recursiveConnect() {
        ArrayList<Integer> destinations = new ArrayList<>();
        recursiveConnect(depthMap.getMaxDepthStart(),null,0,destinations, false);
    }
    private static int recursiveConnect(Node_abstract current, Node_abstract linkTo, int cidx, ArrayList<Integer> destinations, boolean destinationOverride) {
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
            ArrayList<Integer> children = ((advancedNode) current).getChildNodes();
            ArrayList<Node_abstract> node_children = new ArrayList<>();
            children.forEach((e)->node_children.add(Database.t.getElement(e)));
            Collections.reverse(node_children);
            link_once(current,node_children.get(0),cidx,1);
            for (int i=0;i<node_children.size();i++) {
                if (i == node_children.size()-1) {
                    cidx = recursiveConnect(node_children.get(i),linkTo,cidx+1, destinations, false);
                    return cidx;
                } else {
                    cidx = recursiveConnect(node_children.get(i),node_children.get(i+1),cidx+1, destinations, true);
                }
            }
        }
        if (current.type == NODETYPE.SET) {
            destinations.add(cidx+ current.getLength());
            ArrayList<Integer> children = ((advancedNode) current).getChildNodes();
            Collections.reverse(children);
            for (Integer child : children) {
                link_once(current, Database.t.getElement(child), cidx,1);
                recursiveConnect(Database.t.getElement(child),linkTo, cidx + 1, destinations, false);
            }
            destinations.remove(destinations.size()-1);
            cidx += current.getLength()-1;
            return cidx;
        }
        return 0;
    }
}