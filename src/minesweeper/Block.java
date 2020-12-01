package minesweeper;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public abstract class Block extends JPanel {
    private BlockState state;
    private int numberOfNeighbours;
    private int x;
    private int y;
    private int size;
    private boolean gameEnd;
    
    public Block (boolean isBomb, BlockState state, int x, int y, int size) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.size = size;
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    leftClick();
                } else if (evt.getButton() == MouseEvent.BUTTON2) {
                    rightClick();
                }
            }});
    }
    
    public BlockState getState() {
        return this.state;
    }
    
    public void setState(BlockState newState) {
        this.state = newState;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.x;
    }

    public abstract void setNeighbours(int number);
    
    public abstract int getNeighbours();
    
    public abstract void draw(Graphics g);
    
    protected abstract void leftClick();
    
    private void rightClick() {
        if (state == BlockState.FLAGGED) {
            setState(BlockState.UNCHECKED);
        } else if (state == BlockState.UNCHECKED) {
            setState(BlockState.FLAGGED);
        } 
    }
    
    public boolean hasGameEnded() {
        return this.gameEnd;
    }
    
    
}
