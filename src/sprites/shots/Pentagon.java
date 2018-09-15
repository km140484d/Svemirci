package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Pentagon extends Shot {
    
    protected static final double ANGLE = Main.constants.getShot_max_angle() / 4;
    
    private static final int PENT_STRENGTH = 2;
    
    public Pentagon(double playerAngle, double angle, boolean sizeUp){
        super(playerAngle, angle, PENT_STRENGTH,sizeUp);
        base = new Polygon(xCoord(1, 0), yCoord(1, 0),
                            xCoord(1, 1), yCoord(1, 1),
                            xCoord(1, 2), yCoord(1, 2),
                            xCoord(1, 3), yCoord(1, 3),
                            xCoord(1, 4), yCoord(1, 4));
        base.setFill(new ImagePattern(new Image("/resources/shots/pentagon.png")));
        base.setStroke(Color.WHITE);
        center = new Polygon(xCoord(RATIO, 0), yCoord(RATIO, 0),
                            xCoord(RATIO, 1), yCoord(RATIO, 1),
                            xCoord(RATIO, 2), yCoord(RATIO, 2),
                            xCoord(RATIO, 3), yCoord(RATIO, 3),
                            xCoord(RATIO, 4), yCoord(RATIO, 4));
        center.setFill(Color.WHITE);        
        getChildren().addAll(base, center);
    }
    
    public static double xCoord(double fact, int k){
        return SIDE*fact*Math.cos(k*2*Math.PI/5 - Math.PI/2);
    }
    
    public static double yCoord(double fact, int k){
        return SIDE*fact*Math.sin(k*2*Math.PI/5 - Math.PI/2);
    }
    
}
