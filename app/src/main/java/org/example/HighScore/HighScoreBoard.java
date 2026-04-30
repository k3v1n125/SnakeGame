package org.example.HighScore;

import javax.swing.JFrame;

public class HighScoreBoard extends JFrame {
    private final HighScoreStats stats;

    public HighScoreBoard(HighScoreManager highScoreManager) {
        setTitle("High Scores");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        stats = new HighScoreStats(highScoreManager);
        add(stats);

        pack();
    }
}