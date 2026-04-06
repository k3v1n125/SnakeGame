## Acknowledgements

This project is based on the Snake game implementation by Jan Bodnar. <br>
Source: https://github.com/janbodnar/Java-Snake-Game

The original code is licensed under the BSD 2-Clause License (2020, Jan Bodnar). <br>
Modifications and additional features have been added in this project.

## Running this program
Make sure you have java installed, then move to SnakeGame directory and run:
```bash
javac -cp src src/com/zetcode/*.java src/com/zetcode/Item/*.java src/com/zetcode/StatsBoard/*.java src/com/zetcode/ItemFactory/*.java
```
```bash
java -cp src com.zetcode.Snake
```

## Modification

### Table of Content

[Modification #1](#1-a-stopwatch-for-the-game-shows-the-time-the-snake-survive-when-game-over) <br>
[Modification #2](#2-make-apple-into-a-java-class-implementing-item-interface) <br>
[Modification #3](#3-score-calculated--shown-when-game-over) <br>
[Modification #4](#4-time-limit-for-apple) <br>
[Modification #5](#5-new-star-item) <br>
[Modification #6](#6-stats-board--bug-fix) <br>
[Modification #7](#7-game-pause--restart)

### 1. A "stopwatch" for the game, shows the time the snake survive when game over

Declear startTime when initUI() in Snake and pass it to Board
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

### 2. Make apple into a java class implementing Item interface
Interface: ```Item.java``` <br>
New Apple class: ```Apple.java``` <br>
```locateApple()``` would create the Apple object <br>
This makes it easier to add new items to the game

### 3. Score calculated & shown when game over
Get the ammount of apple collected by substracting length of snake by 3 (initial length) <br>
Calculated average time per apple

### 4. Time limit for apple
Apple would disappear after 5 seconds if not collected
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
After collecting 4 apples, stars will starts to appear <br>
For each 5 stars collected, the snake can have an extra life to avoid touching itself <br>
Items are now created by ItemFactory.java

### 6. Stats board & bug fix
Add a stats borad next to snake window to show current snake's stats <br>
Move all the stats (applesColledted, snakeLength, etc) into GameStats class <br>
Bug fix: add a ```moved``` boolean in Board class to avoid reversing into itself when two keys are press at the same time

### 7. Game pause & restart
When space is pressed, the entire game would pause <br>
When esc is pressed, the entire program would exit

### Expected next modifications: goals, new feature for specific amount of stars.
