package sprites.indicators;

import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.*;
import sprites.Sprite;
import sprites.awards.*;
import sprites.awards.Bonus.*;

public class BonusIndicator extends Sprite{

    private static int BONUS_TIME = Main.constants.getPower_time() / Main.constants.getDifficulty() * 60;
    private static double RADIUS = 20;
    private static double BAR_SPEED = 3*RADIUS/((BONUS_TIME*2));
    
    private BonusType type;
    private double time = BONUS_TIME;
    private String path;
    
    private Circle shape;
    private Rectangle bar_out;
    private Rectangle bar_in;

    public BonusIndicator(BonusType type, ImagePattern image) {
        this.type = type;
        this.path = path;
        shape = new Circle(RADIUS);
        shape.setFill(image);
        getChildren().addAll(shape);
        
        if (type instanceof Bonus.YellowBonus){
            bar_out = new Rectangle(RADIUS/3, RADIUS*3/2);
            bar_out.setFill(Color.BLACK);
            bar_in = new Rectangle(RADIUS/3, RADIUS*3/2);
            bar_in.setFill(Color.YELLOW);
            Group bar = new Group(bar_in);
            bar.setTranslateX(-RADIUS*4/3 - 2);
            bar.setTranslateY(-RADIUS*3/4);
            getChildren().add(bar);
        }
    }

    public void resetBonusTime(){
        bar_in.setHeight(RADIUS*3/2);
        time = BONUS_TIME;
    }
    
    public boolean decTime(){
        time--;
        bar_in.setHeight(bar_in.getHeight() - BAR_SPEED);
        return time == 0;
    }    

    //getters/setters
    public BonusType getType() {
        return type;
    }

    public void setType(BonusType type) {
        this.type = type;
    }

    public double getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
    public static double getWidth(){
        return RADIUS*3;
    }

    @Override
    public void update() {}    
}
