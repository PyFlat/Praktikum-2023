package org.example.data;


import java.util.HashMap;

public abstract class Node_abstract {
    private final int index;
    protected int length;

    public NODETYPE type;

    private final HashMap<Node_abstract , Integer> pathLocations;
    public Node_abstract(String name) {
        this.index = Database.t.addElement(this, name);
        this.pathLocations =new HashMap<>();
    }
    public abstract void unpack();
    public abstract void cleanup();

    public abstract void calculateLength();

    public int getLength() {
        if (this.length == 0) {calculateLength();}
        return this.length;
    }

    public String getName() {return Database.t.getKeyByIndex(this.index);}

    public int getPathLoc(Node_abstract path) {
        return this.pathLocations.get(path);
    }
    public void addPathLoc(Node_abstract path, int loc) {
        if (!this.pathLocations.containsKey(path)) {
            this.pathLocations.put(path,loc);
        }
    }
    public abstract int getOpenEnds();
}
