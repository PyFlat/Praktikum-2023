package org.example.gui;

import org.example.data.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.LINE_START;

public class PropertyDisplayFrame extends JFrame {
    private final JPanel panel;

    private int grid_iter = 0;
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
    private String capitalize(String s) {return s.toUpperCase().charAt(0)+s.toLowerCase().substring(1);}
    public PropertyDisplayFrame(int x, int y, Object cell) {
        setSize(400, 200);
        setMinimumSize(getSize());

        setLocation(x,y);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        Node_abstract parent = findNode(cell);
        assert parent != null;
        addProperty(parent.type==NODETYPE.SUBPATH ? 4 : 2, "Name", parent.getName());
        addProperty(parent.type==NODETYPE.SUBPATH ? 4 : 2,"Type",capitalize(parent.type.name()));
        if (parent.type == NODETYPE.SET) {
            addProperty(2,"Priority", capitalize(((Set)parent).getPriority().name()));
            addProperty(2,"Children (" + ((advancedNode)parent).getChildNodes().size() + ")");
            ((advancedNode)parent).getChildNodes().forEach((c)->addProperty(2,Database.t.getElement(c).getName(),capitalize(Database.t.getElement(c).type.name())));
        } else if (parent.type == NODETYPE.SUBPATH) {
            addProperty(4,"Arrangement",capitalize(((SubPath)parent).getPathtype().name()));
            addProperty(4,"Children (" + ((advancedNode)parent).getChildNodes().size() + ")");
            addProperty(4,"Name","Type","Capacity","Probability");
            for (int i = 0; i< ((advancedNode)parent).getChildNodes().size();i++) {
                Node_abstract element = Database.t.getElement(((advancedNode)parent).getChildNodes().get(i));
                String name = element.getName();
                String type = capitalize(element.type.name());
                String prob = "" + ((SubPath)parent).getProbabilities().get(i);
                String capa = "" + ((SubPath)parent).getCapacity().get(i);
                addProperty(4,name,type,capa,prob);
            }
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        setLocationRelativeTo(null);
    }
    private void addProperty(int gwidth, String ... data) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 1;
        c.gridwidth = gwidth/data.length;
        c.anchor = LINE_START;
        c.gridy = grid_iter;
        for (String val : data) {
            JTextField valueTextField = new JTextField(val);
            valueTextField.setHorizontalAlignment(SwingConstants.CENTER);
            ((GridBagLayout) panel.getLayout()).setConstraints(valueTextField,c);
            valueTextField.setEditable(false);
            panel.add(valueTextField);
            c.gridx += c.gridwidth;
        }
        newLine();
    }
    private void newLine() {
        grid_iter += 1;
    }

}