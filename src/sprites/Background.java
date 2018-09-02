package sprites;

import java.util.*;
import javafx.animation.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
import main.Main;
import sprites.awards.Bonus;

public class Background extends Sprite {
   
    public static final int LIVES_CNT = 3;
    
    private Star star1 = new Star();
    private Star star2 = new Star();
    private Star star3 = new Star();
    
    private List<Life> lives = new ArrayList<>();
    private List<Life> lostLives = new ArrayList<>();
    
    public Background(int width, int height) {
        Rectangle background = new Rectangle(0, 0, width + 10, height + 10);
        Stop [] stops = {
            new Stop(0, Color.BLACK),
            new Stop(1, Color.DARKBLUE)
        };
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        background.setFill(lg);
        getChildren().add(background);
        star1.setTranslateX(Math.random() * 50 + 50);
        star1.setTranslateY(Math.random() * 50 + 50);
        star2.setTranslateX(Main.WINDOW_WIDTH  - 50 - Math.random() * 50);
        star2.setTranslateY(Main.WINDOW_HEIGHT - 50 - Math.random() * 50);
        star3.setTranslateX(Main.WINDOW_WIDTH/2 + Math.random() * 50);
        star3.setTranslateY(Main.WINDOW_HEIGHT/2 - Math.random() * 50);
        
        for(int i = 0; i < LIVES_CNT; i++){
            Life life = new Life();            
            life.setTranslateX(20 + i*(2*Life.getWidth() + 5));
            life.setTranslateY(Life.getHeght());    
            lives.add(life);
            getChildren().add(life);            
        }
        
        getChildren().addAll(star1, star2, star3);

    }
    
    public boolean loseLife(){
        Life lostLife = lives.get(lives.size() - 1);
        if (lives.size() == 1){
            getChildren().remove(lostLife);
            lives.remove(lostLife);
            return true; //player loses
        }else{
            lostLives.add(lostLife);
            lives.remove(lostLife);
            Timeline disappearingLife = new Timeline(
                    new KeyFrame(Duration.ZERO, 
                        new KeyValue(lostLife.opacityProperty(), 1.0, Interpolator.DISCRETE)),
                    new KeyFrame(Duration.seconds(0.5), 
                            new KeyValue(lostLife.opacityProperty(), 0, Interpolator.DISCRETE)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(lostLife.opacityProperty(), 0))
            );
            disappearingLife.setCycleCount(5);
            disappearingLife.setOnFinished(t ->{
                getChildren().remove(lostLife);
                lostLives.remove(lostLife);
            });   
            disappearingLife.play();
            return false; //player has more lives
        }
    }
    
    public void collectLife(){
        Life life = new Life();            
        life.setTranslateX(20 + lives.size()*(2*Life.getWidth() + 5));
        life.setTranslateY(Life.getHeght());    
        lives.add(life);
        getChildren().add(life); 
    }
    
    public int getLifeNumber(){
        return lives.size();
    }

    @Override
    public void update() {
        star1.update();
        star2.update();
        star3.update();
    }
}
