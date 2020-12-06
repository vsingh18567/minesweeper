
/**
 * Class that stores the user and their associated score. Implements comparable to allow for the ordering
 * in the high-score table.
 * @author vikramsingh
 *
 */
public class ScoreData implements Comparable<ScoreData>{
    private String user;
    private int duration;
    
    public ScoreData(String user, int duration) {
        this.user = user;
        this.duration = duration;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    @Override
    public int compareTo(ScoreData dataPoint) {
        if (this.getDuration() == dataPoint.getDuration()) {
            return 0;
        } else if (this.getDuration() < dataPoint.getDuration()) {
            return -1;
        } else {
            return 1;
        }
    }
    
    @Override
    public String toString() {
        return this.user + this.duration;
    }
}
