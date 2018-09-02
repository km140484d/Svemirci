package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Triangle extends Shot{
    
    private static final double INNER = SIDE*RATIO;
    
    public Triangle(double playerAngle){
        super(playerAngle);
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
