package org.example.gui;

import javax.swing.*;
import java.awt.*;

public class PropertyDisplayFrame extends JFrame {
    private JPanel panel;

    public PropertyDisplayFrame() {
        setTitle("Property Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2)); // GridLayout with two columns

        addProperty("Name", "John Doe");
        addProperty("Age", "30");
        addProperty("Address", "123 Main St");

        // Add more properties as needed

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setLocationRelativeTo(null); // Center the frame
    }

    private void addProperty(String key, String value) {
        JLabel keyLabel = new JLabel(key);
        JTextField valueTextField = new JTextField(value);
        valueTextField.setEditable(false); // Make the text field read-only

        panel.add(keyLabel);
        panel.add(valueTextField);
    }

}