

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.*;

/** 
 * The most important class in Minesweeper. It has two main functionalities
 * 1. Stores all the core logic of the actual game
 * 2. Draws the grid
 * @author vikramsingh
 *
 */
public class Grid extends JPanel {
    private final static int WIDTH = 600;
    private final static int HEIGHT = 600;
    private int rows;
    private int cols;
    private int numBombs;
    private Block[][] blocks;
    private int size;
    private final static int[][] neighbours = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
    // 1 if won, -1 if lost, 0 otherwise
    private GameStatus gameStatus;
    private final static String FILEPATH = "files/lastgame.txt";
    private int bombsLeft;
    
    public Grid(int rows, int cols, int numBombs) {
        this.rows = rows;
        this.cols = cols;
        this.numBombs = numBombs;
        this.blocks = new Block[this.rows][];
        this.size = Math.min(WIDTH, HEIGHT) / rows;
        this.generateBlocks();
        this.setLayout(new GridLayout());
        this.mouseListenerHelper();
        this.gameStatus = GameStatus.PLAYING;
        this.setFocusable(true);
        this.bombsLeft = this.numBombs;
        this.setBackground(Color.pink);
    }
    
    /**
     * Helper function used in Grid constructor to detect and act on left and right click
     */
    private void mouseListenerHelper() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int row = (int) x / size;
                int col = (int) y / size;
                if (row < rows && col < cols) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        LeftClickResponse response = blocks[row][col].leftClick();
                        switch (response) {
                        case FLOODFILL:
                            floodFill(row, col);
                            break;
                        case ENDGAME:
                            gameStatus = GameStatus.GAMELOST;
                            endGame();
                            break;
                        case ALLGOOD:
                            boolean win = isGameWon();
                            if (win) {
                                gameStatus = GameStatus.GAMEWON;
                            }
                            break;
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        blocks[row][col].rightClick();
                        if (blocks[row][col].getState() == BlockState.FLAGGED) {
                            bombsLeft -= 1;
                        } else {
                            bombsLeft += 1;
                        }
                    }
                    repaint();
                }
            }
       });
        
    }
    /**
     * Resets the grid to a new randomly generated hidden grid.
     */
    public void reset() {
        this.rows = rows;
        this.cols = cols;
        this.numBombs = numBombs;
        this.blocks = new Block[this.rows][];
        this.size = Math.min(WIDTH, HEIGHT) / rows;
        this.generateBlocks();
        this.setLayout(new GridLayout());
        this.gameStatus = GameStatus.PLAYING;    
        repaint();
    }
    
    /**
     * Generates the blocks from a randomly generated assignment of bombs.
     */
    private void generateBlocks() {
        TreeSet<Integer> bombBlocks = getNRandom(this.numBombs, this.rows * this.cols);
        // Assign the bomb/safe block for each space in grid
        for (int row = 0; row < this.rows; row++) {
            Block[] blockRow = new Block[this.cols];
            for (int col = 0; col < this.cols; col++) {
                if (!bombBlocks.isEmpty() && row * this.cols + col == bombBlocks.first()) {
                    blockRow[col] = new BombBlock(BlockState.UNCHECKED, this.size * row, this.size * col, this.size);
                    bombBlocks.pollFirst();
                } else {
                    blockRow[col] = new SafeBlock(BlockState.UNCHECKED, this.size * row, this.size * col, this.size);
                }
            }
            this.blocks[row] = blockRow;
        }
        generateNeighbours();
    }
    
    /**
     * Helper function generateBlocks() for that generates the number of neighbours for all the blocks
     */
    private void generateNeighbours() {
        // Find number of neighbours
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                int bombNeighbours = 0;
                for (int[] neighbour: neighbours) {
                    try {
                        if (this.blocks[row+neighbour[0]][col+neighbour[1]].getNeighbours() == -1) {
                            bombNeighbours++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ex){
                        // catches the borders
                        // e.g. if neighbour[0] = 1 and you're on the last row
                    }
                }
                block.setNeighbours(bombNeighbours);
            }
        }
    }
    
    
    /**
     * Helper function for generateBlocks() that picks n random blocks to be the bombs
     */
    private static TreeSet<Integer> getNRandom(int n, int max) {
        TreeSet<Integer> set = new TreeSet<Integer>();      
        Random random = new Random();
     
        while(set.size() < n){
            int thisOne = random.nextInt(max);
            set.add(thisOne);
        }
        return set;
    }
    
    
    /**
     * Recursive algorithm that, when triggered from a particular 0-neighbour block, discovers all neighbouring
     * blocks until they are no longer 0-neighbour blocks. 
     * @param row: row of the 0-neighbour block
     * @param col: col of the 0-neighbour block
     */
    private void floodFill(int row, int col) {
        Block block = this.blocks[row][col];
        System.out.println(block.toString() + " " + block.getState() + " " + Integer.toString(block.getNeighbours()));
        block.setState(BlockState.DISCOVERED);
        if (block.getNeighbours() == 0) {
            // recurse through all the neighbours of the block.
            for (int[] neighbour:neighbours){
                if (isSafe(row + neighbour[0], col + neighbour[1])) {
                    floodFill(row + neighbour[0], col + neighbour[1]);
                }
            }
        }
    }
    
    /**
     * helper function for floodFill that checks if the new block is undiscovered and within bounds
     */
    private boolean isSafe(int row, int col) {
        boolean inGrid = row >= 0 && row < this.blocks.length && col >= 0 && col < this.blocks[0].length;
        if (inGrid) {
            Block block = this.blocks[row][col];
            return block.getState() != BlockState.DISCOVERED;
        } else {
            return false;
        }
    }
    
    /**
     * Ends the game by discovering all blocks and changing game status.
     */
    private void endGame() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                block.setState(BlockState.DISCOVERED);
            }
        }
        this.gameStatus = GameStatus.GAMELOST;
    }
    
    /**
     * Checks if game is won, and if it is won, then discover all blocks
     */
    public boolean isGameWon() {
        if (this.gameStatus == GameStatus.GAMELOST) {
            return false;
        }
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                if (block.getState() != BlockState.DISCOVERED && block.getNeighbours() != -1) {
                    return false;
                }
            }
        }
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                block.setState(BlockState.DISCOVERED);
            }
        }
        repaint();
        this.gameStatus = GameStatus.GAMEWON;
        return true;
    }
    
    
    public GameStatus getGameStatus() {
        return this.gameStatus;
    }
    
    public void setGameStatus(GameStatus status) {
        this.gameStatus = status;
    }
    
    public Block[][] getBlocks() {
        return this.blocks.clone();
    }
    
    public int getBombsLeft() {
        return this.bombsLeft;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        int shift;
        switch (this.rows) {
        // adjust font size based on number of blocks
        case 23:
            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 12));
            shift = 3;
            break;
        case 16:
            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 16));
            shift = 4;
            break;
        default:
            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 20));
            shift = 6;
            break;
        }
        
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                block.draw(g2D, shift);
                
            }
        }
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
    
}
