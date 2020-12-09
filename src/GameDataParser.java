

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;
/**
 * The GameDataParser is the I/O class of the Minesweeper application. It interacts with a file,
 * which is always files/highscores.txt. It has read and write functionality, and is also able to 
 * check if a score is a new highscore.
 * @author vikramsingh
 *
 */
public class GameDataParser {
    private static final String DEFAULTGAME = 
            "god_0 nan2_3600 nan3_3600 nan4_3600 hello_44 hello2_24 hello2_24" + 
            "hellosir_42 VikramSingh_12 yasssssssqueens_22 god_0 nan2_3600 nan3_3600" + 
            "nan4_3600 nan5_3600 nan6_3600 nan7_3600 nan8_3600 nan9_3600 nan10_3600" + 
            "god_0 nan2_3600 nan3_3600 nan4_3600 nan5_3600 nan6_3600 nan7_3600 nan8_3600" + 
            "nan9_3600 nan10_3600 ";
    private String filepath;
    private ArrayList<ScoreData> easyScore;
    private ArrayList<ScoreData> mediumScore;
    private ArrayList<ScoreData> hardScore;
    // Maps integers to difficulties
    private HashMap<Integer, ArrayList<ScoreData>> difficultyMap;
    private String text;

    
    
    public GameDataParser(String filepath) {
        this.filepath = filepath;
        this.easyScore = new ArrayList<ScoreData>();
        this.mediumScore = new ArrayList<ScoreData>();
        this.hardScore = new ArrayList<ScoreData>();
        this.difficultyMap = new HashMap<Integer, ArrayList<ScoreData>>();
        this.difficultyMap.put(0, this.easyScore);
        this.difficultyMap.put(1,  this.mediumScore);
        this.difficultyMap.put(2,  this.hardScore);
        loadData();

    }
    
    /**
     * Saves the most updated version of the ArrayLists that are storing the scores for the three
     * difficulties. 
     */
    public void saveData() {
        String data = "";
        boolean startingRow = true;
        for (int i = 0; i < 3; i++) {
            ArrayList<ScoreData> difficultyData = this.difficultyMap.get(i);
            Iterator<ScoreData> iterator = difficultyData.iterator();
            while (iterator.hasNext()) {            
                ScoreData nextPoint = iterator.next();
                String user = nextPoint.getUser();
                String duration = Integer.toString(nextPoint.getDuration());
                data += user + "_" + duration + " ";
                
            }
        }
        // removes the space at the end.
        data.trim();        
        try {
            FileWriter output = new FileWriter(this.filepath, false);
            BufferedWriter w = new BufferedWriter(output);
            w.write(data);
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Helper function for the GameDataParser constructor. Reads the txt and 
     * loads the data into the three ArrayLists
     */
    private void loadData() {
        try {
            FileReader fr = new FileReader(this.filepath);
            BufferedReader r = new BufferedReader(fr);
            String input = r.readLine();
            if (input == null) {
                this.text = DEFAULTGAME;
            } else {
                this.text = input;
            }
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
            this.text = DEFAULTGAME;
        }   
        String[] dataPoints = this.text.split(" ");
        this.easyScore.clear();
        this.mediumScore.clear();
        this.hardScore.clear();
        for (int i = 0; i < dataPoints.length; i++) {
            String[] dataPoint = dataPoints[i].split("_");
            ScoreData scoreData = new ScoreData(dataPoint[0], Integer.parseInt(dataPoint[1]));
            this.difficultyMap.get((int) i / 10).add(scoreData);
        }
    }
    /**
     * Check if a score is a high score for a particular difficulty level
     * @param difficultyLevel
     * @param score
     * @return true if is new high score
     */
    public boolean isNewHighScore(int difficultyLevel, int score) {
        ArrayList<ScoreData> difficultyData = this.difficultyMap.get(difficultyLevel);
        ScoreData lowestScore = difficultyData.stream().sorted().collect(Collectors.toList()).
                get(difficultyData.size() - 1);
        return lowestScore.getDuration() > score / 1000;
    }
    
    /**
     * insert score into the relevant ArrayList
     * @param difficultyLevel 
     * @param user
     * @param score
     */
    public void insertScore(int difficultyLevel, String user, int score) {
        ArrayList<ScoreData> difficultyData = this.difficultyMap.get(difficultyLevel);
        difficultyData.remove(difficultyData.stream().
                sorted().collect(Collectors.toList()).get(difficultyData.size() - 1));
        ScoreData newScore = new ScoreData(user, score / 1000);
        difficultyData.add(newScore);
        this.difficultyMap.put(difficultyLevel, difficultyData);
    }
    
    /**
     * Get the data for a particular difficulty
     * @param difficulty
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<ScoreData> getData(int difficulty) {
        return (ArrayList<ScoreData>) this.difficultyMap.get(difficulty).clone();
    }
    
    
    
}
