package org.example.gui;

import org.example.data.Node_abstract;

import java.util.ArrayList;

public class guiUtils {
    public static Node_abstract findNode(Object cell, ArrayList<ArrayList<Node_abstract>> nodes, ArrayList<ArrayList<Object>> vertices) {
        int[] coords = findCoords(cell,vertices);
        if (coords == null) return null;
        return nodes.get(coords[0]).get(coords[1]);
    }
    public static int[] findCoords(Object cell, ArrayList<ArrayList<Object>> vertices) {
        for (int x = 0; x < vertices.size(); x++) {
            for (int y = 0; y < vertices.get(x).size(); y++) {
                if (vertices.get(x).get(y).equals(cell)) {
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }
}
