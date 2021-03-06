package sprites.shots;

import javafx.scene.shape.*;
import javafx.scene.transform.*;
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
    protected int shotStrength;

    public interface ShotAngle{ double getAngle(); }    
    public static enum BasicShotType implements ShotAngle{
        Tri{
            @Override
            public double getAngle(){ return Triangle.ANGLE;} 
        }, Rho{
            @Override
            public double getAngle(){ return Rhombus.ANGLE;}            
        }, Pen{
            @Override
            public double getAngle(){ return Pentagon.ANGLE;}
        }, Hex{
            @Override
            public double getAngle(){ return Hexagon.ANGLE;}
        }
    };

    public Shot(double playerAngle, double angle, int strength, boolean sizeUp) {
        this.shotStrength = strength;
        velocityX = -SHOT_VELOCITY*Math.cos(Math.toRadians(-playerAngle + 90 - angle));
        velocityY = SHOT_VELOCITY*Math.sin(Math.toRadians(-playerAngle + 90 - angle));
        setRotate(playerAngle);
        if (sizeUp){
            Scale scale = new Scale(2,2);
            getTransforms().add(scale);
        }
    }
    
    public int getShotStrength(){
        return shotStrength;
    }
    
    @Override
    public void update() {
        double x = getTranslateX();
        double y = getTranslateY();        
        if ((x + velocityX < SIDE) || (x + velocityX > Main.getWidth() - SIDE) || 
                (y + velocityY > Main.getHeight() - SIDE) || (y + velocityY < SIDE)){
            Main.removeSprite(this);
        }else{
            setTranslateX(x + velocityX);
            setTranslateY(y + velocityY);
        }
    }
}
