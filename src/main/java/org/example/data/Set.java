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
        return priority;
    }
    @Override
    public void unpack() {
        while (!names.isEmpty()) {
            this.childNodes.add(Database.t.getIndexByKey(names.remove(names.size()-1)));
        }
    }

    @Override
    public void cleanup() {
        this.names = null;
    }

    @Override
    public void calculateLength() {
        length = 1;
        this.childNodes.forEach((e)->isMaxLength(Database.t.getElement(e).getLength()));
    }
    private void isMaxLength(int l) {
        if (l+1 > length) {
            length = l+1;
        }
    }
    @Override
    public ArrayList<Integer> getChildNodes() {
        return childNodes;
    }

    @Override
    public int getOpenEnds() {
        int ends = 0;
        for (int i: childNodes) {
            Node_abstract n = Database.t.getElement(i);
            ends += n.getOpenEnds();
        }
        return ends;
    }
}
