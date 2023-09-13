package org.example.io;
import org.example.data.*;
import org.json.*;

import java.util.ArrayList;

public class JsonLoad {
    private static boolean isForbidden(String value) {
        if (value.equals("PRG_END") || value.equals("PATHS")||value.equals("PRG_ROOT")) {
            return true;
        }
        return false;
    }
    private static void legalCheck(String value) {
        if (isForbidden(value)) {
            throw new IllegalAccessError("CRITICAL: " + value + " is forbidden! Forbidden values are PRG_END, PATHS and PRG_ROOT");
        }
    }
    public static void loadFromJson(String json) {

        try {
            JSONObject jo = new JSONObject(json);
            preLoad(jo.getJSONArray("paths"));
            JSONArray subPaths = jo.getJSONArray("subpaths");
            parseSubPaths(subPaths);
            JSONArray ns = jo.getJSONArray("nodes");
            parseNodes(ns);
            JSONArray st = jo.getJSONArray("sets");
            parseSets(st);

        }catch (JSONException e) {
            System.out.println("Failed to load json: Invalid json");
        }

    }
    private static void preLoad(JSONArray paths) {
        new Node("PRG_END");
        ArrayList<String> mainPathNames = new ArrayList<>();
        paths.forEach((p)->mainPathNames.add(((JSONObject)p).getString("name")));
        new Set("PATHS",PRIORITY.RANDOM,mainPathNames);
        ArrayList<String> names = new ArrayList<>();
        names.add("PATHS");names.add("PRG_END");
        ArrayList<Integer> priority = new ArrayList<>();
        priority.add(-1);priority.add(-1);
        ArrayList<Float> probability = new ArrayList<>();
        probability.add(1f);probability.add(1f);
        new SubPath("PRG_ROOT",names,probability, priority,TYPES.NORMAL);
    }
    private static void parseNodes(JSONArray nd) {
        nd.forEach((s)->{legalCheck(s.toString());new Node(s.toString());});
    }
    private static void parseSubPaths(JSONArray sp) {
        for (int i=0;i<sp.length();i++) {
            JSONObject e = sp.getJSONObject(i);
            String name = e.getString("name");
            JSONArray nam = e.getJSONArray("names");
            ArrayList<String> names = new ArrayList<>();
            nam.forEach((s)->names.add(s.toString()));
            nam = e.getJSONArray("probs");
            ArrayList<Float> probs = new ArrayList<>();
            nam.forEach((s)->probs.add((((java.math.BigDecimal) s).floatValue())));
            legalCheck(name);
            nam = e.getJSONArray("capacity");
            ArrayList<Integer> capacity = new ArrayList<>();
            nam.forEach((s)->capacity.add((int) s));

            int type = e.getInt("type");
            TYPES[] tl = new TYPES[] {TYPES.NORMAL,TYPES.RANDOM};
            SubPath p = new  SubPath(name,names,probs,capacity,tl[type]);
            int _i=0;
            while (p.getIndex() == -1) {
                p = new  SubPath(name + "(" + _i + ")",names,probs,capacity,tl[type]);
                _i += 1;
            }
        }
    }
    private static void parseSets(JSONArray st) {
        for (int i=0;i<st.length();i++) {
            JSONObject e = st.getJSONObject(i);
            String name = e.getString("name");
            legalCheck(name);
            int priority = e.getInt("priority");
            JSONArray _names = e.getJSONArray("names");
            ArrayList<String> names = new ArrayList<>();
            _names.forEach((s)->names.add((String) s));
            PRIORITY[] pts = new PRIORITY[] {PRIORITY.CLOSEST, PRIORITY.FURTHEST, PRIORITY.SHORTEST_WAIT, PRIORITY.SHORTEST_CLOSEST_WAIT, PRIORITY.RANDOM, PRIORITY.RANDOM_EMPTY, PRIORITY.LOWEST_FREQ};
            //new SET(name,pts[priority],names);
            Set p = new  Set(name,pts[priority],names);
            while (p.getIndex() == -1) {
                p = new  Set(name,pts[priority],names);
            }
        }
    }
}
