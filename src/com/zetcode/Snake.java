package com.zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;

import java.time.Instant;

public class Snake extends JFrame {

    public Snake() {
        
        initUI();
    }
    
    private void initUI() {

        Instant startTime = Instant.now();
        
        add(new Board(startTime));
               
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
