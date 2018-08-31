package sprites.shots;

import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Hexagon extends Shot{
    
    private static final double ROOT = SIDE / Math.sqrt(2);
    private static final double INNER = RATIO*SIDE;
    private static final double INNER_ROOT = INNER / Math.sqrt(2);
    
    public Hexagon(double playerAngle){
        super(playerAngle);
        base = new Polygon(0, -(SIDE/2 + ROOT), 
                ROOT, -SIDE/2, 
                ROOT, SIDE/2,
                0, SIDE/2 + ROOT,
                -ROOT, SIDE/2,
                -ROOT, -SIDE/2 );
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
