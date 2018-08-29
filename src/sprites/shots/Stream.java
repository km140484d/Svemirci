package sprites.shots;

import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Stream extends Shot{
    
    
    public Stream(){
        base = new Circle(SIDE);
        base.setFill(Color.WHITE);
        getChildren().addAll(base);
    }
    
}
