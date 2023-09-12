package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.example.data.Database;
import org.example.data.analysis.depthMap;
import org.example.gui.GraphFrame;
import org.example.io.JsonLoad;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        File file = new File("src/main/java/org/example/xyz.json");
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JsonLoad.loadFromJson(content);
        } catch (IOException e) {
            System.out.println("Failed to load json: File error");
            return;
        }
        System.out.println("Unpack started");
        Database.t.unpack_all();
        Database.t.debug();
        System.out.println("Unpack completed");
        depthMap.runDepthCalc();
        ArrayList<ArrayList<String>> blocks = depthMap.mapHorizontal();
        GraphFrame.visualize(blocks);
    }
}