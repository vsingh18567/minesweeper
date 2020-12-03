package minesweeper;
import java.awt.*;
import java.awt.event.*;
import java.util.TreeSet;

import javax.swing.*;

public class Game implements Runnable {
    public static final JFrame frame = new JFrame("Minesweeper");
    public static final GridBagConstraints gbc = new GridBagConstraints();
    
    public void run() {
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setVisible(true);
        homeScreen();
        // Initial screen
        
    }
    
    private void homeScreen() {
        JLabel title = new JLabel("Minesweeper");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(title, gbc);
        
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame("NEWGAME");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(newGame, gbc);
        
        JButton loadGame = new JButton("Load Game");
        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame("LOADGAME");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(loadGame, gbc);
        
        JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadInstructions(frame, gbc);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(instructions, gbc);
        frame.validate();        
        frame.repaint();
        
    }
    
    private void loadGame(String gameType) {
        frame.getContentPane().removeAll();
        Grid grid;
        if (gameType == "NEWGAME") {
            grid = new Grid(20, 20, 70);
        } else {
            grid = new Grid();
        }
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.add(grid, gbc);
        
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.reset();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(reset, gbc);
        
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.saveArray();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(save, gbc);
        
        final JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                homeScreen();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 1;
        frame.add(back, gbc);
        
        frame.validate();        
        frame.repaint();
        
    }
    
    private void loadInstructions(JFrame f, GridBagConstraints gbc) {
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    
}


