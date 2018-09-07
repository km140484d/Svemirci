package settings;

import java.io.Serializable;

public class Position implements Serializable{
    
    private double x, y;
    private String commanders;
    private boolean last;

    public Position(double x, double y, String commanders, boolean last) {
        this.x = x;
        this.y = y;
        this.commanders = commanders;  
        this.last = last;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getCommanders() {
        return commanders;
    }

    public void setCommanders(String commanders) {
        this.commanders = commanders;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
    
}
