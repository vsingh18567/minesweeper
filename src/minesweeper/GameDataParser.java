package minesweeper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeSet;

public class GameDataParser {
    private static final String DEFAULTGAME = ""; 
    private String text;
    /* Format of text:
     * A block in the array is represented by x_y, where x indicates whether or not its discovered,
     * and y if its a bomb or not.
     * x: 1 if discovered, 0 if flagged, -1 if unchecked
     * y: 1 if bomb, 0 otherwise
     * 
     * Format of file itself
     * File: 'x1_y1 x2_y2 x3_y3 | x4_y4 x5_y5 x6_y6 | x7_y7 x8_y8 x9_y9'
     * | separates rows, the spaces separate columns
     */ 
    
    private BlockState[][] discoveredStates;
    private TreeSet<Integer> bombLocations;
    
    public GameDataParser(String filepath) {
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader r = new BufferedReader(fr);
            String input = r.readLine();
            if (input == null) {
                this.text = DEFAULTGAME;
            } else {
                this.text = input;
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.text = DEFAULTGAME;
        }   
        this.bombLocations = new TreeSet<Integer>();
    }
    
    public void saveData() {
        
    }
    
    private void loadData() {
        String[] rows = this.text.split("|");
        BlockState[][] newStates = new BlockState[rows.length][];
        for (int row = 0; row < rows.length; row++) {
            String[] cols = rows[row].split(" ");
            BlockState[] blockRow = new BlockState[cols.length];
            for (int col = 0; col < cols.length; col++) {
                String[] data = cols[col].split("_");
                switch (data[0]) {
                case "1":
                    blockRow[col] = BlockState.DISCOVERED;
                    break;
                case "0":
                    blockRow[col] = BlockState.FLAGGED;
                    break;
                case "-1":
                    blockRow[col] = BlockState.UNCHECKED;
                    break;
                }
                if (data[1] == "1") {
                    this.bombLocations.add(row * cols.length + col);
                }
            }
            newStates[row] = blockRow;
        }
        this.discoveredStates = newStates;
    }
    
    
}
