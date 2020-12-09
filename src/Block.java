

import java.awt.*;

/**
 * Abstract class that stores block information of the grid
 * @author vikramsingh
 *
 */
public abstract class Block {
    private BlockState state;
    private int numberOfNeighbours;
    private int x;
    private int y;
    private int width;
    
    public Block(BlockState state, int x, int y, int width) {
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
    
    public void setNeighbours(int number) {
        this.numberOfNeighbours = number;
    }
    
    
    public int getNeighbours() {
        return this.numberOfNeighbours;
    }
        
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
        return getState() + Integer.toString(this.x) + "," + 
                Integer.toString(this.y) + "," + this.getNeighbours();
    }

    public abstract void draw(Graphics2D g, int shift);

    
    
}
