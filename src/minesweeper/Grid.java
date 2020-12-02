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
    
    public Grid(int rows, int cols, int numBombs) {
        this.rows = rows;
        this.cols = cols;
        this.numBombs = numBombs;
        this.blocks = new Block[this.rows][];
        this.size = Math.min(WIDTH, HEIGHT) / rows;
        this.generateBlocks();
        this.setLayout(new GridLayout());
        this.mouseListenerHelper();

    }
    
    public void mouseListenerHelper() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int row = (int) x / size;
                int col = (int) y / size;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try {
                        LeftClickResponse response = blocks[row][col].leftClick();
                        if (response == LeftClickResponse.FLOODFILL) {
                            floodFill(row, col);
                        } else if (response == LeftClickResponse.ENDGAME) {
                            endGame();
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
    
    
    private void floodFill(int row, int col) {
        Block block = this.blocks[row][col];
        if (block.getNeighbours() == 0 && block.getState() != BlockState.DISCOVERED) {
            block.setState(BlockState.DISCOVERED);
            for (int[] neighbour: neighbours) {
                floodFill(row + neighbour[0], col + neighbour[1]);
            }
        } else {
            block.setState(BlockState.DISCOVERED);
        }
    }
    
    public void endGame() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                block.setState(BlockState.DISCOVERED);
            }
        }
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
