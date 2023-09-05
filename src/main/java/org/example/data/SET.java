package org.example.data;

import java.util.ArrayList;

public class SET extends NODE {
    private ArrayList<Integer> childNodes = new ArrayList<Integer>();
    private ArrayList<String> names = new ArrayList<String>();
    private PRIORITY priority = PRIORITY.CLOSEST;
    public SET(String name, PRIORITY priority, ArrayList<String> names) {
        super(name);
        this.type = NODETYPE.SET;
        this.priority = priority;
        this.names = names;
    }
    @Override
    public void unpack() {
        while (names.size() > 0) {
            this.childNodes.add(NODE.database.getIndexByKey(names.remove(names.size()-1)));
        }
    }
    public void cleanup() {
        this.names = null;
    }
}
