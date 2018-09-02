package sprites.shots;

import javafx.animation.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
import main.Main;

public class Boomerang extends Shot{
    
    private static final double BOOM = size*3/2;
    private static final double INNER_R = BOOM*3/8;
    private static final double OUTER_R = BOOM*3/4;
    
    private static final double RADIUS = BOOM*5/4;
 
    public Boomerang(double playerAngle){
        super(playerAngle);
        
        base = new Path(
                new MoveTo(INNER_R, 0),
                new LineTo(INNER_R + BOOM/2, -BOOM),
                new ArcTo(OUTER_R/2, OUTER_R/2, 180, INNER_R + OUTER_R + BOOM/2, -BOOM, false, true),
                new LineTo(INNER_R + OUTER_R, 0),
                new ArcTo(OUTER_R, OUTER_R, 180, -INNER_R - OUTER_R, 0, false, true),
                new LineTo(-(INNER_R + OUTER_R + BOOM/2), -BOOM),
                new ArcTo(OUTER_R/2, OUTER_R/2, 180, -(INNER_R + BOOM/2), -BOOM, false, true),
                new LineTo(-INNER_R, 0),
                new ArcTo(INNER_R, INNER_R, 180, INNER_R, 0, false, false)
        );    

        RotateTransition rt = new RotateTransition(Duration.seconds(1), this);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();
        
        base.setFill(new ImagePattern(new Image("/resources/shots/boom1.png")));
        getChildren().addAll(base);
        this.setTranslateY(BOOM/8);
    }
    
    @Override
    public void update() {
        double x = getTranslateX();
        double y = getTranslateY();
        
        if (x + velocityX < RADIUS - 5){
            setTranslateX(RADIUS - 5);
            velocityX = -velocityX;
            Main.setEnemyRedMark(true);
        }else{
            if (x + velocityX > Main.WINDOW_WIDTH - RADIUS - 5){
                setTranslateX(Main.WINDOW_WIDTH - RADIUS - 5);
                velocityX = -velocityX;
                Main.setEnemyRedMark(true);
            }else
                setTranslateX(x + velocityX);
        }
        
        if (y + velocityY > Main.WINDOW_HEIGHT - RADIUS - 5)
            Main.removeSprite(this);        
        else{
            if (y + velocityY < RADIUS - 5){
                setTranslateY(RADIUS - 5);
                velocityY = -velocityY;
                Main.setEnemyRedMark(true);
            }else{
                setTranslateY(y + velocityY);
            }
        }
       
    }
    
}
