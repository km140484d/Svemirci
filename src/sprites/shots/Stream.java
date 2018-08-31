package sprites.shots;

import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Stream extends Shot{
    
    
    public Stream(double playerAngle){
        super(playerAngle);
        base = new Circle(SIDE);
        base.setFill(Color.WHITE);
        getChildren().addAll(base);
    }
    
}
