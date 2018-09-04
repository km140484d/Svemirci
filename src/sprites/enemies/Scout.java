package sprites.enemies;


public class Scout extends Enemy{
    
    public Scout(double fromX, double fromY, double toX, double toY){
        super(fromX, fromY, toX, toY);
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
