package org.example.gui;

import javax.swing.*;
import java.awt.*;

public class PropertyDisplayFrame extends JFrame {
    private final JPanel panel;


    public PropertyDisplayFrame(int x, int y, Object cell) {
        setTitle("Property Display");
        setSize(200, 100);
        setResizable(false);
        setLocation(x,y);

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setLocationRelativeTo(null);
    }
    private void addProperty(String key, String value) {
        JLabel keyLabel = new JLabel(key, JLabel.LEFT);
        JTextField valueTextField = new JTextField(value);
        valueTextField.setEditable(false);

        panel.add(keyLabel);
        panel.add(valueTextField);
    }

}