package sprites;

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Projectile extends Sprite{

    private Circle cBig, cMiddle, cSmall;
    private double velocityX = Math.tan(Math.toRadians(30)), velocityY = 1; //cos
    private static final int RADIUS = 15;
    
    public Projectile(double x, double y){
        velocityX = 1 + Math.random()*3;
        if (Math.random() < 0.5)
            velocityX = -velocityX;
        cBig = new Circle(RADIUS);
        cBig.setFill(Color.CHARTREUSE);
        cBig.setStrokeWidth(1.2);
        cBig.setStroke(Color.BLACK);
        cMiddle = new Circle(RADIUS/3*2);
        cMiddle.setFill(Color.LIME);
        cMiddle.setStrokeWidth(1.2);
        cMiddle.setStroke(Color.BLACK);
        cSmall = new Circle(RADIUS/3);
        cSmall.setFill(Color.BLUEVIOLET);
        cSmall.setStrokeWidth(1.2);
        cSmall.setStroke(Color.BLACK);
        getChildren().addAll(cBig, cMiddle, cSmall);
        setTranslateX(x);
        setTranslateY(y);
    }
    
    public void setPlayerCoordinates(double playerX, double playerY){
        double x = (playerX - getTranslateX());
        double y = playerY - getTranslateY();
        double alpha1 = Math.atan(x/y); //proj above player
        double alpha2 = Math.atan(Math.abs(y/x)); //proj below player
        if (getTranslateY() > playerY){
            if (playerX < getTranslateX())
                this.velocityX = -2*Math.cos(alpha2);
            else
                this.velocityX = 2*Math.cos(alpha2);
            this.velocityY = 2*Math.sin(-Math.abs(alpha2));
        }else{
            this.velocityX = 2*Math.sin(alpha1);
            this.velocityY = 2*Math.cos(alpha1);
        }
    }
    
    @Override
    public void update() {    
        if (((getTranslateY() + velocityY) > Main.getWidth()) || ((getTranslateY() + velocityY) < 0))
            Main.removeSprite(this);
        else{
            if (getTranslateX() + velocityX < RADIUS/2 - 5){
                setTranslateX(RADIUS/2 - 5);
                velocityX = -velocityX;
            }else{
                if (getTranslateX() + velocityX > Main.getWidth() - RADIUS/2 - 5){
                    setTranslateX(Main.getWidth() - RADIUS/2 - 5);
                    velocityX = -velocityX;
                }else{
                    setTranslateX(getTranslateX() + velocityX);
                }
            }
            setTranslateY(getTranslateY() + velocityY);
        }
    }
}
