package org.example.data.analysis;

import org.example.data.*;

import java.util.ArrayList;

public class dephtmap {
    public static void runDephtcalc() {
        System.out.println("Depht calculation started.");
        NODE.database.getElements().forEach((n)->n.calculateLength());
        NODE.database.getElements().forEach((n)->System.out.println(n.getName() + " " + n.getLength()));
        System.out.println("Depht calculation ended.");
    }
    public static int getMaxDepth() {
        int mx = 0;
        for (int i=0;i< NODE.database.getElements().size(); i++) {
            if (NODE.database.getElements().get(i).getLength() > mx) {
                mx = NODE.database.getElements().get(i).getLength();
            }
        }
        return mx;
    }
    public static NODE getMaxDephtStart() {
        int mx = 0;
        NODE n = null;
        for (int i=0;i< NODE.database.getElements().size(); i++) {
            if (NODE.database.getElements().get(i).getLength() > mx) {
                mx = NODE.database.getElements().get(i).getLength();
                n = (NODE) NODE.database.getElements().get(i);
            }
        }
        return n;
    }
    private static int placeBranch(ArrayList<ArrayList<String>>  map, int cidx, NODE next) {
        System.out.println("Entered branch with: " +cidx + " " + next.getName());
        map.get(cidx).add(next.getName());
        if (next.type == NODETYPE.BASIC) {

            return cidx;
        } else if (next.type == NODETYPE.SUBPATH) {
            //map.get(cidx).add(next.getName());
            for (int i=((SUBPATH) next).getChildNodes().size()-1;i>-1 ;i--) {
                cidx = placeBranch(map, cidx+1,(NODE) NODE.database.getElement(((SUBPATH) next).getChildNodes().get(i)));
            }
        } else if (next.type == NODETYPE.SET) {
            //map.get(cidx).add(next.getName());
            for (int i=((SET) next).getChildNodes().size()-1;i>-1 ;i--) {
                placeBranch(map, cidx+1,(NODE) NODE.database.getElement(((SET) next).getChildNodes().get(i)));
            }
            cidx += ((SET) next).getLength()-1;
            return cidx;
        }
        return 0;
    }
    public static ArrayList<ArrayList<String>> mapHorizontal() {
        ArrayList<ArrayList<String>> map = new ArrayList<ArrayList<String>>();
        for (int i=0;i<getMaxDepth();i++) {
            map.add(new ArrayList<String>());
        }
        placeBranch(map, 0, getMaxDephtStart());
        return map;
    }
}
