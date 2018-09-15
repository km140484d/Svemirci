package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Stream extends Shot{
    
    private static final int STR_STRENGTH = 3;

    public Stream(double playerAngle, boolean sizeUp){
        super(playerAngle, 0, STR_STRENGTH, sizeUp);
        base = new Circle(SIDE*2);
        base.setFill(new ImagePattern(new Image("/resources/shots/bowling.png")));
        getChildren().addAll(base);
    }
    
}
