package sprites.awards;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;
import sprites.Sprite;

public class Coin extends Sprite {

    private static final int SIZE = 10;     
    private Circle body;
    private double velocityX, velocityY = 1;
    
    private static final ImagePattern texture;
    static{
        texture = new ImagePattern(new Image("/resources/awards/bit_coin.gif"));
    }
    
    public Coin(double x, double y){
        velocityX = Math.tan(Math.toRadians(-15 + Math.random() * 30));
        body = new Circle(SIZE);
        body.setFill(texture);        
        getChildren().addAll(body);
        setTranslateX(x);
        setTranslateY(y);
    }
    
    @Override
    public void update() {
        if ((getTranslateX() + velocityX) < 0 || (getTranslateX() + velocityX) > Main.getWidth() ||
                            (getTranslateY() + velocityY) > Main.getHeight())
            Main.removeSprite(this);
        else{
            setTranslateX(getTranslateX() + velocityX);
            setTranslateY(getTranslateY() + velocityY);
        }
                                
    }
}
