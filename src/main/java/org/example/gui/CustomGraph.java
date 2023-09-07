package org.example.gui;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;

import java.util.ArrayList;
import java.util.List;

public class CustomGraph extends mxGraph {
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
            System.out.println(this.getIncomingEdges(cell).length);
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

            if(vertex != cellSelected)
            {
                cellsAffected.add(vertex);
            }

            return vertex == cellSelected || !graph.isCellCollapsed(vertex);
        });

        graph.toggleCells(show, cellsAffected.toArray(), true);
    }

}
