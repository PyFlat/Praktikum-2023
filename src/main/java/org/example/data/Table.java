package org.example.data;

import java.util.HashMap;
import java.util.Map;

public class Table {
    private final HashMap<String , Integer> keyMap;
    private final HashMap<Integer , Node_abstract> nodes;
    private final HashMap<Integer , String> indexMap;

    private int counter;
    public Table() {
        keyMap = new HashMap<>();
        nodes = new HashMap<>();
        indexMap = new HashMap<>();
        counter = -1;
    }
    public int addElement(Node_abstract element, String name) {
        if (keyMap.containsKey(name)) {
            return -1;
        }
        counter += 1;
        keyMap.put(name, counter);
        nodes.put(counter, element);
        indexMap.put(counter, name);
        return counter;
    }
    public Node_abstract getElement(int index) {
        return nodes.get(index);
    }
    public int getIndexByKey(String key) {
        return keyMap.get(key);
    }
    public Node_abstract getElementByKey(String key) {
        return nodes.get(getIndexByKey(key));
    }
    public String getKeyByIndex(int index) {
        return indexMap.get(index);
    }
    public void debug() {
        //TODO code debug function
    }
    public HashMap<Integer , Node_abstract> getNodes() {
        return nodes;
    }

    public void unpack_all() {
        for (Map.Entry<Integer , Node_abstract> entry : nodes.entrySet()) {
            entry.getValue().unpack();
            entry.getValue().cleanup();
        }
    }
}
