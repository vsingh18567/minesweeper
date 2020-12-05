package minesweeper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class GameDataParser {
    private static final String DEFAULTGAME = "";
    private String filepath;
    private ArrayList<ScoreData> easyScore;
    private ArrayList<ScoreData> mediumScore;
    private ArrayList<ScoreData> hardScore;
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
    
    
    public void saveData() {
        String data = "";
        boolean startingRow = true;
        for (int i=0; i < 3; i++) {
            ArrayList<ScoreData> difficultyData = this.difficultyMap.get(i);
            Iterator<ScoreData> iterator = difficultyData.iterator();
            while (iterator.hasNext()) {            
                ScoreData nextPoint = iterator.next();
                String user = nextPoint.getUser();
                String duration = Integer.toString(nextPoint.getDuration());
                data += user + "_" + duration + " ";
                
            }
        }
        data.trim();

        System.out.println(data);
        
        try {
            FileWriter output = new FileWriter(this.filepath, false);
            BufferedWriter w = new BufferedWriter(output);
            w.write(data);
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
                
        
    }
    
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
    
    public boolean isNewHighScore(int difficultyLevel, int score) {
        ArrayList<ScoreData> difficultyData = this.difficultyMap.get(difficultyLevel);
        ScoreData lowestScore = difficultyData.stream().sorted().collect(Collectors.toList()).get(difficultyData.size() - 1); // highest duration
        if (lowestScore.getDuration() > score) {
            return true;
        } else {
            return false;
        }
    }
    
    public void insertScore(int difficultyLevel, String user, int score) {
        ArrayList<ScoreData> difficultyData = this.difficultyMap.get(difficultyLevel);
        difficultyData.remove(difficultyData.stream().sorted().collect(Collectors.toList()).get(difficultyData.size() - 1));
        ScoreData newScore = new ScoreData(user, score);
        difficultyData.add(newScore);
        this.difficultyMap.put(difficultyLevel, difficultyData);
    }
    
   public ArrayList<ScoreData> getData(int difficulty) {
       return this.difficultyMap.get(difficulty);
   }
    
    
    
}
