package sprites.enemies;

import java.util.*;
import javafx.animation.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
import main.Main;

public class Commander extends Enemy{

    private static final double HELMET_LINE = EYE_WIDTH*3/2;    
    private List<Warrior> warriors = new ArrayList<>();
    
    private static final ImagePattern armor_texture, feather_texture;    
    static{
        armor_texture = new ImagePattern(new Image("/resources/enemy/golden_armor.png"));
        feather_texture = new ImagePattern(new Image("/resources/enemy/red_feathers.png"));
    }
    
    public Commander(double posX, double posY, double deltaY){
        super(posX, posY, deltaY);
        Path helmet = new Path(
            new MoveTo(-EN_WIDTH/2, EN_HEIGHT/2),
            new LineTo(-HELMET_LINE*3/2, EN_HEIGHT),
            new VLineTo(0),
            new LineTo(-HELMET_LINE*5/2, -HELMET_LINE),
            new LineTo(-HELMET_LINE*3/2, -HELMET_LINE*2),
            new LineTo(-HELMET_LINE/3, 0),
            new HLineTo(HELMET_LINE/3),
            new LineTo(HELMET_LINE*3/2, -HELMET_LINE*2),
            new LineTo(HELMET_LINE*5/2, -HELMET_LINE),
            new LineTo(HELMET_LINE*3/2 , 0),
            new VLineTo(EN_HEIGHT),
            new LineTo(EN_WIDTH/2, EN_HEIGHT/2),
            new ArcTo(EN_WIDTH*2/3, EN_HEIGHT*5/6, 270, -EN_WIDTH/2, EN_HEIGHT/2, true, false)
        );
        helmet.setFill(armor_texture);
        
        Path holder = new Path(
            new MoveTo(-HELMET_LINE/2, -EN_HEIGHT*4/5),
            new LineTo(-HELMET_LINE, -EN_HEIGHT*5/6 - HELMET_LINE),
            new HLineTo(HELMET_LINE),
            new LineTo(HELMET_LINE/2, -EN_HEIGHT*4/5),
            new ClosePath()                
        );
        holder.setFill(armor_texture); //dark grey
        
        Path crown = new Path(
            new MoveTo(-EN_WIDTH/2, -EN_HEIGHT*5/6),
            new LineTo(-EN_WIDTH*3/4, -EN_HEIGHT),
            new ArcTo(EN_WIDTH*10/11, EN_HEIGHT*1.2, 120, EN_WIDTH*3/4, -EN_HEIGHT, false, true),
            new LineTo(EN_WIDTH/2, -EN_HEIGHT*5/6),
            new ArcTo(EN_WIDTH*5/12, EN_HEIGHT/8, 180, -EN_WIDTH/2, -EN_HEIGHT*5/6, false, false)
        );
        crown.setFill(feather_texture);
        
        for(Path e: ears)
            e.setFill(feather_texture);
        getChildren().addAll(helmet, holder, crown);
        
        strength = Main.constants.getCommander_life() * Main.constants.getDifficulty();
    }
    
    public void addWarrior(Warrior warrior){
        warriors.add(warrior);
    }
    
    public void removeWarrior(Warrior warrior){
        warriors.remove(warrior);
    }
    
    public void notifyWarriors(){
        for(int i = 0; i < warriors.size(); i++){
            warriors.get(i).commanderDown(this);
        }
    }
    
    public void orderAttack(double playerX, double playerY){
        FillTransition ft = new FillTransition(Duration.seconds(1.5), body);
        ft.setFromValue(Color.YELLOW);
        ft.setToValue(Color.CRIMSON);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        ft.play();
        ft.setOnFinished(a -> warriors.forEach(w -> w.attack(playerX, playerY)));
    }
    
    public List<Warrior> getWarriors(){
        return warriors;
    }
    
    @Override
    public int enemyStrength() {
        return Main.constants.getCommander_life() * Main.constants.getDifficulty();
    }

    @Override
    public void update() {
        super.update();
    }

}
