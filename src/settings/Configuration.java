package settings;

import java.io.*;

public class Configuration implements Serializable{
    
    private String name;
    private double width, height;
    private int columns, rows;
    private Position[] scouts;
    private Position[] warriors;
    private Position[] commanders;

    public Configuration(String name, double width, double height, int columns, int rows) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.columns = columns;
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Position[] getScouts() {
        return scouts;
    }

    public void setScouts(Position[] scouts) {
        this.scouts = scouts;
    }

    public Position[] getWarriors() {
        return warriors;
    }

    public void setWarriors(Position[] warriors) {
        this.warriors = warriors;
    }

    public Position[] getCommanders() {
        return commanders;
    }

    public void setCommanders(Position[] commanders) {
        this.commanders = commanders;
    }
    
    
    
}
