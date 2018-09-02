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
    
    protected static final double FACTOR = 2;
    
    protected static double size = SIDE;
    
    public Shot(double playerAngle) {
        velocityX = SHOT_VELOCITY*Math.tan(Math.toRadians(-playerAngle));
        setRotate(playerAngle);
    }
    
    public static void setEnlarge(boolean enlarge){
        if (enlarge)
            size = SIDE*FACTOR;
        else
            size = SIDE;
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
