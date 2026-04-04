## Acknowledgements

This project is based on the Snake game implementation by Jan Bodnar. <br>
Source: https://github.com/janbodnar/Java-Snake-Game

The original code is licensed under the BSD 2-Clause License (2020, Jan Bodnar). <br>
Modifications and additional features have been added in this project.

## Modification

### A "stopwatch" for the game, shows the time the snake survive when game over

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

### Make apple into a java class implementing Item interface
Interface: ```Item.java``` <br>
New Apple class: ```Apple.java``` <br>
```locateApple()``` would create the Apple object <br>
This makes it easier to add new items to the game