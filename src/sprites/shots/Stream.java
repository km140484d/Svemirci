package sprites.shots;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Stream extends Shot{

    public Stream(double playerAngle){
        super(playerAngle);
        base = new Circle(SIDE*2);
        base.setFill(new ImagePattern(new Image("/resources/shots/bowling.png")));
        getChildren().addAll(base);
    }
    
}
