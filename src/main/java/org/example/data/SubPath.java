package org.example.data;

import java.util.ArrayList;

public class SubPath extends Node_abstract implements advancedNode{

    private ArrayList<String> names;
    private final ArrayList<Float> probabilities;
    private final ArrayList<Integer> capacity;
    private final ArrayList<Integer> childNodes = new ArrayList<>();
    private final TYPES pathtype;

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
        while (!names.isEmpty()) {
            this.childNodes.add(Database.t.getIndexByKey(names.remove(names.size()-1)));
            Database.t.getElement(this.childNodes.get(childNodes.size()-1)).addPathLoc(this, names.size());
        }
    }

    @Override
    public void cleanup() {
        this.names = null;
    }

    @Override
    public void calculateLength() {
        length = 1;
        this.childNodes.forEach((e)->length += Database.t.getElement(e).getLength());
    }

    @Override
    public ArrayList<Integer> getChildNodes() {
        return childNodes;
    }

    @Override
    public int getOpenEnds() {
        return Database.t.getElement(childNodes.get(0)).getOpenEnds();
    }
    public ArrayList<Integer> getCapacity() {return capacity;}
    public ArrayList<Float> getProbabilities() {return probabilities;}
    public TYPES getPathtype() {return pathtype;}
}
