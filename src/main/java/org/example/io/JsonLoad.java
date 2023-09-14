package org.example.io;
import org.example.data.*;
import org.json.*;

import java.util.ArrayList;

public class JsonLoad {
    private static boolean isForbidden(String value) {
        return value.equals("PRG_END") || value.equals("PATHS") || value.equals("PRG_ROOT");
    }
    private static void legalCheck(String value) {
        if (isForbidden(value)) {
            throw new IllegalAccessError("CRITICAL: " + value + " is forbidden! Forbidden values are PRG_END, PATHS and PRG_ROOT");
        }
    }
    public static void loadFromJson(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            preLoad(jsonObject.getJSONArray("paths"));
            JSONArray subPaths = jsonObject.getJSONArray("subpaths");
            parseSubPaths(subPaths);
            JSONArray nodes = jsonObject.getJSONArray("nodes");
            parseNodes(nodes);
            JSONArray sets = jsonObject.getJSONArray("sets");
            parseSets(sets);

        }catch (JSONException e) {
            System.out.println("Failed to load json: Invalid json");
        }
    }
    private static void preLoad(JSONArray paths) {
        new Node("PRG_END");
        ArrayList<String> mainPathNames = new ArrayList<>();
        paths.forEach((path)->mainPathNames.add(((JSONObject)path).getString("name")));
        new Set("PATHS",PRIORITY.RANDOM,mainPathNames);
        ArrayList<String> names = new ArrayList<>();
        names.add("PATHS");
        names.add("PRG_END");
        ArrayList<Integer> priority = new ArrayList<>();
        priority.add(-1);
        priority.add(-1);
        ArrayList<Float> probability = new ArrayList<>();
        probability.add(1f);
        probability.add(1f);
        new SubPath("PRG_ROOT",names,probability, priority,TYPES.NORMAL);
    }
    private static void parseNodes(JSONArray nodes) {
        nodes.forEach((name)->{
            legalCheck(name.toString());
            new Node(name.toString());
        });
    }
    private static void parseSubPaths(JSONArray subPaths) {
        for (int i=0;i<subPaths.length();i++) {
            JSONObject subPath = subPaths.getJSONObject(i);
            String name = subPath.getString("name");
            legalCheck(name);
            final JSONArray jsonNames = subPath.getJSONArray("names");
            ArrayList<String> names = new ArrayList<>();
            jsonNames.forEach((jsonName)->names.add(jsonName.toString()));
            final JSONArray jsonProbabilities = subPath.getJSONArray("probs");
            ArrayList<Float> probabilities = new ArrayList<>();
            jsonProbabilities.forEach((jsonProbability)->probabilities.add((((java.math.BigDecimal) jsonProbability).floatValue())));
            final JSONArray jsonCapacities = subPath.getJSONArray("capacity");
            ArrayList<Integer> capacity = new ArrayList<>();
            jsonCapacities.forEach((jsonCapacity)->capacity.add((int) jsonCapacity));
            int typeIndex = subPath.getInt("type");
            TYPES[] typeList = new TYPES[] {TYPES.NORMAL,TYPES.RANDOM};
            new  SubPath(name,names,probabilities,capacity,typeList[typeIndex]);
        }
    }
    private static void parseSets(JSONArray sets) {
        for (int i=0;i<sets.length();i++) {
            JSONObject setJsonObject = sets.getJSONObject(i);
            String name = setJsonObject.getString("name");
            legalCheck(name);
            int priorityIndex = setJsonObject.getInt("priority");
            JSONArray jsonNames = setJsonObject.getJSONArray("names");
            ArrayList<String> names = new ArrayList<>();
            jsonNames.forEach((jsonName)->names.add((String) jsonName));
            PRIORITY[] priorityList = new PRIORITY[] {PRIORITY.CLOSEST, PRIORITY.FURTHEST, PRIORITY.SHORTEST_WAIT, PRIORITY.SHORTEST_CLOSEST_WAIT, PRIORITY.RANDOM, PRIORITY.RANDOM_EMPTY, PRIORITY.LOWEST_FREQ};
            new  Set(name,priorityList[priorityIndex],names);
        }
    }
}
