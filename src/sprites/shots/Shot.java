package sprites.shots;

import javafx.scene.shape.*;
import main.Main;
import sprites.*;

public class Shot extends Sprite {

    protected static final double SHOT_VELOCITY = -5;
    
    protected Shape base;
    protected Shape center;
    
    protected static final double SIDE = 10;
    protected static final double RATIO = 0.4;
    
    protected double velocityX, velocityY = SHOT_VELOCITY;
    
    public Shot(double playerAngle) {
        velocityX = SHOT_VELOCITY*Math.tan(Math.toRadians(-playerAngle));
        setRotate(playerAngle);
    }
    
    @Override
    public void update() {
        double x = getTranslateX();
        double y = getTranslateY();
        
        if ((x + velocityX < SIDE) || (x + velocityX > Main.WINDOW_WIDTH - SIDE) || 
                (y + velocityY > Main.WINDOW_HEIGHT - SIDE) || (y + velocityY < SIDE)){
            Main.removeSprite(this);
        }else{
            setTranslateX(x + velocityX);
            setTranslateY(y + velocityY);
        }

    }
    
}
