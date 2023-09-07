package org.example.gui;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomGraph extends mxGraph {
    private mxCell connection;
    @Override
    public boolean isCellConnectable(Object cell) {
        return false;
    }
    @Override
    public boolean isCellFoldable(Object cell, boolean collapse)
    {
        boolean result = super.isCellFoldable(cell, collapse);
        if(!result)
        {
            return this.getOutgoingEdges(cell).length > 1;
        }
        return true;

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
        graph.traverse(cellSelected, true, (vertex, edge) -> {

            if(vertex != cellSelected && !Objects.equals(((mxCell) vertex).getId(), "6"))
            {
                cellsAffected.add(vertex);
            }

            return vertex == cellSelected || !graph.isCellCollapsed(vertex);
        });
        graph.toggleCells(show, cellsAffected.toArray(), true);

        if (!show) {
            connection = (mxCell)graph.insertEdge(graph.getDefaultParent(), null, "", ((mxGraphModel) graph.getModel()).getCell("3"), ((mxGraphModel) graph.getModel()).getCell("6"));
        }
        else{
            if (connection != null){
                graph.getModel().remove(connection);
                connection = null;
            }
        }
    }
    public static void main(String[] args){
        System.out.println("OOPs you executed the wrong file");
    }

}
