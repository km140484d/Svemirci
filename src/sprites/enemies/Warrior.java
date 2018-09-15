package sprites.enemies;

import java.util.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;
import sprites.*;
import static sprites.enemies.Enemy.EN_WIDTH;

public class Warrior extends Enemy{
    
    private List<Commander> commanders = new ArrayList<>();
    
    public Warrior(double posX, double posY, double deltaY){
        super(posX, posY, deltaY);        
        Path helmet = new Path(
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
        
        strength = Main.constants.getWarrior_life() * Main.constants.getDifficulty();
    }
    
    public void addCommander(Commander commander){
        this.commanders.add(commander);
        commander.addWarrior(this);
    }
    
    public void notifyCommanders(){
        for(int i = 0; i < commanders.size(); i++)
            commanders.get(i).removeWarrior(this);
    }
    
    public void commanderDown(Commander commander){
        commanders.remove(commander);
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
        return Main.constants.getWarrior_life() * Main.constants.getDifficulty();
    }
    
        @Override
    public void update() {
        super.update();
    }

}
