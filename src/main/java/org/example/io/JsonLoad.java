package org.example.io;
import org.example.data.*;
import org.json.*;

import java.util.ArrayList;
import java.util.Arrays;

public class JsonLoad {
    public static void loadFromJson(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            JSONArray spfs = jo.getJSONArray("subpaths");
            parseSubPaths(spfs);
            JSONArray ns = jo.getJSONArray("nodes");
            parseNodes(ns);
            JSONArray st = jo.getJSONArray("sets");
            parseSets(st);

        }catch (JSONException e) {
            System.out.println("Failed to load json: Invalid json");
        }

    }
    private static void parseNodes(JSONArray nd) {
        nd.forEach((s)->new NODE(s.toString()));
    }
    private static void parseSubPaths(JSONArray sp) {
        for (int i=0;i<sp.length();i++) {
            JSONObject e = sp.getJSONObject(i);
            String name = e.getString("name");
            JSONArray nam = e.getJSONArray("names");
            ArrayList<String> names = new ArrayList<String>();
            nam.forEach((s)->names.add(s.toString()));
            nam = e.getJSONArray("probs");
            ArrayList<Float> probs = new ArrayList<Float>();
            nam.forEach((s)->probs.add((((java.math.BigDecimal) s).floatValue())));

            nam = e.getJSONArray("capacity");
            ArrayList<Integer> capc = new ArrayList<Integer>();
            nam.forEach((s)->capc.add((int) s));

            int type = e.getInt("type");
            TYPES[] tl = new TYPES[] {TYPES.NORMAL,TYPES.RANDOM};
            new SUBPATH(name,names,probs,capc,tl[type]);
        }
    }
    private static void parseSets(JSONArray st) {
        for (int i=0;i<st.length();i++) {
            JSONObject e = st.getJSONObject(i);
            String name = e.getString("name");
            int priority = e.getInt("priority");
            JSONArray _names = e.getJSONArray("names");
            ArrayList<String> names = new ArrayList<String>();
            _names.forEach((s)->names.add((String) s));
            PRIORITY[] pts = new PRIORITY[] {PRIORITY.CLOSEST, PRIORITY.FURTHEST, PRIORITY.SHORTEST_WAIT, PRIORITY.SHORTEST_CLOSEST_WAIT, PRIORITY.RANDOM, PRIORITY.RANDOM_EMPTY, PRIORITY.LOWEST_FREQ};
            new SET(name,pts[priority],names);
        }
    }
}
