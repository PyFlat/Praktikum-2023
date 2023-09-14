package org.example.data;


import java.util.HashMap;

public abstract class Node_abstract {
    private final int index;
    protected int length;

    public NODETYPE type;

    private final HashMap<Node_abstract , Integer> pathlocations;
    public Node_abstract(String name) {
        this.index = Database.t.addElement(this, name);pathlocations=new HashMap<>();
    }
    public abstract void unpack();
    public abstract void cleanup();

    public abstract void calculateLength();

    public int getLength() {
        if (length == 0) {calculateLength();}
        return length;
    }

    public String getName() {return Database.t.getKeyByIndex(index);}

    public int getIndex() {return index;}

    public int getPathLoc(Node_abstract path) {
        return pathlocations.get(path);
    }
    public void addPathLoc(Node_abstract path, int loc) {
        if (!pathlocations.containsKey(path)) {
            pathlocations.put(path,loc);
        }
    }
    public abstract int getOpenEnds();
}
