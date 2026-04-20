## Acknowledgements

This project is based on the Snake game implementation by Jan Bodnar. <br>
Source: https://github.com/janbodnar/Java-Snake-Game

The original code is licensed under the BSD 2-Clause License (2020, Jan Bodnar). <br>
Modifications and additional features have been added in this project.

## Running this program
Make sure you have java installed, then move to SnakeGame directory and run: <br>
For MacOS/Linux
```bash
./gradlew run
```
For Windows
```bash
gradlew.bat run
```
For Android <br>
Download and install the apk <br>
[Link to SnakeGameAndroid repo](https://github.com/k3v1n125/SnakeGameAndroid)


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
[Modification #11](#11-winning-conditions-spawning-items--achievement-updates)

### 1. A "stopwatch" for the game, shows the time the snake survive when game over

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

### 2. Make apple into a java class implementing Item interface
Interface: ```Item.java``` <br>
New Apple class: ```Apple.java``` <br>
```locateApple()``` would create the Apple object <br>
This makes it easier to add new items to the game

### 3. Score calculated & shown when game over
Get the amount of apple collected by subtracting length of snake by 3 (initial length) <br>
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
After collecting 4 apples, stars will start to appear <br>
For each 5 stars collected, the snake can have an extra life to avoid touching itself <br>
Items are now created by ItemFactory.java

### 6. Stats board & bug fix
Add a stats board next to snake window to show current snake's stats <br>
Move all the stats (applesCollected, snakeLength, etc) into GameStats class <br>
StartTime is now recorded here <br>
Bug fix: add a ```moved``` boolean in Board class to avoid reversing into itself when two keys are pressed at the same time

### 7. Game pause & restart
When space is pressed, the entire game would pause <br>
When esc is pressed, the entire program would exit

### 8. More apples & formatted game time
For each 5 stars collected, there will be a new apple (at most 6 new apples) <br>
When game over, the time would be shown in minutes and seconds if game is more than 60 seconds

### 9. Achievement & highscore
This program now has two new features, achievement and highscore <br>
Currently, only length and stats related to apple would be recorded <br>
Next modification will aim on recording and giving achievement for stars and specific task during game

### 10. Modified achievement & highscore display on statsBoard and new achievements
The statsBoard would now display the longest time played <br>
The achievementBoard is now located at the left of snake window <br>
Added two new achievements, can be obtained by dodging apples for specific amount of time

### 11. Winning conditions, spawning items, & achievement updates
The game now has a winning condition, happens when the entire board is filled with the snake <br>
A checker for item spawning, this avoids items to spawn on snake body, and make sure that total amount of items spawned is not more than the space left <br>
Achievements now have categories, and the achievement board is scrollable

### Expected next modifications: new feature, new achievements
