package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Rhombus extends Shot{
    
    protected static final double ANGLE = Main.constants.getShot_max_angle()/2; //2*45
    
    private static final double WIDTH = SIDE*2/3;
    private static final double INNER = SIDE*RATIO;
    private static final double INNER_WIDTH = INNER*2/3;

    private static final int RHOMB_STRENGTH = 1;
    
    public Rhombus(double playerAngle, double angle, boolean sizeUp){
        super(playerAngle, angle, RHOMB_STRENGTH, sizeUp);
        base = new Polygon( 0, -SIDE,
                WIDTH, 0,
                0, SIDE,
                -WIDTH, 0                
        );
        base.setFill(new ImagePattern(new Image("/resources/shots/rhombus.png")));
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
