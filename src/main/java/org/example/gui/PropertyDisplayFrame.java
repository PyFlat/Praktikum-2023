package org.example.gui;

import org.example.data.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.LINE_START;
import static org.example.gui.guiUtils.findNode;

public class PropertyDisplayFrame extends JFrame {
    private final JPanel panel;

    private int gridYLevel = 0;
    public static ArrayList<ArrayList<Node_abstract>> nodes;

    public static ArrayList<ArrayList<Object>> vertices;

    private String capitalize(String s) {return s.toUpperCase().charAt(0)+s.toLowerCase().substring(1);}
    public PropertyDisplayFrame(int x, int y, Object cell) {
        setSize(400, 200);
        setMinimumSize(getSize());

        setLocation(x,y);

        this.panel = new JPanel();
        this.panel.setLayout(new GridBagLayout());

        Node_abstract parent = findNode(cell,nodes,vertices);
        assert parent != null;
        this.addProperty(parent.type==NODETYPE.SUBPATH ? 4 : 2, "Name", parent.getName());
        this.addProperty(parent.type==NODETYPE.SUBPATH ? 4 : 2,"Type",capitalize(parent.type.name()));
        if (parent.type == NODETYPE.SET) {
            this.addProperty(2,"Priority", capitalize(((Set)parent).getPriority().name()));
            this.addProperty(2,"Children (" + ((advancedNode)parent).getChildNodes().size() + ")");
            ((advancedNode)parent).getChildNodes().forEach((c)->addProperty(2,Database.t.getElement(c).getName(),capitalize(Database.t.getElement(c).type.name())));
        } else if (parent.type == NODETYPE.SUBPATH) {
            this.addProperty(4,"Arrangement",capitalize(((SubPath)parent).getPathtype().name()));
            this.addProperty(4,"Children (" + ((advancedNode)parent).getChildNodes().size() + ")");
            this.addProperty(4,"Name","Type","Capacity","Probability");
            for (int i = 0; i< ((advancedNode)parent).getChildNodes().size();i++) {
                Node_abstract element = Database.t.getElement(((advancedNode)parent).getChildNodes().get(i));
                String name = element.getName();
                String type = capitalize(element.type.name());
                String probability = "" + ((SubPath)parent).getProbabilities().get(i);
                String capacity = "" + ((SubPath)parent).getCapacity().get(i);
                addProperty(4,name,type,capacity,probability);
            }
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        setLocationRelativeTo(null);
    }
    private void addProperty(int gridWidth, String ... data) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.gridwidth = gridWidth/data.length;
        constraints.anchor = LINE_START;
        constraints.gridy = gridYLevel;
        for (String value : data) {
            JTextField valueTextField = new JTextField(value);
            valueTextField.setHorizontalAlignment(SwingConstants.CENTER);
            ((GridBagLayout) panel.getLayout()).setConstraints(valueTextField,constraints);
            valueTextField.setEditable(false);
            panel.add(valueTextField);
            constraints.gridx += constraints.gridwidth;
        }
        this.gridYLevel += 1;
    }
}