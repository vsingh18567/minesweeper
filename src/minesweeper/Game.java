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
        frame.setSize(800,800);
        frame.setVisible(true);
        GameDataParser gdp = new GameDataParser("files/highscores.txt");
        System.out.println(gdp.getData(0));
        homeScreen();
        // Initial screen
    }
    
    private void homeScreen() {
        JLabel title = new JLabel("Minesweeper");
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(title, gbc);
            
        JLabel difficulty = new JLabel("Difficulty: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        frame.add(difficulty, gbc);
        
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox difficultyList = new JComboBox<String>(difficulties);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(difficultyList, gbc);
        
        
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame((String) difficultyList.getSelectedItem());
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        frame.add(newGame, gbc);
        
        JButton highScores = new JButton("View High Scores");
        highScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(highScores, gbc);
        
        JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadInstructions(frame, gbc);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(instructions, gbc);
        frame.validate();        
        frame.repaint();
        
    }
    
    private void loadGame(String gameType) {
        frame.getContentPane().removeAll();
        
        
        
        Grid grid;
        
        switch (gameType) {
        case "Hard":
            grid = new Grid(23, 23, 99);
            break;
        case "Medium":
            grid = new Grid(16, 16, 40);
            break;
        default:
            grid = new Grid(9, 9, 10);
            break;
        }
        

        System.out.println("STEP 4 " + grid.getBlocks().length);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        frame.add(grid, gbc);
        System.out.println(grid.getBlocks().length);
        
//        GameTimer gameTimer = new GameTimer(grid);
//        gbc.gridx = 3;
//        gbc.gridy = 1;
//        frame.add(gameTimer, gbc);
        final GameTimer timer = new GameTimer();
        gbc.gridx = 4;
        gbc.gridy = 1;
        frame.add(timer, gbc);
        
        gbc.gridwidth = 1;
        

        
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.reset();
                timer.reset();
                
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(reset, gbc);
        
        
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
        
        final JButton pausePlay = new JButton("Pause/Play");
 
        boolean isPaused = false;    
        gbc.gridx = 3;
        gbc.gridy = 1;
        frame.add(pausePlay, gbc);
        pausePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (grid.getGameStatus() != GameStatus.PAUSED) {
                    grid.setGameStatus(GameStatus.PAUSED);
                    frame.remove(grid);
                } else if (grid.getGameStatus() == GameStatus.PAUSED) {
                    grid.setGameStatus(GameStatus.PLAYING);
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.gridwidth = 5;
                    frame.add(grid, gbc);
                    grid.repaint();
                }
                timer.pause();
                frame.validate();
                frame.repaint();
            }
        });
//        final JButton pause = new JButton("Pause/Play");
//        pause.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // gameTimer.pause();
//            }
//        });
//        gbc.gridx = 4;
//        gbc.gridy = 1;
//        frame.add(pause, gbc);
        

        
        System.out.println("STEP 5 " + grid.getBlocks().length);
        frame.validate();     
        System.out.println("STEP 6 " + grid.getBlocks().length);
        frame.repaint();
        System.out.println("STEP 7 " + grid.getBlocks().length);

        
    }
    
    private void loadInstructions(JFrame f, GridBagConstraints gbc) {
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    
}


