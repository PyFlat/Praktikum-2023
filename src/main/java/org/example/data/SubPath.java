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
        while (!this.names.isEmpty()) {
            this.childNodes.add(Database.t.getIndexByKey(this.names.remove(this.names.size()-1)));
            Database.t.getElement(this.childNodes.get(this.childNodes.size()-1)).addPathLoc(this, this.names.size());
        }
    }

    @Override
    public void cleanup() {
        this.names = null;
    }

    @Override
    public void calculateLength() {
        this.length = 1;
        this.childNodes.forEach((childIndex)->this.length += Database.t.getElement(childIndex).getLength());
    }

    @Override
    public ArrayList<Integer> getChildNodes() {
        return this.childNodes;
    }

    @Override
    public int getOpenEnds() {
        return Database.t.getElement(this.childNodes.get(0)).getOpenEnds();
    }
    public ArrayList<Integer> getCapacity() {return this.capacity;}
    public ArrayList<Float> getProbabilities() {return this.probabilities;}
    public TYPES getPathtype() {return this.pathtype;}
}
