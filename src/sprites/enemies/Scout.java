package sprites.enemies;

import javafx.scene.paint.*;
import javafx.scene.shape.*;


public class Scout extends Enemy{
    
    public Scout(double fromX, double fromY, double toX, double toY){
        super(fromX, fromY, toX, toY);
        strength = SCOUT;
        for(Path e: ears)
            e.setStroke(Color.BLACK);
    }

    @Override
    public int enemyStrength() {
        return Enemy.SCOUT;
    }
  
    
    @Override
    public void update() {
        super.update();
    }
    
}
