package minesweeper;
import java.awt.*;
import java.awt.event.*;
import java.util.TreeSet;

import javax.swing.*;

public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setVisible(true);
        homeScreen(frame, gbc);
        // Initial screen
        
    }
    
    private void homeScreen(JFrame f, GridBagConstraints gbc) {
        JLabel title = new JLabel("Minesweeper");
        gbc.gridx = 0;
        gbc.gridy = 0;
        f.add(title, gbc);
        
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGameGrid(f, gbc);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        f.add(newGame, gbc);
        
        JButton loadGame = new JButton("Load Game");
        loadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGameGrid(f, gbc);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        f.add(loadGame, gbc);
        
        JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadInstructions(f, gbc);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        f.add(instructions, gbc);
        
    }
    
    private void newGameGrid(JFrame f, GridBagConstraints gbc) {
        f.getContentPane().removeAll();
        final Grid grid = new Grid(20,20,70);
        viewGame(f, gbc, grid);
        
    }
    
    private void loadGameGrid(JFrame f, GridBagConstraints gbc) {
        f.getContentPane().removeAll();
        GameDataParser gdp = new GameDataParser("files/lastgame.txt");
        BlockState[][] blockStates = gdp.getDiscoveredStates();
        TreeSet<Integer> bombLocations = gdp.getBombLocations();
        System.out.println(bombLocations);
        final Grid grid = new Grid(blockStates, bombLocations);
        viewGame(f, gbc, grid);
        
    }
    
    private void viewGame(JFrame f, GridBagConstraints gbc, Grid grid) {
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
        
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grid.saveArray();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        f.add(save, gbc);
        
        final JButton back = new JButton("Back");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                run();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        f.add(save, gbc);
        
        f.validate();        
        f.repaint();
    }
    
    private void loadInstructions(JFrame f, GridBagConstraints gbc) {
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    
}


