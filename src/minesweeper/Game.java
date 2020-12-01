package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLayout(new BorderLayout());
        final Grid grid = new Grid(10,10,10);
        frame.add(grid);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    
}


