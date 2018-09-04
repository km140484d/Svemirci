package sprites;

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Background extends Sprite {
   
    public static final int LIVES_CNT = 3;
    
    private Star star1 = new Star();
    private Star star2 = new Star();
    private Star star3 = new Star();
        
    public Background(double width, double height) {
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
        getChildren().addAll(star1, star2, star3);

    }

    @Override
    public void update() {
        star1.update();
        star2.update();
        star3.update();
    }

}
