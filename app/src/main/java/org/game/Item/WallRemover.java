package org.game.Item;

import java.awt.Image;

import java.time.Duration;
import java.time.Instant;

import javax.swing.ImageIcon;

import org.game.Board;
import org.game.ItemFactory.WallRemoverFactory;
import org.game.StatsBoard.GameStats;

public class WallRemover extends Item {
    private static final Image IMAGE = new ImageIcon(WallRemover.class.getResource("/wall_remover.png")).getImage();
    private Duration expireDuration = Duration.ofSeconds(3);
    private WallRemoverFactory factory;

    public WallRemover(int x, int y, Instant placedTime) {
        super(IMAGE, x, y, placedTime, 4);
        factory = new WallRemoverFactory();
    }

    @Override
    public void itemMissed(GameStats gameStats) {
        return;
    }

    @Override
    public void locateItem(Board board) {
        board.locateItem(factory);
    }

    @Override
    public void itemEffect(GameStats gameStats) {
        gameStats.setUseableHammer(0);
        gameStats.setHammerCollected(gameStats.getHammerCollected() - 10);
    }

    @Override
    public Duration getExpireDuration() {
        return expireDuration;
    }
}
