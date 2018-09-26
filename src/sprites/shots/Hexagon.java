package sprites.shots;

import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Hexagon extends Shot{
    
    protected static final double ANGLE = Main.constants.getShot_max_angle() / 8;    
    private static final double ROOT = SIDE / Math.sqrt(2);
    private static final double INNER = RATIO*SIDE;
    private static final double INNER_ROOT = INNER / Math.sqrt(2);    
    private static final int HEX_STRENGTH = 2;
    
    protected static final ImagePattern hex;     
    static{
        hex = new ImagePattern(new Image("/resources/shots/hexagon.png"));
    }
    
    public Hexagon(double playerAngle, double angle, boolean sizeUp){
        super(playerAngle, angle, HEX_STRENGTH, sizeUp);
        base = new Polygon(0, -(SIDE/2 + ROOT), 
                ROOT, -SIDE/2, 
                ROOT, SIDE/2,
                0, SIDE/2 + ROOT,
                -ROOT, SIDE/2,
                -ROOT, -SIDE/2 );
        base.setFill(hex);
        //base.setFill(Color.RED);
        base.setStroke(Color.WHITE);
        
        center = new Polygon(0, -(INNER/2 + INNER_ROOT),
                INNER_ROOT, -INNER/2,
                INNER_ROOT, INNER/2,
                0, INNER/2 + INNER_ROOT,
                -INNER_ROOT, INNER/2,
                -INNER_ROOT, -INNER/2
        );
        center.setFill(Color.WHITE);        
        getChildren().addAll(base,center);
    }    
}
