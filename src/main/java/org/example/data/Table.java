package org.example.data;

import java.util.HashMap;
import java.util.Map;

public class Table {
    private final HashMap<String , Integer> keyMap;
    private final HashMap<Integer , Node_abstract> nodes;
    private final HashMap<Integer , String> indexMap;

    private int counter;
    public Table() {
        this.keyMap = new HashMap<>();
        this.nodes = new HashMap<>();
        this.indexMap = new HashMap<>();
        this.counter = -1;
    }
    public int addElement(Node_abstract element, String name) {
        if (this.keyMap.containsKey(name)) {
            return -1;
        }
        this.counter += 1;
        this.keyMap.put(name, this.counter);
        this.nodes.put(this.counter, element);
        this.indexMap.put(this.counter, name);
        return this.counter;
    }
    public Node_abstract getElement(int index) {
        return this.nodes.get(index);
    }
    public int getIndexByKey(String key) {
        return this.keyMap.get(key);
    }
    public Node_abstract getElementByKey(String key) {
        return this.nodes.get(getIndexByKey(key));
    }
    public String getKeyByIndex(int index) {
        return this.indexMap.get(index);
    }
    public HashMap<Integer , Node_abstract> getNodes() {
        return this.nodes;
    }

    public void unpack_all() {
        System.out.println("[LOG] Unpack started");
        for (Map.Entry<Integer , Node_abstract> entry : this.nodes.entrySet()) {
            entry.getValue().unpack();
            entry.getValue().cleanup();
        }
        System.out.println("[LOG] Unpack completed");
    }
}
