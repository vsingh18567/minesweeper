package minesweeper;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public abstract class Block {
    private BlockState state;
    private int numberOfNeighbours;
    private int x;
    private int y;
    private int width;
    private boolean gameEnd;
    
    public Block (BlockState state, int x, int y, int width) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.width = width;
        
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
        return this.y;
    }

    public abstract void setNeighbours(int number);
    
    public abstract int getNeighbours();
    
    // public abstract void draw(Graphics g);
    
    protected abstract void leftClick(Graphics2D g);
    
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
    
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.x) + "," + Integer.toString(this.y);
    }

    public abstract void draw(Graphics2D g);

    
    
}
