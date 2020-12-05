package minesweeper;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class GameTimer extends JLabel{
    private Timer timer;
    private int duration;
    
    public GameTimer() {
        this.duration = 0;
        this.setText(durationToString());
        this.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                duration += 1000;
                updateText();
            }
        });
        timer.start();
    
    }
    
    public void updateText() {
        this.setText(durationToString());
    }
    
    public void reset() {
        this.duration = 0;
    }
    
    public String durationToString() {
       int seconds = (int) this.duration / 1000;
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
