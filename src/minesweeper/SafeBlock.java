package minesweeper;

import java.awt.Graphics;

public class SafeBlock extends Block {
    private BlockState state;
    private int numberOfNeighbours;
    private int x;
    private int y;
    private int size;
    private boolean gameEnd;    
    
    public SafeBlock(boolean isBomb, BlockState state, int x, int y, int size) {
        super(isBomb, state, x, y, size);
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
    protected void leftClick() {
        this.state = BlockState.DISCOVERED;
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawRect(x, y, size, size);
        System.out.println("yeet");
    }

    
    
}
