# Minesweeper

Java Swing implementation of the traditional Minesweeper game.

## Classes

1. Block (+ SafeBlock, BombBlock)

    Stores the internal state of a block and provides instruction on how to deal with clicks + drawing the blocks.
2. ScoreData implements Comparable\<ScoreData>

    Simple data-structure to store the user and score of a high-score. This is created in order to have a comparison function to allow for the data to be sorted by the score, which makes viewing the high-scores in order much easier.
3. GameTimer extends JLabel

    Stores the internal clock of the game, updating every 500ms. Also allows for pause/play/reset
    functionality. Extends JLabel class because this allows it to directly be drawn on the frame through the JLabel.setText() function.
4. GameDataParser 

    Handles the I/O functionality of the game. Interacts with the rest of the game through its
    functions that check if a score is a new high-score, inserting a score into the dataset, and then saving the dataset to the txt
5. Grid extends JPanel

    Core functionality of game. Stores the blocks and the game state. Has checks if the game has been
    won/lost. Handles the interaction between the user and the grid (MouseClickListener + 
    paintComponent). 
6. Game implements Runnable

    Sets up the frame (with the GridBagLayout()). Splits functions into the homeScreen(), loadGame(),
    loadInstructions(), viewHighScores() screens. Also has multiple helper functions. Tick is a timer
    for the loadGame() screen. It updates the "mines left" label, changes the pause/play button function,
    and checks if the game has ended. There are also inputDialogs/messageDialogs for different endGame
    outcomes. 
    
    homeScreen() - main navigation screen. Has dropdown that lets you choose between difficulty levels. 

    loadGame() - Grid, reset button, back button, pause/play button

    viewHighScores() - JTable of scores generated from the GameDataParser.

    loadInstructions() - simple instructions page

7. Enums: BlockState, GameStatus, LeftClickResponse
        