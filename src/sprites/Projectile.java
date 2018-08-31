package sprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import main.Main;
import sprites.Sprite;
import static main.Main.WINDOW_HEIGHT;

public class Projectile extends Sprite{

    private Circle cBig, cMiddle, cSmall;
    private double velocityX = Math.tan(Math.toRadians(30)), velocityY = 1;
    private static final int RADIUS = 15;
    
    public Projectile(double x, double y){
        velocityX = 1 + Math.random()*3;
        if (Math.random() < 0.5)
            velocityX = -velocityX;
        cBig = new Circle(RADIUS);
        cBig.setFill(Color.CHARTREUSE);
        cBig.setStrokeWidth(1.2);
        cMiddle = new Circle(RADIUS/3*2);
        cMiddle.setFill(Color.LIME);
        cMiddle.setStrokeWidth(1.2);
        cSmall = new Circle(RADIUS/3);
        cSmall.setFill(Color.BLUEVIOLET);
        cSmall.setStrokeWidth(1.2);
        getChildren().addAll(cBig, cMiddle, cSmall);
        setTranslateX(x);
        setTranslateY(y);
    }
    
    @Override
    public void update() {
        if ((getTranslateY() + velocityY) > WINDOW_HEIGHT)
                        Main.removeSprite(this);
        else{
            if (getTranslateX() + velocityX < RADIUS/2 - 5){
                setTranslateX(RADIUS/2 - 5);
                velocityX = -velocityX;
            }else{
                if (getTranslateX() + velocityX > Main.WINDOW_WIDTH - RADIUS/2 - 5){
                    setTranslateX(Main.WINDOW_WIDTH - RADIUS/2 - 5);
                    velocityX = -velocityX;
                }else{
                    setTranslateX(getTranslateX() + velocityX);
                }
            }
            setTranslateY(getTranslateY() + velocityY);
        }
    }
    
}
