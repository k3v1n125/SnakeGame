package org.example;

import java.awt.EventQueue;
import javax.swing.JFrame;

import org.example.Achievement.AchievementBoard;
import org.example.Achievement.AchievementManager;
import org.example.HighScore.HighScoreManager;
import org.example.StatsBoard.GameStats;
import org.example.StatsBoard.StatsBoard;

import java.time.Instant;

public class Snake extends JFrame {
    
    public Snake() {
        
        initUI();
    }
    
    private void initUI() {

        Instant startTime = Instant.now();

        HighScoreManager highScoreManager = new HighScoreManager();
        AchievementManager achievementManager = new AchievementManager();

        GameStats gameStats = new GameStats(startTime, achievementManager, highScoreManager);
        StatsBoard statsBoard = new StatsBoard(gameStats);
        AchievementBoard achievementBoard = new AchievementBoard(achievementManager);
        Board board = new Board(this, statsBoard);

        achievementManager.setListener(achievementBoard);
        
        add(board);
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        statsBoard.setLocation(
            getX() + getWidth(),
            getY()
        );
        statsBoard.setVisible(true);

        achievementBoard.setLocation(
            getX() - getWidth() / 2,
            getY()
        );
        achievementBoard.setVisible(true);
    }

    public void restart() {
        dispose();
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
