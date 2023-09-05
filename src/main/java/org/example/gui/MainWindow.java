package org.example.gui;

import org.example.Main;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow(){
        super("Just a test");

        getContentPane().setBackground(Color.WHITE);
        setSize(480, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void paint(Graphics g) {
        super.paint(g);

        new RECT(g, 30, 50, 80, 80);
    }
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
