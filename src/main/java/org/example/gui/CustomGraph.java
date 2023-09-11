package org.example.gui;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;
import org.example.data.NODETYPE;
import org.example.data.Node;
import org.example.data.Node_abstract;
import org.example.data.analysis.depthMap;

import java.util.*;

public class CustomGraph extends mxGraph {
    private HashMap<Object , Object> connections = new HashMap<>(); // <from , to>

    public ArrayList<ArrayList<String>> map;
    public ArrayList<ArrayList<Node_abstract>> nodes;
    public ArrayList<ArrayList<Object>> vertices;

    public ArrayList<ArrayList<Node_abstract>> parents;

    @Override
    public boolean isCellConnectable(Object cell) {
        return false;
    }

    private Node_abstract findNode(Object cell) {
        int[] d = findCoords(cell);
        if (d == null) return null;
        return nodes.get(d[0]).get(d[1]);
    }
    private int[] findCoords(Object cell) {
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (vertices.get(x).get(y).equals(cell)) {
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }
    @Override
    public boolean isCellFoldable(Object cell, boolean collapse)
    {
        return findNode(cell).type != NODETYPE.BASIC && findNode(cell) != depthMap.getMaxDepthStart();
    }
    @Override
    public Object[] foldCells(boolean collapse, boolean recurse, Object[] cells, boolean checkFoldable)
    {
        if(cells == null)
        {
            cells = getFoldableCells(getSelectionCells(), collapse);
        }

        this.getModel().beginUpdate();

        try
        {
            toggleSubtree(this, cells[0], !collapse);
            this.model.setCollapsed(cells[0], collapse);
            fireEvent(new mxEventObject(mxEvent.FOLD_CELLS, "cells", cells, "collapse", collapse, "recurse", recurse));
        }
        finally
        {
            this.getModel().endUpdate();
        }

        return cells;
    }
    private void toggleSubtree(mxGraph graph, Object cellSelected, boolean show)
    {
        List<Object> cellsAffected = new ArrayList<>();
        Node_abstract parent = findNode(cellSelected);
        int parent_x = findCoords(cellSelected)[0];
        ArrayList<mxCell> goal  = new ArrayList<>();

        graph.traverse(cellSelected, true, (vertex, edge) -> {

            //System.out.println(vertex);
            int[] c = findCoords(vertex);
            if (c[0] >= parent_x+parent.getLength()) { //TODO is broken, graph cannot find correct endpoint. Write own traversal algorithm
                goal.add((mxCell) vertex);
                //return false;
            }
            boolean hasPassed = false;
            int[] vc = findCoords(vertex);
            if (edge != null) {
                System.out.println("VERTEX: " + vertex + "; EDGE: " + ((mxCell)edge).getSource());
            }

            if(vertex != cellSelected && !goal.contains(vertex) /*&& (!graph.isCellCollapsed(((mxCell)edge).getSource()) || ((mxCell)edge).getSource() == cellSelected)*/)
            {
                cellsAffected.add(vertex);
                hasPassed = true;
            }
            //System.out.println("Called strange return");
            return vertex == cellSelected || connections.containsValue(edge) || hasPassed;
            //return true;
        });
        //System.out.println(Arrays.toString(goal.toArray()));
        try {
            //System.out.println(goal.get(0));
            graph.toggleCells(show, cellsAffected.toArray(), true);

            if (!show) {
                if (!connections.containsKey(cellSelected)) {
                    connections.put(cellSelected, graph.insertEdge(graph.getDefaultParent(), null, "???", cellSelected, goal.get(0)));
                    System.out.println(Arrays.toString(graph.getOutgoingEdges(cellSelected)));
                    graph.repaint();
                }
            } else {
                if (connections.containsKey(cellSelected)) {
                    graph.getModel().remove(connections.get(cellSelected));
                    connections.remove(cellSelected);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR, CANNOT FIND GOAL :(");
        }
    }
    public static void main(String[] args){
        org.example.Main.main(args);
    }

}
