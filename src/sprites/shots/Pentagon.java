package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Pentagon extends Shot {
    
    public Pentagon(double playerAngle){
        super(playerAngle);
        base = new Polygon(xCoord(1, 0), yCoord(1, 0),
                            xCoord(1, 1), yCoord(1, 1),
                            xCoord(1, 2), yCoord(1, 2),
                            xCoord(1, 3), yCoord(1, 3),
                            xCoord(1, 4), yCoord(1, 4));
        base.setFill(new ImagePattern(new Image("/resources/shots/pentagon1.png")));
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
        //System.out.println("x-coord: " + fact*8.1*Math.cos(k*2*Math.PI/5));
        return SIDE*fact*Math.cos(k*2*Math.PI/5 - Math.PI/2);
    }
    
    public static double yCoord(double fact, int k){
        //System.out.println("y-coord: " + fact*8.1*Math.sin(k*2*Math.PI/5));
        return SIDE*fact*Math.sin(k*2*Math.PI/5 - Math.PI/2);
    }
    
}
