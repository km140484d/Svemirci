package sprites.enemies;


public class Scout extends Enemy{
    
    public Scout(){        
        strength = SCOUT;
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
