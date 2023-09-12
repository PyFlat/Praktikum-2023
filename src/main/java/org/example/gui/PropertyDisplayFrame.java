package org.example.gui;

import org.example.data.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PropertyDisplayFrame extends JFrame {
    private final JPanel panel;

    public static ArrayList<ArrayList<Node_abstract>> nodes;

    public static ArrayList<ArrayList<Object>> vertices;

    private Node_abstract findNode(Object cell) {
        int[] d = findCoords(cell);
        if (d == null) return null;
        try {
            return nodes.get(d[0]).get(d[1]);
        } catch (NullPointerException e) {
            return null;
        }
    }
    private int[] findCoords(Object cell) {
        for (int x = 0; x < vertices.size(); x++) {
            for (int y = 0; y < vertices.get(x).size(); y++) {
                if (vertices.get(x).get(y).equals(cell)) {
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }
    private String capitalize(String s) {return s.toUpperCase().substring(0,1)+s.toLowerCase().substring(1);}
    public PropertyDisplayFrame(int x, int y, Object cell) {
        setSize(200, 100);
        //setResizable(false);
        setLocation(x,y);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        Node_abstract parent = findNode(cell);
        addProperty(2,"Name",parent.getName());

        switch (parent.type) {
            case BASIC:
                break;
            case SET:
                addProperty(2,"Type",capitalize(parent.type.name()));
                addProperty(2,"Priority", capitalize(((Set)parent).getPriority().name()));
                addProperty(2,"Children", "Amount: " + ((advancedNode)parent).getChildNodes().size());
                ((advancedNode)parent).getChildNodes().forEach((c)->addProperty(2,Database.t.getElement(c).getName(),capitalize(Database.t.getElement(c).type.name())));
                break;
            case SUBPATH:
                panel.setLayout(new GridBagLayout());
                addProperty(2,"Type",capitalize(parent.type.name()));
                addProperty(2,"Arrangement",capitalize(((SubPath)parent).getPathtype().name()));
                addProperty(2,"Children", "Amount: " + ((advancedNode)parent).getChildNodes().size());
                addProperty(4,"Name","Type","Capacity","Probability");
                for (int i = 0; i< ((advancedNode)parent).getChildNodes().size();i++) {
                    Node_abstract element = Database.t.getElement(((advancedNode)parent).getChildNodes().get(i));
                    String name = element.getName();
                    String type = capitalize(element.type.name());
                    String prob = "" + ((SubPath)parent).getProbabilities().get(i);
                    String capa = "" + ((SubPath)parent).getCapacity().get(i);
                    addProperty(4,name,type,capa,prob);
                }
                break;
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setLocationRelativeTo(null);
    }
    private void addProperty(int gwidth, String ... data) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = -1;
        c.weightx = 1;
        c.gridwidth = 1;
        for (String val : data) {
            c.gridx += 1;
            JTextField valueTextField = new JTextField(val);
            ((GridBagLayout) panel.getLayout()).setConstraints(valueTextField,c);
            panel.add(valueTextField);
        }
    }

}