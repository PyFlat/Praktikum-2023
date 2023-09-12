package org.example.data;

import java.util.ArrayList;

public class SubPath extends Node_abstract implements advancedNode{

    private ArrayList<String> names;
    private final ArrayList<Float> probabilities;
    private final ArrayList<Integer> capacity;
    private final ArrayList<Integer> childNodes = new ArrayList<>();
    private TYPES pathtype;

    public SubPath(String name, ArrayList<String> names, ArrayList<Float> probabilities, ArrayList<Integer> capacity, TYPES type) {
        super(name);
        this.type = NODETYPE.SUBPATH;
        this.capacity = capacity;
        this.names = names;
        this.probabilities = probabilities;
        this.pathtype = type;
    }

    @Override
    public void unpack() {
        int i = 0;
        while (!names.isEmpty()) {
            this.childNodes.add(Database.t.getIndexByKey(names.remove(names.size()-1)));
            Database.t.getElement(this.childNodes.get(childNodes.size()-1)).addPathLoc(this, names.size());
            i += 1;
        }
    }

    @Override
    public void cleanup() {
        this.names = null;
    }

    @Override
    public int calculateLength() {
        length = 1;
        this.childNodes.forEach((e)->length += Database.t.getElement(e).getLength());
        return length;
    }

    @Override
    public ArrayList<Integer> getChildNodes() {
        return childNodes;
    }

    @Override
    public int getOpenEnds() {
        if (Database.t.getElement(childNodes.get(childNodes.size()-1)).type == NODETYPE.BASIC) {
            return 1;
        } else {
            return ((advancedNode) Database.t.getElement(childNodes.get(childNodes.size()-1))).getOpenEnds();
        }
    }
    public ArrayList<Integer> getCapacity() {return capacity;}
    public ArrayList<Float> getProbabilities() {return probabilities;}
    public TYPES getPathtype() {return pathtype;}
}
