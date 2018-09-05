package sprites.shots;

import javafx.scene.shape.*;
import main.Main;
import sprites.*;

public class Shot extends Sprite {

    protected static final double SHOT_VELOCITY = -5;
    
    protected Shape base;
    protected Shape center;
    
    protected static final double SIDE = 10;
    private static double side = SIDE;
    protected static final double RATIO = 0.4;
    
    protected double velocityX, velocityY = SHOT_VELOCITY;
    
    protected static final double FACTOR = 2;
    
    protected static double size = SIDE;
    
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
            public double getAngle(){ return Hexagon.ANGLE; }
        }
    };

    public Shot(double playerAngle, double angle, int strength) {
        this.shotStrength = strength;
        velocityX = -SHOT_VELOCITY*Math.cos(Math.toRadians(-playerAngle + 90 - angle));
        velocityY = SHOT_VELOCITY*Math.sin(Math.toRadians(-playerAngle + 90 - angle));
        setRotate(playerAngle);
    }
    
    public int getShotStrength(){
        return shotStrength;
    }
    
    public static void setEnlarge(boolean enlarge){
        if (enlarge)
            size = side*FACTOR;
        else
            size = side;
    }
    
    @Override
    public void resizeWindow(double windowWidth, double windowHeight){
        super.resizeWindow(windowWidth, windowHeight);
        side*=windowWidth/windowHeight;
    }
    
    @Override
    public void update() {
        double x = getTranslateX();
        double y = getTranslateY();
        
        if ((x + velocityX < SIDE) || (x + velocityX > Main.width - SIDE) || 
                (y + velocityY > Main.height - SIDE) || (y + velocityY < SIDE)){
            Main.removeSprite(this);
        }else{
            setTranslateX(x + velocityX);
            setTranslateY(y + velocityY);
        }

    }
    
}
