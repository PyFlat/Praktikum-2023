package org.example.data;
import java.util.ArrayList;
public class TABLE<T> {
    private ArrayList<T> elements;
    private ArrayList<String> names;
    public int addElement(T element, String name){
        if (this.names.contains(name)) {
            throw new ArrayIndexOutOfBoundsException("RIP");
        }
        this.names.add(name);
        this.elements.add(element);
        return this.elements.size() - 1;
    }
    public T getElement(int index){
        return this.elements.get(index);
    }
}
