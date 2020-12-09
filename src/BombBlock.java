

import java.awt.Color;
import java.awt.Graphics2D;
/**
 * the BombBlock is a game-ending block if clicked. There are only two ways it can
 * be drawn.
 * @author vikramsingh
 *
 */
public class BombBlock extends Block {

    
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
    
    /**
     * If clicked, it triggers Grid.endgame()
     */
    @Override
    protected LeftClickResponse leftClick() {
        setState(BlockState.DISCOVERED);
        return LeftClickResponse.ENDGAME;
    }
    
    @Override
    public void draw(Graphics2D g, int shift) {
        switch (getState()) {
            case DISCOVERED:
                g.setColor(Color.RED);
                break;
            case FLAGGED:
                g.setColor(new Color(255, 140, 0));
                break;
            default:
                g.setColor(Color.GRAY);
                break;
        }
        g.fillRect(getX(), getY(), getWidth(), getWidth());
        g.setColor(Color.BLACK);
        g.drawRect(getX(), getY(), getWidth(), getWidth());
    }
    
    
    
}

