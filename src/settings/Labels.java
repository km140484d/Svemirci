package settings;

import java.io.*;

public class Labels implements Serializable{
    private String start, final_score, victory, defeat, life;
    private String time, points, high_score;//start, high_score
    
    private static Labels labels;
    
    private Labels(){}

//    private Labels(String start, String final_score, String victory, String defeat, String life, 
//            String time, String points, String high_score) {
//        this.start = start;
//        this.final_score = final_score;
//        this.victory = victory;
//        this.defeat = defeat;
//        this.life = life;
//        this.time = time;
//        this.points = points;
//        this.high_score = high_score;
//    } 
    
    public static Labels getInstance(){
        if (labels == null){
            labels = new Labels();
        }
        return labels;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinal_score() {
        return final_score;
    }

    public void setFinal_score(String final_score) {
        this.final_score = final_score;
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

    public String getLife() {
        return life;
    }

    public void setLife(String life) {
        this.life = life;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getHigh_score() {
        return high_score;
    }

    public void setHigh_score(String high_score) {
        this.high_score = high_score;
    }
    
    
}
