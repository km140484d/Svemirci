package sprites;

import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import sprites.awards.*;
import sprites.awards.Bonus.*;

public class BonusIndicator extends Sprite{
    
    private static int BONUS_TIME = 2*60;
    public static double RADIUS = 20;
    
    private BonusType type;
    private double time = BONUS_TIME;
    private String path;
    
    private Circle shape;
    private Rectangle bar_out;
    private Rectangle bar_in;

    public BonusIndicator(BonusType type, String path) {
        this.type = type;
        this.path = path;
        shape = new Circle(RADIUS);
        shape.setFill(new ImagePattern(new Image(path)));
        getChildren().addAll(shape);
        
        if (type instanceof Bonus.YellowBonus){
            System.out.println("ROTACIJA");
            bar_out = new Rectangle(RADIUS/3, RADIUS*3/2);
            bar_out.setFill(Color.BLACK);
            bar_in = new Rectangle(RADIUS/3, RADIUS*3/2);
            bar_in.setFill(Color.CHARTREUSE);
            Group bar = new Group(bar_out, bar_in);
            bar.setTranslateX(-RADIUS*7/6 - 2);
            bar.setTranslateY(-RADIUS);
            getChildren().add(bar);
        }
    }

    public boolean decTime(){
        time--;
        bar_in.setHeight(bar_in.getHeight() - 3/2*RADIUS*(time/(BONUS_TIME)));
        return time == 0;
    }    
    
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

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
