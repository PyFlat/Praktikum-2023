package org.example.gui;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;
import org.example.data.NODETYPE;
import org.example.data.Node_abstract;
import org.example.data.analysis.depthMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomGraph extends mxGraph {
    private final HashMap<Object , Object> connections = new HashMap<>();
    public ArrayList<ArrayList<String>> map;
    public ArrayList<ArrayList<Node_abstract>> nodes;
    public ArrayList<ArrayList<Object>> vertices;
    public ArrayList<ArrayList<Node_abstract>> parents;

    @Override
    public boolean isCellConnectable(Object cell) {
        return false;
    }

    private Node_abstract findNode(Object cell) {
        int[] coords = findCoords(cell);
        if (coords == null) return null;
        return this.nodes.get(coords[0]).get(coords[1]);
    }
    private int[] findCoords(Object cell) {
        for (int x = 0; x < this.map.size(); x++) {
            for (int y = 0; y < this.map.get(x).size(); y++) {
                if (this.vertices.get(x).get(y).equals(cell)) {
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }
    @Override
    public boolean isCellFoldable(Object cell, boolean collapse)
    {
        return Objects.requireNonNull(findNode(cell)).type != NODETYPE.BASIC && findNode(cell) != depthMap.getMaxDepthStart();
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
    void toggleSubtree(mxGraph graph, Object cellSelected, boolean show)
    {
        List<Object> cellsAffected = new ArrayList<>();
        Node_abstract parent = findNode(cellSelected);
        int parent_x = Objects.requireNonNull(findCoords(cellSelected))[0];
        ArrayList<mxCell> goal  = new ArrayList<>();

        graph.traverse(cellSelected, true, (vertex, edge) -> {

            int[] coords = findCoords(vertex);
            if ((coords != null ? coords[0] : 0) >= parent_x+ (parent != null ? parent.getLength() : 0)) {
                goal.add((mxCell) vertex);
            }
            boolean hasPassed = false;
            if (edge == null) {return true;}
            if (((mxCell)edge).getSource().isCollapsed() && ((mxCell)edge).getSource() != cellSelected && !((mxCell)edge).getValue().equals(" ")) {
                return false;
            }
            if(vertex != cellSelected && !goal.contains((mxCell) vertex))
            {
                cellsAffected.add(vertex);
                hasPassed = true;
            }
            return vertex == cellSelected || this.connections.containsValue(edge) || hasPassed;
        });
        try {
            graph.toggleCells(show, cellsAffected.toArray(), true);

            if (!show) {
                if (!this.connections.containsKey(cellSelected)) {
                    this.connections.put(cellSelected, graph.insertEdge(graph.getDefaultParent(), null, " ", cellSelected, goal.get(0), "strokeColor=#ccd0d9;strokeWidth=1;"));
                    graph.repaint();
                }
            } else {
                if (this.connections.containsKey(cellSelected)) {
                    graph.getModel().remove(this.connections.get(cellSelected));
                    this.connections.remove(cellSelected);
                }
            }
        } catch (IndexOutOfBoundsException ignored) {}
    }
    public static void main(String[] args){
        System.out.println("You stupid, wrong class");
        System.exit(1);
    }

}
