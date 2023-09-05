package org.example.gui;

import org.example.Main;
import org.example.gui.canvasitems.*;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame {
    private Rect a;
    private Oval b;
    private Text c;
    public MainWindow(){
        super("Just a test");
        a = new Rect(30,30,30,30, 10, 10);
        b = new Oval(50, 50, 100, 100);
        c = new Text(50, 100, "HALLO WELT");
        getContentPane().setBackground(Color.WHITE);
        setSize(480, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        this.a.paint(g2d);
        this.b.paint(g2d);
        this.c.paint(g2d);
    }
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
