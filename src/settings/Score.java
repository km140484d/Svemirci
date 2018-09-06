package settings;

import java.io.*;

public class Score implements Serializable {
    
    private String name;
    
    private int points;
    
    private int time;

    public Score(String name, int points, int time) {
        this.name = name;
        this.points = points;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
    
    
    
}
