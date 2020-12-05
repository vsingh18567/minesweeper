package minesweeper;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.*;

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
    }
    
    
    
    private void mouseListenerHelper() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int row = (int) x / size;
                int col = (int) y / size;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try {
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
                                endGame();
                            }
                            break;
                        }
                        repaint();
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        // catches clicking on the last row/col which isn't actually part of grid
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    try {
                        blocks[row][col].rightClick();
                        repaint();
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        // catches clicking on the last row/col which isn't actually part of grid
                    }
                }
            }
       });
        
    }
    
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
    
    private void setBlocks(Block[][] blockArr) {
        this.blocks = blockArr;
    }
    
    private void loadBlocks(BlockState[][] blockStates, TreeSet<Integer> bombLocations) {
        Block[][] blockArr = new Block[this.rows][];
        for (int row = 0; row < this.rows; row++) {
            Block[] blockRow = new Block[this.cols];
            for (int col = 0; col < this.cols; col++) {
                if (bombLocations.size() != 0 && row * this.cols + col == bombLocations.first()) {
                    blockRow[col] = new BombBlock(blockStates[row][col], this.size * row, this.size * col, this.size);
                    bombLocations.pollFirst();
                } else {
                    blockRow[col] = new SafeBlock(blockStates[row][col], this.size * row, this.size * col, this.size);
                }
            }
            blockArr[row] = blockRow;
        }
        this.blocks = blockArr;
        System.out.println("STEP 2 " + this.blocks.length);
        generateNeighbours();
        System.out.println("STEP 3 " + this.blocks.length);
        repaint();
    }
    
    
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
    
    
    // helper function
    private static TreeSet<Integer> getNRandom(int n, int max) {
        TreeSet<Integer> set = new TreeSet<Integer>();      
        Random random = new Random();
     
        while(set.size() < n){
            int thisOne = random.nextInt(max);
            set.add(thisOne);
        }
        return set;
    }
    
    // helper for floodFill
    private boolean isSafe(int row, int col) {
        boolean inGrid = row >= 0 && row < this.blocks.length && col >= 0 && col < this.blocks[0].length;
        if (inGrid) {
            Block block = this.blocks[row][col];
            return block.getState() != BlockState.DISCOVERED;
        } else {
            return false;
        }
    }
    
    
    private void floodFill(int row, int col) {
        /* https://en.wikipedia.org/wiki/Flood_fill#Stack-based_recursive_implementation_(four-way)
         * Start node: this.blocks[row][col]
         * target-color: block.getState() == BlockState.UNCHECKED && block.getNeighbours() == 0
         * replacement-color: block.setState(BlockState.DISCOVERED)
         */
        
        Block block = this.blocks[row][col];
        System.out.println(block.toString() + " " + block.getState() + " " + Integer.toString(block.getNeighbours()));
        block.setState(BlockState.DISCOVERED);
        if (block.getNeighbours() == 0) {
            for (int[] neighbour:neighbours){
                if (isSafe(row + neighbour[0], col + neighbour[1])) {
                    floodFill(row + neighbour[0], col + neighbour[1]);
                }
            }
        }
    }
    
    private void endGame() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                block.setState(BlockState.DISCOVERED);
            }
        }
        this.gameStatus = GameStatus.GAMELOST;
    }
    
    
    private boolean isGameWon() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                if (block.getState() != BlockState.DISCOVERED && block.getNeighbours() != -1) {
                    return false;
                }
            }
        }
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
    
    @Override
    public void paintComponent(Graphics g) {
        int shift;
        switch (this.rows) {
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
        System.out.println("STEP 8 " + this.getBlocks().length);
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
