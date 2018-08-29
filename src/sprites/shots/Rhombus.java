package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Rhombus extends Shot{
    
    private static final double WIDTH = SIDE*2/3;
    private static final double INNER = SIDE*RATIO;
    private static final double INNER_WIDTH = INNER*2/3;
    
    public Rhombus(){
        base = new Polygon( 0, -SIDE,
                WIDTH, 0,
                0, SIDE,
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
