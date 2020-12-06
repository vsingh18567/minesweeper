

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
/**
 * The Game Timer class has three main functions
 * 1. Store the time taken so far in the game
 * 2. Allow for that time to be started/stopped/paused
 * 3. Display that text once its added to a frame
 * @author vikramsingh
 *
 */
public class GameTimer extends JLabel {
    private Timer timer;
    private int duration;
    
    public GameTimer() {
        this.duration = 0;
        this.setText(durationToString(this.duration));
        this.timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                duration += 500;
                updateGame();
            }
        });
        timer.start();
    
    }
    
    private void updateGame() {
        this.setText(durationToString(this.duration));
    }
    
    public void reset() {
        this.duration = 0;
    }
    
    /**
     * Valuable helper function that turns a millisecond-duration into a min:sec string.
     * @param duration
     * @return
     */
    public static String durationToString(int duration) {
       int seconds = (int) duration / 1000;
       int minutes = (int) seconds / 60;
       seconds = seconds % 60;
       String sString;
       if (seconds < 10) {
           sString = "0" + Integer.toString(seconds);
       } else {
           sString = Integer.toString(seconds);
       }
       String mString;
       if (minutes < 10) {
           mString = "0" + Integer.toString(minutes);
       } else {
           mString = Integer.toString(minutes);
       }
       
       return mString + ":" + sString;
    }
    
    public void pause() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.restart();
        }
    }
    
    public int getTime() {
        return this.duration;
    }
    
}
