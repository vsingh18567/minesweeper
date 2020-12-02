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
    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;
    private int rows;
    private int cols;
    private int numBombs;
    private Block[][] blocks;
    private int size;
    private final static int[][] neighbours = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
    // 1 if won, -1 if lost, 0 otherwise
    private int gameStatus;
    
    public Grid(int rows, int cols, int numBombs) {
        this.rows = rows;
        this.cols = cols;
        this.numBombs = numBombs;
        this.blocks = new Block[this.rows][];
        this.size = Math.min(WIDTH, HEIGHT) / rows;
        this.generateBlocks();
        this.setLayout(new GridLayout());
        this.mouseListenerHelper();
        this.gameStatus = 0;
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
                            gameStatus = -1;
                            endGame();
                            break;
                        case ALLGOOD:
                            boolean win = isGameWon();
                            if (win) {
                                gameStatus = 1;
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
                        System.out.println("yeet");
                        blocks[row][col].rightClick();
                        repaint();
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        // catches clicking on the last row/col which isn't actually part of grid
                    }
                }
                System.out.println(e.getButton());
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
        this.mouseListenerHelper();
        this.gameStatus = 0;    
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
        return true;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                block.draw(g2D);
                
            }
        }
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
    
}
