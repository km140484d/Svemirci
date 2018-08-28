package sprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import main.Main;

public class Star extends Sprite{

    private Polygon body;
    private Rectangle boundary;
    
    private double velocityX = 1, velocityY  = 1;  
    
    public Star(){        
        //kao 2 pentagona, unutrasnji manji pentagon je za 180 stepeni rotiran
        body = new Polygon(xCoord(2, 0), yCoord(2, 0),
                            -xCoord(1, 3), -yCoord(1, 3),
                            xCoord(2, 1), yCoord(2, 1),
                            -xCoord(1, 4), -yCoord(1, 4),
                            xCoord(2, 2), yCoord(2, 2),
                            -xCoord(1, 0), -yCoord(1, 0),
                            xCoord(2, 3), yCoord(2, 3),
                            -xCoord(1, 1), -yCoord(1, 1),
                            xCoord(2, 4), yCoord(2, 4),
                            -xCoord(1, 2), -yCoord(1, 2));
        body.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        boundary = new Rectangle(30, 30);
        boundary.setTranslateX(-15);
        boundary.setTranslateY(-15);
        boundary.setFill(Color.TRANSPARENT);
        getChildren().addAll(boundary, body);
    }

        
    @Override
    public void update() {
        if (getTranslateX() + velocityX < boundary.getWidth() / 2 + 5) {
            setTranslateX(boundary.getWidth() / 2 + 5);
            velocityX = - velocityX;             
        } else if (getTranslateX() + velocityX > Main.WINDOW_WIDTH - boundary.getWidth() / 2 - 5) {
            setTranslateX(Main.WINDOW_WIDTH - boundary.getWidth() / 2 - 5);
            velocityX = - velocityX;
        } else {
            setTranslateX(getTranslateX() + velocityX);
        }
        
        if (getTranslateY() + velocityY < boundary.getHeight() / 2 + 5) {
            setTranslateY(boundary.getHeight() / 2 + 5); 
            velocityY = - velocityY;       
        } else if (getTranslateY() + velocityY > Main.WINDOW_HEIGHT - boundary.getHeight() / 2 - 5) {
            setTranslateY(Main.WINDOW_HEIGHT - boundary.getHeight() / 2 - 5);
            velocityY = - velocityY; 
        } else {
            setTranslateY(getTranslateY() + velocityY);
        }
        
    }
    
    public static double xCoord(int fact, int k){
        //System.out.println("x-coord: " + fact*8.1*Math.cos(k*2*Math.PI/5));
        return fact*8.1*Math.cos(k*2*Math.PI/5);
    }
    
    public static double yCoord(int fact, int k){
        //System.out.println("y-coord: " + fact*8.1*Math.sin(k*2*Math.PI/5));
        return fact*8.1*Math.sin(k*2*Math.PI/5);
    }
}
