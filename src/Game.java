

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.swing.*;

/**
 * Minesweeper application developed by Vikram Singh as Homework 9 of CIS 120, Fall 2020.
 */
public class Game implements Runnable {
    public static final JFrame frame = new JFrame("Minesweeper");
    public static final GridBagConstraints gbc = new GridBagConstraints();
    
    /**
     * Sets up basic frame settings and triggers home screen
     */
    public void run() {
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800,800);
        frame.setVisible(true);
        frame.setBackground(Color.PINK);
        homeScreen();
        // Initial screen
    }
    
    /**
     * Home screen that contains title, buttons that lead to "New Game", "View High Scores" and instructions.
     */
    private void homeScreen() {
        frame.getContentPane().removeAll();
        frame.getContentPane().setBackground(Color.PINK);

        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.ipady = 10;

        JLabel title = new JLabel("Minesweeper");
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(title, gbc);
        
        JLabel difficulty = new JLabel("Difficulty: ");
        difficulty.setFont(new Font(difficulty.getFont().getFontName(), Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        frame.add(difficulty, gbc);
        
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox difficultyList = new JComboBox<String>(difficulties);
        difficultyList.setFont(new Font(difficultyList.getFont().getFontName(), Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(difficultyList, gbc);
        
        
        JButton newGame = new JButton("New Game");
        newGame.setFont(new Font(newGame.getFont().getFontName(), Font.PLAIN, 18));
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame((String) difficultyList.getSelectedItem());
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(newGame, gbc);
        
        JButton highScores = new JButton("View High Scores");
        highScores.setFont(new Font(highScores.getFont().getFontName(), Font.PLAIN, 18));
        highScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHighScores();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(highScores, gbc);
        
        JButton instructions = new JButton("Instructions");
        instructions.setFont(new Font(instructions.getFont().getFontName(), Font.PLAIN, 18));
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadInstructions();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        frame.add(instructions, gbc);
        frame.validate();        
        frame.repaint();
        
    }
    
    /**
     * Main game screen - has all the core logic that's handled through the Grid and the related buttons.
     * Triggered by home screen.
     * @param gameType: String -- "Easy", "Medium", "Hard"
     */
    @SuppressWarnings("unchecked")
    private void loadGame(String gameType) {
        frame.getContentPane().removeAll();
        
        String[] columnNames = {"User", "Score"};
        
        
        Grid grid;
        int difficulty;
        
        switch (gameType) {
        case "Hard":
            grid = new Grid(23, 23, 99);
            difficulty = 2;
            break;
        case "Medium":
            grid = new Grid(16, 16, 40);
            difficulty = 1;
            break;
        default:
            grid = new Grid(9, 9, 5);
            difficulty = 0;
            break;
        }
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        frame.add(grid, gbc);
        
        gbc.gridwidth = 1;

        final GameTimer timer = new GameTimer();
        timer.setFont(new Font(timer.getFont().getFontName(), Font.PLAIN, 20));
        gbc.gridx = 4;
        gbc.gridy = 1;
        frame.add(timer, gbc);

    
        
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {loadGame(gameType);}
        });
        reset.setFont(new Font(reset.getFont().getFontName(), Font.PLAIN, 20));
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
        back.setFont(new Font(back.getFont().getFontName(), Font.PLAIN, 20));
        gbc.gridx = 2;
        gbc.gridy = 1;
        frame.add(back, gbc);
        
        final JButton pausePlay = new JButton("Pause/Play");
        pausePlay.setFont(new Font(pausePlay.getFont().getFontName(), Font.PLAIN, 20));
        boolean isPaused = false;    
        gbc.gridx = 3;
        gbc.gridy = 1;
        frame.add(pausePlay, gbc);
        pausePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                 * Based on what the current game state is, pause/play does different things. The grid is removed from the screen
                 * on purpose to prevent "cheating" by pausing and continuing to think about options.
                 */
                if (grid.getGameStatus() == GameStatus.GAMELOST || grid.isGameWon()) {
                    return;
                }
                else if (grid.getGameStatus() == GameStatus.PLAYING) {
                    grid.setGameStatus(GameStatus.PAUSED);
                    frame.remove(grid);
                } else if (grid.getGameStatus() == GameStatus.PAUSED) {
                    grid.setGameStatus(GameStatus.PLAYING);
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.gridwidth = 6;
                    frame.add(grid, gbc);
                    grid.repaint();
                }
                timer.pause();
                frame.validate();
                frame.repaint();
            }
        });
        
        int bombsLeftNum = grid.getBombsLeft();

        final JLabel bombsLeft = new JLabel("Mines left: " + bombsLeftNum);
        bombsLeft.setFont(new Font(bombsLeft.getFont().getFontName(), Font.PLAIN, 20));
        gbc.gridx = 5;
        gbc.gridy = 1;
        frame.add(bombsLeft, gbc);
        
          
        Timer ticker = new Timer(100, null);
        ticker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean tickOutcome = tick(grid, timer, difficulty, bombsLeft);
                if (tickOutcome) {
                    ticker.stop();
                }
            }
        });       
        ticker.start();
    }
    
    /**
     * Handles parts of the frame that need to be continuously upgraded. There are three
     * main functionalities
     * 1. Check if the game state has changed and act accordingly (i.e. throw message dialog / input dialog)
     * 2. Update bombs left
     * 3. Update timer
     * @param g: the game grid
     * @param timer: the timer that needs to be paused/played
     * @param difficultyLevel
     * @param bombsLeft: the label which is displaying the number of bombs left
     * @return true if game has started/ended
     */
    private boolean tick(Grid g, GameTimer timer, int difficultyLevel, JLabel bombsLeft) {
        int bombsLeftNum = g.getBombsLeft();
        bombsLeft.setText("Mines left: " + bombsLeftNum);       
        
        GameDataParser gdp = new GameDataParser("files/highscores.txt");
        if (g.isGameWon()) {
            timer.pause();
            int duration = timer.getTime();

            
            if (gdp.isNewHighScore(difficultyLevel, duration)) {
                highScorePopUp(gdp, difficultyLevel, duration);
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "You won in " + GameTimer.durationToString(duration) + " minutes. \n Not quite fast enough for a high score!");
                frame.repaint();
            }
            return true;

        } else if (g.getGameStatus() == GameStatus.GAMELOST) {
            timer.pause();
            JOptionPane.showMessageDialog(frame, "Unlucky, you lost. Try again!");
            return true;
        }
        
        return false;
    }
    
    /**
     * Loads the instruction screen
     */
    private void loadInstructions() {
        
    }
    
    /**
     * View the high scores. Mainly uses the JTable component. Pulls from the GameDataParser
     */
    private void viewHighScores() {
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.ipady = 10;
        frame.getContentPane().removeAll();
        GameDataParser gdp = new GameDataParser("files/highscores.txt");
        String[] difficultyNames = {"Easy", "Medium", "Hard"};
        
        JLabel header = new JLabel("High Scores");
        header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        frame.add(header, gbc);
        
        gbc.gridwidth = 1;
        for (int i = 0; i < 3; i++) {
            String difficulty = difficultyNames[i];
            gbc.gridx = 2 * i;
            gbc.gridy = 1;
            JLabel difficultyLabel = new JLabel(difficulty);
            difficultyLabel.setFont(new Font(difficultyLabel.getFont().getFontName(), Font.PLAIN, 18));
            frame.add(difficultyLabel, gbc);
            
            ArrayList<ScoreData> dataset = gdp.getData(i);
            dataset = (ArrayList<ScoreData>) dataset.stream().sorted().collect(Collectors.toList());
            
            String[] columnNames = {"User", "Score"};
            String[][] scores = new String[10][];
            
            for (int j = 0; j < 10; j++) {
                ScoreData score = dataset.get(j);
                String user = score.getUser();
                String durationString = GameTimer.durationToString(1000 * score.getDuration());
                String[] scoreArr = {user, durationString};
                scores[j] = scoreArr;
            }
            
            JTable table = new JTable(scores, columnNames);
            table.setFont(new Font(table.getFont().getFontName(), Font.PLAIN, 16));
            table.setRowHeight(20);
            table.getColumnModel().getColumn(0).setPreferredWidth(200);
            table.getColumnModel().getColumn(1).setPreferredWidth(50);

            gbc.gridx = 2 * i;
            gbc.gridy = 2;
            frame.add(table, gbc);
            System.out.println(Arrays.deepToString(scores));
        }
        
        final JButton back = new JButton("Back");
        back.setFont(new Font(back.getFont().getFontName(), Font.PLAIN, 20));
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                homeScreen();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 3;
        frame.add(back, gbc);
        
        frame.validate();
        frame.repaint();
        
        
    }
    
    /**
     * Helper function for Game.tick() to handle a new high score.
     * Utilises the GameDataParser and the GameTimer.durationToString() function.
     * @param gdp
     * @param difficultyLevel
     * @param duration
     */
    private void highScorePopUp(GameDataParser gdp, int difficultyLevel, int duration) {
        String s = (String)JOptionPane.showInputDialog(frame, "Congrats you achieved a high score of " + GameTimer.durationToString(duration)+ " minutes! \nEnter your name to write your place in history:");
        String user = "";
        String[] spaces = s.split(" ");
        for (int i = 0; i < spaces.length; i++) {
            String[] _split = spaces[i].split("_");
            for (int j = 0; j < _split.length; j++) {
                user += _split[j];
            }
        }
        gdp.insertScore(difficultyLevel, user, duration);
        gdp.saveData();
        return;
    }
    
    /**
     * Run minesweeper!
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    
}

