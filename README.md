## Acknowledgements

This project is based on the Snake game implementation by Jan Bodnar. <br>
Source: https://github.com/janbodnar/Java-Snake-Game

The original code is licensed under the BSD 2-Clause License (2020, Jan Bodnar). <br>
Modifications and additional features have been added in this project.

## Running this program
For MacOS <br>
Unzip macos-arm64 (or macos-x64 for Mac with Intel), then double-click SnakeGame in the extracted folder in Finder

Alternatively, make sure you have Java installed, then move to the SnakeGame directory and run:
```bash
./gradlew run
```

For Windows <br>
Make sure you have Java installed, then move to the SnakeGame directory and run:
```bash
gradlew.bat run
```

For Android <br>
Download and install the apk <br>
[Link to SnakeGameAndroid repo](https://github.com/k3v1n125/SnakeGameAndroid)

For iOS <br>
Visit this [Repository](https://github.com/k3v1n125/SnakeGameiOS)

## Modification

### Table of Content

[Modification #1](#1-a-stopwatch-for-the-game-shows-the-time-the-snake-survive-when-game-over) <br>
[Modification #2](#2-make-apple-into-a-java-class-implementing-item-interface) <br>
[Modification #3](#3-score-calculated--shown-when-game-over) <br>
[Modification #4](#4-time-limit-for-apple) <br>
[Modification #5](#5-new-star-item) <br>
[Modification #6](#6-stats-board--bug-fix) <br>
[Modification #7](#7-game-pause--restart) <br>
[Modification #8](#8-more-apples--formatted-game-time) <br>
[Modification #9](#9-achievement--highscore) <br>
[Modification #10](#10-modified-achievement--highscore-display-on-statsboard-and-new-achievements) <br>
[Modification #11](#11-winning-conditions-spawning-items--achievement-updates) <br>
[Modification #12](#12-items-status) <br>
[Modification #13](#13-new-pineapple-item--bug-fix) <br>
[Modification #14](#14-modification--improvements) <br>
[Modification #15](#15-wall--hammer) <br>
[Modification #16](#16-wall-remover)

### 1. A "stopwatch" for the game, shows the time the snake survived when the game is over

Declare startTime when initUI() in Snake and pass it to Board
```bash
Instant startTime = Instant.now();
add(new Board(startTime));
```
New Board constructor
```bash
private Instant startTime; // new field
private Instant endTime; // new field

public Board(Instant startTime) {
    this.startTime = startTime;
    initBoard();
}
```
Get endTime on gameOver
```bash
private void gameOver(Graphics g) {
    endTime = Instant.now(); // added
    Duration duration = Duration.between(startTime, endTime); // added
        
    String msg1 = "Game Over";
    String msg2 = "Duration: " + duration.toSeconds() + " seconds"; // added
    Font small = new Font("Helvetica", Font.BOLD, 14);
    FontMetrics metr = getFontMetrics(small);

    g.setColor(Color.white);
    g.setFont(small);
    g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, B_HEIGHT / 2 - 20); // modified game over window
    g.drawString(msg2, (B_WIDTH - metr.stringWidth(msg2)) / 2, B_HEIGHT / 2 + 20);
}
```

### 2. Make Apple into a Java class implementing Item interface
Interface: ```Item.java``` <br>
New Apple class: ```Apple.java``` <br>
```locateApple()``` would create the Apple object <br>
This makes it easier to add new items to the game

### 3. Score calculated & shown when game over
Get the amount of apples collected by subtracting the length of the snake by 3 (initial length) <br>
Calculated average time per apple

### 4. Time limit for apple
Apples would disappear after 5 seconds if not collected
```bash
public void actionPerformed(ActionEvent e) {
    if (inGame) {
        if (appleItem != null && Duration.between(appleItem.getPlacedTime(), Instant.now()).getSeconds() >= 5) {
            appleItem = null;
            appleMissed = appleMissed + 1;
            locateApple();
        }

        checkApple();
        checkCollision();
        move();
    }

    repaint();
}
```

### 5. New star item
After collecting 4 apples, stars will start to appear <br>
For each 5 stars collected, the snake can have an extra life to avoid touching itself <br>
Items are now created by ItemFactory.java

### 6. Stats board & bug fix
Add a stats board next to snake window to show current snake's stats <br>
Move all the stats (applesCollected, snakeLength, etc.) into the GameStats class <br>
startTime is now recorded here <br>
Bug fix: add a ```moved``` boolean in Board class to avoid reversing into itself when two keys are pressed at the same time

### 7. Game pause & restart
When space is pressed, the entire game would pause <br>
When esc is pressed, the entire program would exit

### 8. More apples & formatted game time
For each 5 stars collected, there will be a new apple (at most 6 new apples) <br>
When the game is over, the time would be shown in minutes and seconds if the game lasted more than 60 seconds

### 9. Achievement & highscore
This program now has two new features, achievement and highscore <br>
Currently, only length and stats related to apples would be recorded <br>
The next modification will aim at recording and giving achievements for stars and specific tasks during the game

### 10. Modified achievement & highscore display on statsBoard and new achievements
The statsBoard would now display the longest time played <br>
The achievementBoard is now located at the left of the snake window <br>
Added two new achievements, which can be obtained by dodging apples for a specific amount of time

### 11. Winning conditions, spawning items, & achievement updates
The game now has a winning condition, which happens when the entire board is filled with the snake <br>
A checker for item spawning; this avoids items spawning on the snake body, and makes sure that the total amount of items spawned is not more than the space left <br>
Achievements now have categories, and the achievement board is scrollable

### 12. Items status
Each item has a different color when it has already passed 1/3 and 2/3 of its expiry time

### 13. New pineapple item & bug fix
A new item, pineapple, will start to appear after collecting 25 stars <br>
When a pineapple is collected, the length of the snake will decrease by 1 <br>
This can make the duration of the game longer by avoiding the winning status, and the player can collect more items in one game <br>
Bug fix: fixed item status changing when the game is paused

### 14. Modification & improvements
Use squares instead of circles for the snake <br>
Move highscore into another panel <br>
When the game ends, keep the game stats window and the game board would only show the game end message <br>
Modified gameStats, showing "Locked" when an item is not available yet <br>
Use a direction queue to handle rapid key tapping

### 15. Wall & hammer
For each 5 stars missed, that star would turn into a wall, and hammers would start to spawn <br>
A wall can be broken through with 5 hammers; if there are not enough hammers, the snake's lives decrease <br>
Modified items spawning priority and itemMissed methods

### 16. Wall remover
When 10 hammers are collected, a new item, the wall remover, will appear. <br>
When a wall remover is collected, all the walls and hammers in the game board will be removed, and usable hammer count will drop to 0, and hammerCollected will decrease by 10

### Expected next modifications: new feature, new achievements, penalty for item missed
