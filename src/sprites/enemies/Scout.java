package sprites.enemies;

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Scout extends Enemy{
    
    public Scout(double fromX, double fromY, double toX, double toY){
        super(fromX, fromY, toX, toY);
        strength = Main.constants.getScout_life() * Main.constants.getDifficulty();
        for(Path e: ears)
            e.setStroke(Color.BLACK);
    }

    @Override
    public int enemyStrength() {
        return Main.constants.getScout_life() * Main.constants.getDifficulty();
    }
  
    
    @Override
    public void update() {
        super.update();
    }
    
}
