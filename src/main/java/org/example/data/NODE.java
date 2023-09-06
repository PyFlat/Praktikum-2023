package org.example.data;

public class NODE implements NODE_LIKE{
    public static TABLE database = new TABLE();
    public NODETYPE type = NODETYPE.BASIC;
    private int index;
    protected int length;

    public NODE(String name) {
        this.index = NODE.database.addElement(this, name);

    }
    public void unpack() {};
    public void cleanup() {}

    @Override
    public int calculateLength() {
        length = 1;
        return 1;
    }

    @Override
    public int getLength() {
        return 1;
    }

    public String getName() {
        return NODE.database.getKeyByIndex(this.index);
    }

    public int getIndex() {
        return index;
    }
}
