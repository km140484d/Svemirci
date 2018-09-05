package sprites.enemies;

import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;
import sprites.*;
import static sprites.enemies.Enemy.EN_WIDTH;

public class Warrior extends Enemy{
    
    private Path helmet;
    private Commander commander;
    
    public Warrior(double fromX, double fromY, double toX, double toY, Commander commander){
        super(fromX, fromY, toX, toY);        
        if (commander != null){
            this.commander = commander;
            commander.addWarrior(this);
        }
        helmet = new Path(
                new MoveTo(-EN_WIDTH/2, EN_HEIGHT/2),
                new LineTo(-HELMET_LINE*3/2, EN_HEIGHT*2/3),
                new VLineTo(0),
                new LineTo(-HELMET_LINE*5/2, -HELMET_LINE),
                new LineTo(-HELMET_LINE*3/2, -HELMET_LINE*2),
                new LineTo(-HELMET_LINE/3, 0),
                new HLineTo(HELMET_LINE/3),
                new LineTo(HELMET_LINE*3/2, -HELMET_LINE*2),
                new LineTo(HELMET_LINE*5/2, -HELMET_LINE),
                new LineTo(HELMET_LINE*3/2 , 0),
                new VLineTo(EN_HEIGHT*2/3),
                new LineTo(EN_WIDTH/2, EN_HEIGHT/2),
                new ArcTo(EN_WIDTH*2/3, EN_HEIGHT*5/6, 270, -EN_WIDTH/2, EN_HEIGHT/2, true, false)
        );
        helmet.setFill(new ImagePattern(new Image("/resources/enemy/steel_armor.png")));
        getChildren().addAll(helmet);
        for(Path e: ears)
            e.setFill(Color.GRAY);
        
        strength = WARRIOR;
    }
    
    public void notifyCommander(){
        if (commander != null)
            commander.removeWarrior(this);
    }
    
    public void attack(double playerX, double playerY){      
        double x = getTranslateX() + getWidth()/2;
        double y = getTranslateY() + getHeight()/2;
        Projectile proj = new Projectile(x, y);
        proj.setPlayerCoordinates(playerX, playerY);
        Main.addProjectile(proj);
    }
    
    @Override
    public int enemyStrength() {
        return Enemy.WARRIOR;
    }
    
        @Override
    public void update() {
        super.update();
    }


    
}
