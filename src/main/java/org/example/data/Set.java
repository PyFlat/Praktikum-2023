package org.example.data;

import java.util.ArrayList;

public class Set extends Node_abstract implements advancedNode{

    private final ArrayList<Integer> childNodes = new ArrayList<>();
    private ArrayList<String> names;
    private final PRIORITY priority;

    public Set(String name, PRIORITY priority, ArrayList<String> names) {
        super(name);
        this.type = NODETYPE.SET;
        this.names = names;
        this.priority = priority;
    }
    public PRIORITY getPriority() {
        return this.priority;
    }
    @Override
    public void unpack() {
        while (!this.names.isEmpty()) {
            this.childNodes.add(Database.t.getIndexByKey(this.names.remove(this.names.size()-1)));
        }
    }

    @Override
    public void cleanup() {
        this.names = null;
    }

    @Override
    public void calculateLength() {
        this.length = 1;
        this.childNodes.forEach((e)->isMaxLength(Database.t.getElement(e).getLength()));
    }
    private void isMaxLength(int newLength) {
        if (newLength+1 > this.length) {
            this.length = newLength+1;
        }
    }
    @Override
    public ArrayList<Integer> getChildNodes() {
        return this.childNodes;
    }

    @Override
    public int getOpenEnds() {
        int ends = 0;
        for (int childIndex: this.childNodes) {
            Node_abstract child = Database.t.getElement(childIndex);
            ends += child.getOpenEnds();
        }
        return ends;
    }
}
