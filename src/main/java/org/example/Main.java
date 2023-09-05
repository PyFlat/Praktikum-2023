package org.example;

import org.example.data.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World");
        new SUBPATH("Pfad-0",new ArrayList<String>(
                Arrays.asList("Quelle-0","test","Teilpfad-2")
        ), new ArrayList<Float>(
                Arrays.asList(1f,1f,1f)
        ), new ArrayList<Integer>(Arrays.asList(-1,-1,-1)),TYPES.NORMAL);
        new SET("test",PRIORITY.RANDOM,new ArrayList<String>(
                Arrays.asList("Regale bottom", "Teilpfad-1")
        ));
        new SET("Regale bottom", PRIORITY.RANDOM, new ArrayList<String>(
                Arrays.asList("Ziel-3", "Ziel-5", "Ziel-6","Ziel-7","Ziel-2")
        ));
        new SET("Regale top", PRIORITY.RANDOM, new ArrayList<String>(
                Arrays.asList("Ziel-1","Ziel-4","Ziel-9","Ziel-12","Ziel-13","Ziel-8")
        ));
        new SET("Kassen", PRIORITY.SHORTEST_WAIT, new ArrayList<String>(
                Arrays.asList("Warteschlange-0","Warteschlange-1","Warteschlange-2")
        ));
        new SUBPATH("Teilpfad-1",new ArrayList<String>(
                Arrays.asList("Aufzug-0","Regale-top","Aufzug-0")
        ), new ArrayList<Float>(
                Arrays.asList(1f,1f,1f)
        ), new ArrayList<Integer>(Arrays.asList(-1,-1,-1)),TYPES.NORMAL);
        new SUBPATH("Teilpfad-2",new ArrayList<String>(
                Arrays.asList("Kassen","Ziel-0")
        ), new ArrayList<Float>(
                Arrays.asList(1f,1f)
        ), new ArrayList<Integer>(Arrays.asList(-1,-1)),TYPES.NORMAL);

        new NODE("Quelle-0");
        new NODE("Ziel-0");
        new NODE("Ziel-1");
        new NODE("Ziel-2");
        new NODE("Ziel-3");
        new NODE("Ziel-4");
        new NODE("Ziel-5");
        new NODE("Ziel-6");
        new NODE("Ziel-7");
        new NODE("Ziel-8");
        new NODE("Ziel-9");
        new NODE("Ziel-12");
        new NODE("Ziel-13");
        new NODE("Warteschlange-0");
        new NODE("Warteschlange-1");
        new NODE("Warteschlange-2");
        new NODE("Aufzug-0");

        NODE.database.unpack_all();
    }
}