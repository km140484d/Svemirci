package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Triangle extends Shot{
    
    public static final double ANGLE = Main.constants.getShot_max_angle(); //1
    
    private static final double INNER = SIDE*RATIO;
    
    private static final int TRI_STRENGTH = 1;
    
    public Triangle(double playerAngle, double angle){
        super(playerAngle, angle, TRI_STRENGTH);
        base = new Polygon(0, -size*3/4,
                size*3/4, size*3/4,
                -size*3/4, size*3/4
        );
        base.setFill(new ImagePattern(new Image("/resources/shots/triangle1.png")));
        base.setStroke(Color.WHITE);
        
        center = new Polygon(0, -INNER*3/4,
                INNER*3/4, INNER*3/4,
                -INNER*3/4, INNER*3/4
        );
        center.setFill(Color.WHITE);
        
        getChildren().addAll(base, center);
    }
    
}
