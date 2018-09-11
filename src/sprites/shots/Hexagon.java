package sprites.shots;

import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Hexagon extends Shot{
    
    public static final double ANGLE = Main.constants.getShot_max_angle() / 8;
    
    private static final double ROOT = size / Math.sqrt(2);
    private static final double INNER = RATIO*size;
    private static final double INNER_ROOT = INNER / Math.sqrt(2);
    
    private static final int HEX_STRENGTH = 2;
    
    public Hexagon(double playerAngle, double angle){
        super(playerAngle, angle, HEX_STRENGTH);
        base = new Polygon(0, -(size/2 + ROOT), 
                ROOT, -size/2, 
                ROOT, size/2,
                0, size/2 + ROOT,
                -ROOT, size/2,
                -ROOT, -size/2 );
        base.setFill(new ImagePattern(new Image("/resources/shots/hexagon.png")));
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
