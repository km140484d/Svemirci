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
            life.setTranslateY(20);    
            lives.add(life);
            getChildren().add(life);            
        }
        
        getChildren().addAll(star1, star2, star3);
//        Bonus b1 = new Bonus(Bonus.RedBonus.Boomerang);
//        b1.setTranslateX(30);
//        b1.setTranslateY(height - 50);
//        Bonus b2 = new Bonus(Bonus.RedBonus.Stream);
//        b2.setTranslateX(100);
//        b2.setTranslateY(height - 50);
//        Bonus b3 = new Bonus(Bonus.YellowBonus.KnockOut);
//        b3.setTranslateX(170);
//        b3.setTranslateY(height - 50);
//        Bonus b4 = new Bonus(Bonus.YellowBonus.ProjectileGrowth);
//        b4.setTranslateX(240);
//        b4.setTranslateY(height - 50);
//        Bonus b5 = new Bonus(Bonus.YellowBonus.Rotation);
//        b5.setTranslateX(310);
//        b5.setTranslateY(height - 50);
//        Bonus b6 = new Bonus(Bonus.YellowBonus.Shield);
//        b6.setTranslateX(380);
//        b6.setTranslateY(height - 50);
//        Bonus b7 = new Bonus(Bonus.YellowBonus.Speed);
//        b7.setTranslateX(450);
//        b7.setTranslateY(height - 50);
//        Bonus b8 = new Bonus(Bonus.GreenBonus.Life);
//        b8.setTranslateX(520);
//        b8.setTranslateY(height - 50);
//        Bonus b9 = new Bonus(Bonus.GreenBonus.PointS);
//        b9.setTranslateX(590);
//        b9.setTranslateY(height - 50);
//        Bonus b10 = new Bonus(Bonus.GreenBonus.PointM);
//        b10.setTranslateX(660);
//        b10.setTranslateY(height - 50);
//        Bonus b11 = new Bonus(Bonus.GreenBonus.PointL);
//        b11.setTranslateX(730);
//        b11.setTranslateY(height - 50);
//        Bonus b12 = new Bonus(Bonus.BlackBonus.Munition);
//        b12.setTranslateX(800);
//        b12.setTranslateY(height - 50);
//        Bonus b13 = new Bonus(Bonus.BlackBonus.Triangle);
//        b13.setTranslateX(870);
//        b13.setTranslateY(height - 50);
//        Bonus b14 = new Bonus(Bonus.BlackBonus.Rhombus);
//        b14.setTranslateX(940);
//        b14.setTranslateY(height - 50);
//        Bonus b15 = new Bonus(Bonus.BlackBonus.Pentagon);
//        b15.setTranslateX(1010);
//        b15.setTranslateY(height - 50);
//        Bonus b16 = new Bonus(Bonus.BlackBonus.Hexagon);
//        b16.setTranslateX(1080);
//        b16.setTranslateY(height - 50);
//        getChildren().addAll(b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16);
    }
    
    public boolean loseLife(){
        Life lostLife = lives.get(lives.size() - 1);
        if (lives.size() == 1){
            return true; //player loses
        }else{
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
                lives.remove(lostLife);
            });   
            disappearingLife.play();
            return false; //player has more lives
        }
    }

    @Override
    public void update() {
        star1.update();
        star2.update();
        star3.update();
    }
}
