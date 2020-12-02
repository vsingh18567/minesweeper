package minesweeper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class BombBlock extends Block {
    private BlockState state;
    private int x;
    private int y;
    private int width;
    private boolean gameEnd;    
    
    public BombBlock(BlockState state, int x, int y, int width) {
        super(state, x, y, width);
    }
    
    @Override
    public void setNeighbours(int number) {
        // you don't want to store neighbours 
    }
    
    @Override
    public int getNeighbours() {
        return -1;
    }
    
    @Override
    protected LeftClickResponse leftClick() {
        setState(BlockState.DISCOVERED);
        return LeftClickResponse.ENDGAME;
    }
    
    @Override
    public void draw(Graphics2D g) {
        switch (getState()) {
        case UNCHECKED:
            g.setColor(Color.GRAY);
            break;
        case DISCOVERED:
            g.setColor(Color.RED);
            break;
        case FLAGGED:
            g.setColor(Color.ORANGE);
            break;
        }
        g.fillRect(getX(), getY(), getWidth(), getWidth());
        g.setColor(Color.BLACK);
        g.drawRect(getX(), getY(), getWidth(), getWidth());
    }
    
    
    
}

