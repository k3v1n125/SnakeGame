package org.game;

import java.awt.EventQueue;
import javax.swing.JFrame;

import org.game.Achievement.AchievementBoard;
import org.game.Achievement.AchievementManager;
import org.game.HighScore.HighScoreBoard;
import org.game.HighScore.HighScoreManager;
import org.game.StatsBoard.GameStats;
import org.game.StatsBoard.StatsBoard;

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
        HighScoreBoard highScoreBoard = new HighScoreBoard(highScoreManager);
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

        highScoreBoard.setLocation(
            statsBoard.getX(),
            statsBoard.getY() + statsBoard.getHeight()
        );
        highScoreBoard.setVisible(true);

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
