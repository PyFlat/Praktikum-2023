package org.example.data;

import java.util.ArrayList;

public class SUBPATH extends NODE {
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<Float> probabilities = new ArrayList<Float>();
    private ArrayList<Integer> capacity = new ArrayList<Integer>();
    private ArrayList<Integer> childNodes =  new ArrayList<Integer>();
    private TYPES pathtype = TYPES.NORMAL;
    public SUBPATH(String name, ArrayList<String> names, ArrayList<Float> probabilities, ArrayList<Integer> capacity, TYPES type) {
        super(name);
        this.type = NODETYPE.SUBPATH;
        this.capacity = capacity;
        this.names = names;
        this.probabilities = probabilities;
        this.pathtype = type;
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

    @Override
    public int calculateLength() {
        length = 1;
        this.childNodes.forEach((e)->length += NODE.database.getElement(e).getLength());
        return length;
    }
    public ArrayList<Integer> getChildNodes() {
        return childNodes;
    }
    @Override
    public int getLength() {
        if (length == 0) {
            calculateLength();
        }
        return length;
    }
}
