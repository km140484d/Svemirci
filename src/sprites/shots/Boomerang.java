package sprites.shots;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import static sprites.shots.Shot.SIDE;

public class Boomerang extends Shot{
 
    public Boomerang(){
        base = new Circle(SIDE);
        base.setFill(Color.PINK);
        getChildren().addAll(base);
    }
    
}
