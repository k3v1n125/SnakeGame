package com.zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;

import com.zetcode.StatsBoard.GameStats;
import com.zetcode.StatsBoard.StatsBoard;

import java.time.Instant;

public class Snake extends JFrame {

    // private GameStats gameStats;

    public Snake() {
        
        initUI();
    }
    
    private void initUI() {

        Instant startTime = Instant.now();

        GameStats gameStats = new GameStats(startTime);
        StatsBoard statsBoard = new StatsBoard(gameStats);
        Board board = new Board(this, statsBoard);
        
        add(board);
               
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        statsBoard.setLocation(
            getX() + getWidth() + 10,
            getY()
        );
        statsBoard.setVisible(true);
    }

    public void restart() {
        dispose();
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }

    public void end() {
        dispose();
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
