package minesweeper;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class Grid extends JPanel {
    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;
    private int rows;
    private int cols;
    private int numBombs;
    private Block[][] blocks;
    
    public Grid(int rows, int cols, int numBombs) {
        this.rows = rows;
        this.cols = cols;
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int mx = evt.getX();
                int my = evt.getY();
                System.out.println(mx + my);
            }});
        
    }
    
    
    private void generateBlocks() {
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = Math.min(getWidth(), getHeight()) / 30;
        int width = getWidth();
        int height = getHeight();

        int y = (getHeight() - (size * 30)) / 2;
        for (int horz = 0; horz < 30; horz++) {
            int x = (getWidth() - (size * 30)) / 2;
            for (int vert = 0; vert < 30; vert++) {
                SafeBlock block = new SafeBlock(false, BlockState.DISCOVERED, 50, 50, 30);
                block.draw(g);
                // g.drawRect(x, y, size, size);
                // System.out.println(x);
                // System.out.println(y);
                // g.drawString("4", x+(size/2), y+(size/2));
                x += size;
            }
            y += size;
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
