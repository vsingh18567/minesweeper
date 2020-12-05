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
        
    protected abstract LeftClickResponse leftClick();
    
    public void rightClick() {
        if (state == BlockState.FLAGGED) {
            setState(BlockState.UNCHECKED);
        } else if (state == BlockState.UNCHECKED) {
            setState(BlockState.FLAGGED);
        }
    }
    
    public int getWidth() {
        return this.width;
    }
    
    
    @Override
    public String toString() {
        return getState() + Integer.toString(this.x) + "," + Integer.toString(this.y) + "," + this.getNeighbours();
    }

    public abstract void draw(Graphics2D g, int shift);

    
    
}
