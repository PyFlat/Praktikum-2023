package org.example.data;
enum NODETYPE {BASIC, NODE,SUBPATH, SET}
public class NODE {
    public static TABLE<NODE> database;
    public NODETYPE type = NODETYPE.BASIC;
    private int index;
    public NODE(String name) {
        this.index = NODE.database.addElement(this, name);

    }

}
