package org.example.data;
import java.util.ArrayList;
import java.util.Arrays;

public class TABLE {
    private ArrayList<NODE_LIKE> elements = new ArrayList<NODE_LIKE>();
    private ArrayList<String> names = new ArrayList<String>();
    public int addElement(NODE_LIKE element, String name){
        if (this.names.contains(name)) {
           return -1;
        }
        this.names.add(name);
        this.elements.add(element);
        return this.elements.size() - 1;
    }
    public NODE_LIKE getElement(int index){
        return this.elements.get(index);
    }
    public int getIndexByKey(String key) {
        for (int i=0;i<this.names.size();i++) {
            if (this.names.get(i).equals(key)) {return i;}
        }
        System.out.println("WARNING! KEY NOT FOUND " + key);
        return -1;
    }
    public NODE_LIKE getElementByKey(String key) {
        return this.getElement(this.getIndexByKey(key));
    }
    public String getKeyByIndex(int index) {return this.names.get(index);}

    public void unpack_all() {
        elements.forEach((e)->e.unpack());
        elements.forEach((e)->e.cleanup());
    }
    public void debug() {
        System.out.println(Arrays.toString(elements.toArray()));
        System.out.println(Arrays.toString(names.toArray()));
    }
    public static TABLE getTable() {return NODE.database;}
    public ArrayList<NODE_LIKE> getElements() {return elements;}
}
