package sprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import main.Main;

public class Star extends Sprite{

    private static double BOUND = 30;
    
    private Polygon body;
    
    private double velocityX = 1, velocityY  = 1;  
    private double width = BOUND, height = BOUND;
    
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
        body.setStroke(Color.WHITE);
        body.setStrokeWidth(1.5);
        getChildren().addAll(body);
    }

    @Override
    public void resizeWindow(double ratioWidth, double ratioHeight){
        super.resizeWindow(ratioWidth, ratioHeight);
        width*=ratioWidth;
        height*=ratioHeight;
        velocityX *= ratioWidth;
        velocityY *= ratioHeight;
    }
    
    @Override
    public void update() {
        if (getTranslateX() + velocityX < width / 2 + 5) {
            setTranslateX(width / 2 + 5);
            velocityX = - velocityX;             
        } else if (getTranslateX() + velocityX > Main.width - width / 2 - 5) {
            setTranslateX(Main.width - width / 2 - 5);
            velocityX = - velocityX;
        } else {
            setTranslateX(getTranslateX() + velocityX);
        }
        
        if (getTranslateY() + velocityY < height / 2 + 5) {
            setTranslateY(height / 2 + 5); 
            velocityY = - velocityY;       
        } else if (getTranslateY() + velocityY > Main.height - height/ 2 - 5) {
            setTranslateY(Main.height - height / 2 - 5);
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
