package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Rhombus extends Shot{
    
    private static final double WIDTH = size*2/3;
    private static final double INNER = size*RATIO;
    private static final double INNER_WIDTH = INNER*2/3;
    public static final double ANGLE = 90; //2*45
    
    public Rhombus(double playerAngle, double angle){
        super(playerAngle, angle);
        base = new Polygon( 0, -size,
                WIDTH, 0,
                0, size,
                -WIDTH, 0                
        );
        base.setFill(new ImagePattern(new Image("/resources/shots/rhombus1.png")));
        base.setStroke(Color.WHITE);
        
        center = new Polygon( 0, -INNER,
                INNER_WIDTH, 0,
                0, INNER,
                -INNER_WIDTH, 0                
        );
        center.setFill(Color.WHITE);
        
        getChildren().addAll(base, center);
    }
    
}
