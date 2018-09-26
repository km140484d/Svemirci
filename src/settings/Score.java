package settings;

import java.io.*;
import sprites.Player.Type;

public class Score implements Serializable {
    
    private Type type;
    private String name;    
    private int points, time;  

    public Score(Type type, String name, int points, int time) {
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
