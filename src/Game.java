

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * Minesweeper application developed by Vikram Singh as Homework 9 of CIS 120, Fall 2020.
 */
public class Game implements Runnable {
    public static final JFrame FRAME = new JFrame("Minesweeper");
    public static final GridBagConstraints GBC = new GridBagConstraints();
    
    /**
     * Sets up basic frame settings and triggers home screen
     */
    public void run() {
        FRAME.setLayout(new GridBagLayout());
        FRAME.setResizable(false);
        FRAME.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        FRAME.setSize(800,800);
        FRAME.setVisible(true);
        FRAME.setBackground(Color.PINK);
        homeScreen();
        // Initial screen
    }
    
    /**
     * Home screen that contains title, buttons that lead to "New Game", "View High Scores" and instructions.
     */
    private void homeScreen() {
        FRAME.getContentPane().removeAll();
        FRAME.getContentPane().setBackground(Color.PINK);

        GBC.insets = new Insets(3, 3, 3, 3);
        GBC.ipady = 10;

        JLabel title = new JLabel("Minesweeper");
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 40));
        GBC.gridx = 0;
        GBC.gridy = 0;
        GBC.gridwidth = 2;
        FRAME.add(title, GBC);
        
        JLabel difficulty = new JLabel("Difficulty: ");
        difficulty.setFont(new Font(difficulty.getFont().getFontName(), Font.PLAIN, 18));
        GBC.gridx = 0;
        GBC.gridy = 2;
        GBC.gridwidth = 1;
        FRAME.add(difficulty, GBC);
        
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox difficultyList = new JComboBox<String>(difficulties);
        difficultyList.setFont(new Font(difficultyList.getFont().getFontName(), Font.PLAIN, 18));
        GBC.gridx = 1;
        GBC.gridy = 2;
        FRAME.add(difficultyList, GBC);
        
        
        JButton newGame = new JButton("New Game");
        newGame.setFont(new Font(newGame.getFont().getFontName(), Font.PLAIN, 18));
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame((String) difficultyList.getSelectedItem());
            }
        });
        GBC.gridx = 0;
        GBC.gridy = 3;
        GBC.gridwidth = 2;
        FRAME.add(newGame, GBC);
        
        JButton highScores = new JButton("View High Scores");
        highScores.setFont(new Font(highScores.getFont().getFontName(), Font.PLAIN, 18));
        highScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHighScores();
            }
        });
        GBC.gridx = 0;
        GBC.gridy = 4;
        FRAME.add(highScores, GBC);
        
        JButton instructions = new JButton("Instructions");
        instructions.setFont(new Font(instructions.getFont().getFontName(), Font.PLAIN, 18));
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadInstructions();
            }
        });
        GBC.gridx = 0;
        GBC.gridy = 5;
        FRAME.add(instructions, GBC);
        
        JLabel credits = new JLabel("Made by Vikram Singh - Penn ID: 40121920");
        GBC.gridx = 0;
        GBC.gridy = 6;
        FRAME.add(credits, GBC);
        FRAME.validate();        
        FRAME.repaint();
        
    }
    
    /**
     * Main game screen - has all the core logic that's handled through the Grid and the related buttons.
     * Triggered by home screen.
     * @param gameType: String -- "Easy", "Medium", "Hard"
     */
    @SuppressWarnings("unchecked")
    private void loadGame(String gameType) {
        FRAME.getContentPane().removeAll();        
        
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
        
        
        GBC.gridx = 0;
        GBC.gridy = 0;
        GBC.gridwidth = 6;
        FRAME.add(grid, GBC);
        
        GBC.gridwidth = 1;

        final GameTimer timer = new GameTimer();
        timer.setFont(new Font(timer.getFont().getFontName(), Font.PLAIN, 20));
        GBC.gridx = 4;
        GBC.gridy = 1;
        FRAME.add(timer, GBC);

    
        
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {loadGame(gameType);}
        });
        reset.setFont(new Font(reset.getFont().getFontName(), Font.PLAIN, 20));
        GBC.gridx = 0;
        GBC.gridy = 1;
        FRAME.add(reset, GBC);
        
        
        final JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FRAME.getContentPane().removeAll();
                homeScreen();
            }
        });
        back.setFont(new Font(back.getFont().getFontName(), Font.PLAIN, 20));
        GBC.gridx = 2;
        GBC.gridy = 1;
        FRAME.add(back, GBC);
        
        final JButton pausePlay = new JButton("Pause/Play");
        pausePlay.setFont(new Font(pausePlay.getFont().getFontName(), Font.PLAIN, 20));
        boolean isPaused = false;    
        GBC.gridx = 3;
        GBC.gridy = 1;
        FRAME.add(pausePlay, GBC);
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
                    FRAME.remove(grid);
                } else if (grid.getGameStatus() == GameStatus.PAUSED) {
                    grid.setGameStatus(GameStatus.PLAYING);
                    GBC.gridx = 0;
                    GBC.gridy = 0;
                    GBC.gridwidth = 6;
                    FRAME.add(grid, GBC);
                    grid.repaint();
                }
                timer.pause();
                FRAME.validate();
                FRAME.repaint();
            }
        });
        
        int bombsLeftNum = grid.getBombsLeft();

        final JLabel bombsLeft = new JLabel("Mines left: " + bombsLeftNum);
        bombsLeft.setFont(new Font(bombsLeft.getFont().getFontName(), Font.PLAIN, 20));
        GBC.gridx = 5;
        GBC.gridy = 1;
        FRAME.add(bombsLeft, GBC);
        
          
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
        FRAME.validate();
        FRAME.repaint();
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
                viewHighScores();
            } else {
                JOptionPane.showMessageDialog(FRAME, "You won in " + GameTimer.durationToString(duration) + " minutes. \n Not quite fast enough for a high score!");
                FRAME.repaint();
            }
            return true;

        } else if (g.getGameStatus() == GameStatus.GAMELOST) {
            timer.pause();
            JOptionPane.showMessageDialog(FRAME, "Unlucky, you lost. Try again!");
            return true;
        }
        
        return false;
    }
    
    /**
     * Loads the instruction screen. Formatted in HTML
     */
    private void loadInstructions() {        
        FRAME.getContentPane().removeAll();
        JLabel instructions = new JLabel("<html>Instructions<br>");
        instructions.setFont(new Font(instructions.getFont().getFontName(), Font.BOLD, 25));
        GBC.gridx = 0;
        GBC.gridy = 0;
        GBC.gridwidth = 3;
        FRAME.add(instructions, GBC);
        
        String html = "<html><div style=\"font-size: 14; text-align: justify\">"
                .concat("Everyone's played Minesweeper before, but this is Minesweeper <strong>in Pink</strong><br>")
                .concat("and that makes it better! To start a new game, choose a difficulty. If<br>")
                .concat("you haven't played before, go with easy. It's really really easy.<br><br>")
                .concat("The goal of the game is to find all the mines. Click on a block. If it's<br>")
                .concat("a mine, that's game over! If not, then well done! The 'safe' blocks <br>")
                .concat("show the number of neighbouring bombs. If you suspect that a block is<br>")
                .concat("a bomb, <b>right-click</b> that block. If you think it's safe, take a risk,<br>")
                .concat("and <b>left-click</b>. The game ends when you hit a 'bomb' and lose, or you<br>")
                .concat("clear all the blocks (if you do it fast enough, you might make it onto the <br>")
                .concat("leaderboard). It's not over till you see a pop-up!<br><br>")
                .concat("If you need a break, just click pause. If you're paused, your timer stops<br>")
                .concat("and the grid disappears. When you're ready to continue, click the same<br>")
                .concat("button and the clock starts again.<br><br>")
                .concat("If you want to just give up, click reset and just start again. Want to <br>")
                .concat("move up a level? Go back to the home page and change the difficulty.<br><br>")
                .concat("Feeling a bit competitive? Check the high scores to see if your name<br>")
                .concat("is on the list. If not, try and complete the game faster than the times <br>")
                .concat("on that list. <br><br>")
                .concat("Have fun playing! And if you see a 'problem', it's not a bug, it's a")
                .concat("<br>feature :)</br></div></html>");
        JLabel textArea = new JLabel(html);
        GBC.gridx = 0;
        GBC.gridy = 1;
        FRAME.add(textArea, GBC);
        

        
        final JButton back = new JButton("Back");
        back.setFont(new Font(back.getFont().getFontName(), Font.PLAIN, 18));
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FRAME.getContentPane().removeAll();
                homeScreen();
            }
        });
        GBC.gridx = 0;
        GBC.gridy = 2;
        FRAME.add(back, GBC);
        
        String funFact = "<html> <i> Fun fact: When you click on a block with no bomb neighbours, suddenly the<br>" +
                "neighbours of that block all 'cascade' and get clicked too. That's called the <br> Flood Fill algorithm.</i>";
        
        final JLabel funFactLabel = new JLabel(funFact);
        GBC.gridx = 0;
        GBC.gridy = 4;
        FRAME.add(funFactLabel, GBC);
        
        FRAME.validate();
        FRAME.repaint();
    }
    
    /**
     * View the high scores. Mainly uses the JTable component. Pulls from the GameDataParser
     */
    private void viewHighScores() {
        GBC.insets = new Insets(6, 6, 6, 6);
        GBC.ipady = 10;
        FRAME.getContentPane().removeAll();
        GameDataParser gdp = new GameDataParser("files/highscores.txt");
        String[] difficultyNames = {"Easy", "Medium", "Hard"};
        
        JLabel header = new JLabel("High Scores");
        header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, 25));
        GBC.gridx = 0;
        GBC.gridy = 0;
        GBC.gridwidth = 6;
        FRAME.add(header, GBC);
        
        GBC.gridwidth = 1;
        for (int i = 0; i < 3; i++) {
            String difficulty = difficultyNames[i];
            GBC.gridx = 2 * i;
            GBC.gridy = 1;
            JLabel difficultyLabel = new JLabel(difficulty);
            difficultyLabel.setFont(new Font(difficultyLabel.getFont().getFontName(), Font.PLAIN, 18));
            FRAME.add(difficultyLabel, GBC);
            
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
            table.setRowHeight(30);
            table.getColumnModel().getColumn(0).setPreferredWidth(200);
            table.getColumnModel().getColumn(1).setPreferredWidth(50);

            GBC.gridx = 2 * i;
            GBC.gridy = 2;
            FRAME.add(table, GBC);
        }
        
        final JButton back = new JButton("Back");
        back.setFont(new Font(back.getFont().getFontName(), Font.PLAIN, 20));
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FRAME.getContentPane().removeAll();
                homeScreen();
            }
        });
        GBC.gridx = 2;
        GBC.gridy = 3;
        FRAME.add(back, GBC);
        
        FRAME.validate();
        FRAME.repaint();
        
        
    }
    
    /**
     * Helper function for Game.tick() to handle a new high score.
     * Utilises the GameDataParser and the GameTimer.durationToString() function.
     * @param gdp
     * @param difficultyLevel
     * @param duration
     */
    private void highScorePopUp(GameDataParser gdp, int difficultyLevel, int duration) {
        String s = (String)JOptionPane.showInputDialog(FRAME, "Congrats you achieved a high score of " + GameTimer.durationToString(duration)+ " minutes! \nEnter your name to write your place in history:");
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


