package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.data.Database;
import org.example.data.analysis.depthMap;
import org.example.gui.GraphFrame;
import org.example.io.JsonLoad;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main {
    public static HashMap<String , String> config = new HashMap<>();
    private static void loadConfig() {
        File cfgFile = new File("config.cfg");
        try {
            String config = new String(Files.readAllBytes(Paths.get(cfgFile.toURI())));
            String[] lines = config.split("(;?(\r\n|\n|\r)|;(\r\n|\r|\n)?)");
            for (String line : lines) {
                Main.config.put(line.split(" *= *")[0],line.split(" *= *")[1]);
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Unable to reach config.cfg");
            System.exit(1);
        }
    }
    private static void loadJson() {
        File file = new File(config.get("JSON"));
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JsonLoad.loadFromJson(content);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to load json: File error");
            System.exit(1);
        }
    }
    public static void main(String[] args) {
        loadConfig();
        FlatDarkLaf.setup();
        loadJson();
        Database.t.unpack_all();
        depthMap.runDepthCalc();
        GraphFrame.visualize();
    }
}