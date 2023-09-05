package org.example.data;
enum NODETYPE {BASIC, NODE,SUBPATH, SET}
public class NODE implements NODE_LIKE{
    public static TABLE database = new TABLE();
    public NODETYPE type = NODETYPE.BASIC;
    private int index;
    public NODE(String name) {
        this.index = NODE.database.addElement(this, name);

    }
    public void unpack() {};
    public void cleanup() {}
}
