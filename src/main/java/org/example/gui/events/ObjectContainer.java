package org.example.gui.events;

public class ObjectContainer<T> {
    private T content;
    public ObjectContainer() {}

    public void set(T newObject) {this.content=newObject;}
    public T get() {return this.content;}
}
