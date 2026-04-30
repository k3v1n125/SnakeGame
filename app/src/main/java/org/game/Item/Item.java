package org.game.Item;

import java.awt.Graphics;
import java.awt.Image;
import java.time.Duration;
import java.time.Instant;

import org.game.Board;
import org.game.StatsBoard.GameStats;

public abstract class Item {
    private final int DOT_SIZE = 20;
    private Image image;
    private int x;
    private int y;
    private Instant placedTime;
    private Duration pauseDuration = Duration.ZERO;
    private Instant activePauseStart = null;
    private final int spawnPriority;
    
    public Item(Image image, int x, int y, Instant placedTime, int spawnPriority) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.placedTime = placedTime;
        this.spawnPriority = spawnPriority;
    }

    public int getSpawnPriority() {
        return spawnPriority;
    }

    public void draw(Graphics g, Board board) {
        g.drawImage(image, x, y, DOT_SIZE, DOT_SIZE, board);
    }

    protected void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Instant getPlacedTime() {
        return placedTime;
    }

    public Duration existDuration() {
        Duration elapsed = Duration.between(placedTime, Instant.now()).minus(pauseDuration);
        if (activePauseStart != null) {
            elapsed = elapsed.minus(Duration.between(activePauseStart, Instant.now()));
        }
        return elapsed;
    }

    public void startPause() {
        if (activePauseStart == null) {
            activePauseStart = Instant.now();
        }
    }

    public void setPauseDuration(Duration pauseDuration) {
        this.pauseDuration = this.pauseDuration.plus(pauseDuration);
        this.activePauseStart = null;
    }

    public abstract void itemMissed(GameStats gameStats);

    public abstract void locateItem(Board board);
    public abstract void itemEffect(GameStats gameStats);
    public abstract Duration getExpireDuration();
}
