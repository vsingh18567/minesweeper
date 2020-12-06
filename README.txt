=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: 40121920
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2-D Arrays
        The gamespace is stored in Grid as a 2D array of the Block class. Storing this data in
        a 2D array is the easiest way to access the data, because it mimics the row/column
        nature of how the Blocks are presented on the screen. In various situations, the 2D
        array data structure allows for easy traversal through the blocks by having a double
        for-loop, which is especially valuable in finding the number of neighbouring bombs
        for a particular block. 

  2. File I/O
        Unlike what was stated in the proposal, there is no "load Game" functionality, and this
        has been replaced by a "View High Scores" section.
  
        Highscores are stored in a persistent file under "files/highscores.txt". To access and 
        edit this file, I/O is used through the BufferedReader/BufferedWriter classes. It is
        important that this data is stored in a manner which allows for the maximum number
        of characters to be used in defining your "username". Therefore, the only protected
        characters are "_" and " ". Each score is separated by " ", and the actual data is
        stored as "name_score". There are always exactly 30 scores stored - the top 10 for
        each difficulty level. 
            
        The I/O functionality is handled by the GameDataParser (gdp) class. 
        
        Read: gdp.loadData() parses the single line in the txt file, and uses the rules described
        above to convert the string into three ArrayList<ScoreData>, one for each difficulty.
        
        Write: gdp.saveData() overwrites the line in the txt by converting the ArrayList<ScoreData>
        into a string using the rules described above. 
        
        Handles IOException with a default string. 

  3. Inheritance/Subtyping
        The array stores the Block class, but the Block class is an abstract class, which is
        extended by SafeBlock and BombBlock. The Block class stores the key attributes of a block:
            1. BlockState state
            2. int x
            3. int y
            4. int width
            5. int numberOfNeighbours
        This is relevant to both SafeBlock and BombBlock, as are the getter and setter methods for
        the various attributes. There is one exception to this. BombBlock overrides setNeighbours()
        and getNeighbours(), because a BombBlock is itself a bomb, so its number of bomb neighbours
        is irrelevant. The right-click function is also shared between Bomb & Safe, as either way,
        all that changes is the BlockState to/from FLAGGED. 
        
        On the other hand, left-click and draw are obviously different for the two different types
        of blocks, and therefore they're implemented in the child classes. There's dynamic dispatch
        in the Grid.generateNeighbours() function, and in the MouseListener with regards to left-click.
        
        Further, having SafeBlock and BombBlock inherit from Block allows for both of them to be stored
        together in the Block[][] 2D-array. This is implemented in Grid.floodFill().

  4. Recursion
        This is evident in the Grid.floodFill() algorithm. Consider a block with 0 bomb neighbours. When 
        it is clicked, every neighbouring block should automatically be discovered, because it is obvious
        that it is not a bomb. But then if one of those blocks also has 0 bomb neighbours, then that 
        block's neighbours also need to be discovered. This is evidently a recursive algorithm, and 
        continues until you hit a block which does not have 0 bomb neighbours. It searches through the 
        neighbours using the private final static int[][] neighbours, which makes it easy to access 
        adjacent blocks. 

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
        1. Block (+ SafeBlock, BombBlock)
            Stores the internal state of a block and provides instruction on how to deal with clicks + 
            drawing the blocks.
        2. ScoreData implements Comparable<ScoreData>
            Simple data-structure to store the user and score of a high-score. This is created in order to 
            have a comparison function to allow for the data to be sorted by the score, which makes viewing
            the high-scores in order much easier.
        3. GameTimer extends JLabel
            Stores the internal clock of the game, updating every 500ms. Also allows for pause/play/reset
            functionality. Extends JLabel class because this allows it to directly be drawn on the frame 
            through the JLabel.setText() function.
        4. GameDataParser 
            Handles the I/O functionality of the game. Interacts with the rest of the game through its
            functions that check if a score is a new high-score, inserting a score into the dataset, and
            then saving the dataset to the txt
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
            


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
        
        The load game feature was getting tedious to implement because it made the Grid class extremely messy, as
        it needed two completely different constructors, and for some reason the blocks were not getting 
        painted properly. After a lot of time spent trying to debug, I made the decision to drop the idea and switch
        to HighScores. 
        
        It also took a while to decide on a data-type to store the data to the txt. This was because any character
        that was used in the structure of the string in the text could not be part of the name structure. I ended
        up going with _ and " ". If these characters were used in the name, the GameDataParser strips the name of
        those characters.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
        
        I think the best part of my design is the GridBagLayout - it makes positioning items and aligning them
        extremely easy. This design choice also allowed for faster coding, as I'm used to the structure as it's
        similar to the TKinter GUI engine in Python, which I am familiar with.
        
        I think the enums were also useful as they got passed around by functions a lot, so standardising them
        prevented typos and errors. I would probably refactor the game difficulty to an Enum, because it got
        passed around as strings and ints and became a bit confusing.
        
        Encapsulation is pretty good. All variables are private, and only relevant variables have getter/setter
        methods. The Grid.getBlocks() and GameDataParser.getData() methods return clones of the array, but aside
        from that arrays are only passed around privately, so encapsulation isn't an issue. 
        
        Separation of functionality could be improved mainly in the Grid class. It should probably be split into 
        a pure logic-based class, and another one that actually displays the data, but in the end the class wasn't
        **too** messy so it was okay. 



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
    - Java Oracle Documentation
    - TutorialsPoint: https://www.tutorialspoint.com/index.htm to see practical examples of different
        Swing components being used. 
    
