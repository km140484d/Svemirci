package settings;

import java.io.*;

public class Labels implements Serializable{
    private String start, victory, defeat;
    private String time, score, high_score;

    public Labels(String start, String victory, String defeat, String time, String score, String high_score) {
        this.start = start;
        this.victory = victory;
        this.defeat = defeat;
        this.time = time;
        this.score = score;
        this.high_score = high_score;
    }       

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getVictory() {
        return victory;
    }

    public void setVictory(String victory) {
        this.victory = victory;
    }

    public String getDefeat() {
        return defeat;
    }

    public void setDefeat(String defeat) {
        this.defeat = defeat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHigh_score() {
        return high_score;
    }

    public void setHigh_score(String high_score) {
        this.high_score = high_score;
    }
    
    
}
