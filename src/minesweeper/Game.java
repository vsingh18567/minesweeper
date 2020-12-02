package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Initial screen
        JLabel title = new JLabel("Minesweeper");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(title, gbc);
        
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startNewGame(frame, gbc);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(newGame, gbc);
        
        JButton loadGame = new JButton("Load Game");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGame(frame, gbc);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(loadGame, gbc);
        
        JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadInstructions(frame, gbc);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(instructions, gbc);
        
        

        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setVisible(true);
    }
    
    private void startNewGame(JFrame f, GridBagConstraints gbc) {
        f.getContentPane().removeAll();
        final Grid grid = new Grid(5,5,0);
        gbc.gridx = 1;
        gbc.gridy = 0;
        f.add(grid, gbc);
        
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grid.reset();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        f.add(reset, gbc);
        
        f.validate();        
        f.repaint();
    }
    
    private void loadGame(JFrame f, GridBagConstraints gbc) {
        
    }
    
    private void loadInstructions(JFrame f, GridBagConstraints gbc) {
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    
}


