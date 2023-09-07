package org.example.data;


public abstract class Node_abstract {
    private final int index;
    protected int length;

    public NODETYPE type;

    public Node_abstract(String name) {
        this.index = Database.t.addElement(this, name);
    }
    public abstract void unpack();
    public abstract void cleanup();

    public abstract int calculateLength();

    public int getLength() {
        if (length == 0) {calculateLength();}
        return length;
    }

    public String getName() {return Database.t.getKeyByIndex(index);}

    public int getIndex() {return index;}
}
