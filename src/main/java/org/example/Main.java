package org.example;

import org.example.data.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.example.data.analysis.dephtmap;
import org.example.gui.GraphFrame;
import org.example.io.JsonLoad;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        File file = new File("src/main/java/org/example/xy.json");
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JsonLoad.loadFromJson(content);
        } catch (IOException e) {
            System.out.println("Failed to load json: File error");
            return;
        }
        System.out.println("Unpack started");
        NODE.database.unpack_all();
        NODE.database.debug();
        System.out.println("Unpack completed");
        dephtmap.runDephtcalc();
        ArrayList<ArrayList<String>> blocks = dephtmap.mapHorizontal();
        System.out.println(Arrays.toString(blocks.toArray()));
        GraphFrame.visualize(blocks);
    }
}