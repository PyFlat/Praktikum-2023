package org.example.data;

public class Node extends Node_abstract{

    public Node(String name) {
        super(name);
        this.type = NODETYPE.BASIC;
    }

    @Override
    public void unpack() {}

    @Override
    public void cleanup() {}

    @Override
    public void calculateLength() {
        length = 1;
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public int getOpenEnds() {
        return 1;
    }
}
