package sprites.indicators;

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import sprites.Sprite;

public class Life extends Sprite{
    
    private static final double SIZE = 6;
    private static final double CHEEK_WIDTH = 2;
    public static final double POINTS = 50;    
    
    public Life(){ 
        Arc leftCheek = new Arc(-(SIZE-CHEEK_WIDTH), 0, SIZE, SIZE, 0,180);
        leftCheek.setFill(Color.CRIMSON);
        Arc rightCheek = new Arc(SIZE-CHEEK_WIDTH, 0, SIZE, SIZE, 0, 180);
        rightCheek.setFill(Color.CRIMSON);
        
        Path core = new Path(new MoveTo(-(2*SIZE-CHEEK_WIDTH), 0),
                new QuadCurveTo(-(SIZE+CHEEK_WIDTH), SIZE, 0, SIZE*2), //right
                new QuadCurveTo(SIZE+CHEEK_WIDTH, SIZE, 2*SIZE-CHEEK_WIDTH, 0), //left
                new ClosePath());
        core.setFill(Color.CRIMSON);
        core.setStroke(Color.CRIMSON);
        getChildren().addAll(leftCheek, rightCheek, core);
    }
    
    public static double getWidth(){
        return 2*SIZE-CHEEK_WIDTH;
    }
    
    public static double getHeght(){
        return 2*SIZE;
    }
    
    @Override
    public void update() {} 
}
