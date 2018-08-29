package sprites;

import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Life extends Sprite{
    
    private static final double SIZE = 6;
    private static final double CHEEK_WIDTH = 2;
    
    private Path core;
    private Arc leftCheek;
    private Arc rightCheek;
    
    
    public Life(){ 
        leftCheek = new Arc(-(SIZE-CHEEK_WIDTH), 0, SIZE, SIZE, 0,180);
        leftCheek.setFill(Color.RED);
        rightCheek = new Arc(SIZE-CHEEK_WIDTH, 0, SIZE, SIZE, 0, 180);
        rightCheek.setFill(Color.RED);
        
        core = new Path(new MoveTo(-(2*SIZE-CHEEK_WIDTH), 0),
                new QuadCurveTo(-(SIZE+CHEEK_WIDTH), SIZE, 0, SIZE*2), //right
                new QuadCurveTo(SIZE+CHEEK_WIDTH, SIZE, 2*SIZE-CHEEK_WIDTH, 0), //left
                new ClosePath());
        core.setFill(Color.RED);
        core.setStroke(Color.RED);
        getChildren().addAll(leftCheek, rightCheek, core);
    }
    
    public static double getWidth(){
        return 2*SIZE-CHEEK_WIDTH;
    }
    
    public static double getHeght(){
        return 2*SIZE;
    }
    

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
