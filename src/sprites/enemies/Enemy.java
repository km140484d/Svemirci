package sprites.enemies;

import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.util.Duration;
import sprites.Sprite;

public abstract class Enemy extends Sprite {
    
    protected static final double EN_WIDTH = 40;
    protected static final double EN_HEIGHT = 30;
    protected static final double EYE_WIDTH = EN_WIDTH/6;
    protected static final double EYE_HEIGHT = EN_WIDTH/10;
    protected static final double PUPIL_RADIUS = EYE_WIDTH/4;
    
    
    private boolean first = true;
    private int velocityX = -1, moves = 0;
    private boolean update = false;
    
    public Enemy() {
        
    }
    
    public abstract Rectangle getBody();
    
    public abstract Group getLeftEye();
    
    public abstract Group getRightEye();

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
    
    @Override
    public void update() {
        if (update){
            if (first)
                moves--;
            else{
                if (velocityX > 0)
                    moves++;
                else
                    moves--;
            }
            if (first){
                if (moves < -120){
                    moves = 0;
                    velocityX = - velocityX;
                    first = false;
                }
            }else{
                if ((moves < -180) || (moves > 180)){
                    moves = 0;
                    velocityX = - velocityX;
                }
            }
            setTranslateX(getTranslateX() + velocityX); 
        }
    }
}
