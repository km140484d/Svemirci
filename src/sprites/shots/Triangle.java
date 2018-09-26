package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Triangle extends Shot{
    
    protected static final double ANGLE = Main.constants.getShot_max_angle(); //1    
    private static final double INNER = SIDE*RATIO;    
    private static final int TRI_STRENGTH = 1;
    
    protected static final ImagePattern tri;     
    static{
        tri = new ImagePattern(new Image("/resources/shots/triangle.png"));
    }
 
    public Triangle(double playerAngle, double angle, boolean sizeUp){
        super(playerAngle, angle, TRI_STRENGTH, sizeUp);
        base = new Polygon(0, -SIDE*3/4,
                SIDE*3/4, SIDE*3/4,
                -SIDE*3/4, SIDE*3/4
        );
        base.setFill(tri);
        //base.setFill(Color.DARKSEAGREEN);
        base.setStroke(Color.WHITE);
        
        center = new Polygon(0, -INNER*3/4,
                INNER*3/4, INNER*3/4,
                -INNER*3/4, INNER*3/4
        );
        center.setFill(Color.WHITE);
        
        getChildren().addAll(base, center);
    }
    
}
