package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Triangle extends Shot{
    
    private static final double INNER = SIDE*RATIO;
    public static final double ANGLE = 120;//60*2
    
    public Triangle(double playerAngle, double angle){
        super(playerAngle, angle);
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
