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
    
    public Grid(int rows, int cols, int numBombs) {
        this.rows = rows;
        this.cols = cols;
        this.numBombs = numBombs;
        this.blocks = new Block[this.rows][];
        this.size = Math.min(WIDTH, HEIGHT) / rows;
        this.generateBlocks();
        this.setLayout(new GridLayout());
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = e.getX();
                    int y = e.getY();
                    int row = (int) x / size;
                    int col = (int) y / size;
                    try {
                    blocks[row][col].setState(BlockState.DISCOVERED);
                    repaint();
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        // catches clicking on the last row/col which isn't actually part of grid
                    }
                }
            }
       });
        
    }
    
    
    private void generateBlocks() {
        TreeSet<Integer> bombBlocks = getNRandom(this.numBombs, this.rows * this.cols);
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
        
        
        
    }
    
    private static TreeSet<Integer> getNRandom(int n, int max) {
        TreeSet<Integer> set = new TreeSet<Integer>();      
        Random random = new Random();
     
        while(set.size() < n){
            int thisOne = random.nextInt(max);
            set.add(thisOne);
        }
        return set;
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                Block block = this.blocks[row][col];
                block.draw(g2D);
                
//                g2D.setColor(Color.GRAY);
//                if (block.getState() == BlockState.DISCOVERED) {
//                    g2D.setColor(Color.WHITE);
//                }
//                System.out.println(block.toString());
//                g2D.fillRect(block.getX(), block.getY(), block.getWidth(), block.getWidth());
//                g2D.setColor(Color.BLACK);
//                g2D.drawRect(block.getX(), block.getY(), block.getWidth(), block.getWidth());
            }
        }
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
    
}
