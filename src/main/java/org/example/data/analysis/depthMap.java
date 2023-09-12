package org.example.data.analysis;

import java.util.ArrayList;
import java.util.Map;


import org.example.data.*;

public class depthMap {
    public static void runDepthCalc() {
        System.out.println("Depth calculation started.");
        for (Map.Entry<Integer , Node_abstract> entry : Database.t.getNodes().entrySet()) {
            entry.getValue().calculateLength();
        }
        System.out.println("Depth calculation ended.");
    }
    public static int getMaxDepth() {
        int mx = 0;
        for (int i=0;i< Database.t.getNodes().size(); i++) {
            if (Database.t.getNodes().get(i).getLength() > mx) {
                mx = Database.t.getNodes().get(i).getLength();
            }
        }
        return mx;
    }
    public static Node_abstract getMaxDepthStart() {
        int mx = 0;
        Node_abstract n = null;
        for (int i=0;i< Database.t.getNodes().size(); i++) {
            if (Database.t.getNodes().get(i).getLength() > mx) {
                mx = Database.t.getNodes().get(i).getLength();
                n = Database.t.getNodes().get(i);
            }
        }
        return n;
    }
    private static int placeBranch(ArrayList<ArrayList<String>>  map, int cidx, Node_abstract next) {
        //System.out.println("Entered branch with: " +cidx + " " + next.getName());
        map.get(cidx).add(next.getName());
        if (next.type == NODETYPE.BASIC) {

            return cidx;
        } else if (next.type == NODETYPE.SUBPATH) {
            //map.get(cidx).add(next.getName());
            for (int i = ((advancedNode) next).getChildNodes().size()-1; i>-1 ; i--) {
                cidx = placeBranch(map, cidx+1, Database.t.getElement(((SubPath) next).getChildNodes().get(i)));

            }
            return cidx;
        } else if (next.type == NODETYPE.SET) {
            //map.get(cidx).add(next.getName());
            for (int i = ((Set) next).getChildNodes().size()-1; i>-1 ; i--) {
                placeBranch(map, cidx+1, Database.t.getElement(((Set) next).getChildNodes().get(i)));
            }
            cidx += next.getLength()-1;
            return cidx;
        }
        throw new ArrayIndexOutOfBoundsException("ERROR");
        //return 0;
    }
    public static ArrayList<ArrayList<String>> mapHorizontal() {
        ArrayList<ArrayList<String>> map = new ArrayList<>();
        for (int i=0;i<getMaxDepth();i++) {
            map.add(new ArrayList<>());
        }
        placeBranch(map, 0, getMaxDepthStart());
        return map;
    }
}
