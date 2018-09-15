package settings;

import java.io.*;
import javafx.scene.paint.Color;

public class Score implements Serializable {
    
    private Color color;
    private String name;    
    private int points;    
    private int time;

    public Score(Color color, String name, int points, int time) {
        this.color = color;
        this.name = name;
        this.points = points;
        this.time = time;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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
