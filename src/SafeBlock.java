

import java.awt.Color;
import java.awt.Graphics2D;
/**
 * A SafeBlock is every block that is not a BombBlock. 
 * @author vikramsingh
 *
 */
public class SafeBlock extends Block {

    
    public SafeBlock(BlockState state, int x, int y, int width) {
        super(state, x, y, width);
    }
    
    
    /**
     * If a SafeBlock has no BombNeighbours, then the floodfill algorithm is triggered,
     * because if a block has no BombNeighbours, then you can safely discover every
     * neighbouring block
     */
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
    public void draw(Graphics2D g, int shift) {

        switch (getState()) {
        case UNCHECKED:
            g.setColor(Color.GRAY);
            break;
        case DISCOVERED:
            g.setColor(new Color(50, 205, 50));
            
            break;
        case FLAGGED:
            g.setColor(new Color(255, 140, 0));
            break;
        }
        g.fillRect(getX(), getY(), getWidth(), getWidth());
        g.setColor(Color.BLACK);
        g.drawRect(getX(), getY(), getWidth(), getWidth());
        if (getNeighbours() != 0 && getState() == BlockState.DISCOVERED) {
            g.drawString(Integer.toString(this.getNeighbours()), getX() + getWidth() / 2 - shift, getY() + getWidth() / 2 + shift);
        }
    }
    

    
    
}
