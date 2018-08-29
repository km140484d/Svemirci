package sprites.shots;

import javafx.scene.shape.*;
import sprites.*;

public class Shot extends Sprite {

    private static final double SHOT_VELOCITY = -5;
    
    protected Shape base;
    protected Shape center;
    
    protected static final double SIDE = 10;
    protected static final double RATIO = 0.4;
    
    public Shot() {
    }
    
    @Override
    public void update() {
        setTranslateY(getTranslateY() + SHOT_VELOCITY);
    }
    
}
