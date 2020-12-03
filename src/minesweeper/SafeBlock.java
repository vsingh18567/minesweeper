package minesweeper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SafeBlock extends Block {
    private BlockState state;
    private int numberOfNeighbours;
    private int x;
    private int y;
    private int width;
    
    public SafeBlock(BlockState state, int x, int y, int width) {
        super(state, x, y, width);
    }
    
    @Override
    public void setNeighbours(int number) {
        this.numberOfNeighbours = number;
    }
    
    @Override
    public int getNeighbours() {
        return this.numberOfNeighbours;
    }
    
    @Override
    protected LeftClickResponse leftClick() {
        setState(BlockState.DISCOVERED);
        if (getNeighbours() == 0) {
            return LeftClickResponse.FLOODFILL;
        }
        else {
            return LeftClickResponse.ALLGOOD;
        }
    }
    
    @Override
    public void draw(Graphics2D g) {

        switch (getState()) {
        case UNCHECKED:
            g.setColor(Color.GRAY);
            break;
        case DISCOVERED:
            g.setColor(Color.GREEN);
            
            break;
        case FLAGGED:
            g.setColor(Color.ORANGE);
            break;
        }
        g.fillRect(getX(), getY(), getWidth(), getWidth());
        g.setColor(Color.BLACK);
        g.drawRect(getX(), getY(), getWidth(), getWidth());
        if (getNeighbours() != 0 && getState() == BlockState.DISCOVERED) {
            g.drawString(Integer.toString(this.getNeighbours()), getX() + getWidth() / 2, getY() + getWidth() / 2);
        }
        
    }
    

    
    
}
