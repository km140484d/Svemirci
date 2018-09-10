package sprites;

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;

public class Background extends Sprite {
    
    private Star star1 = new Star();
    private Star star2 = new Star();
    private Star star3 = new Star();
    
    private final double width = Main.WINDOW_WIDTH;
    private final double height = Main.WINDOW_HEIGHT;
        
    public Background() {
        Rectangle background = new Rectangle(0, 0, width, height);
        Stop [] stops = {
            new Stop(0, Color.BLACK),
            new Stop(1, Color.DARKBLUE)
        };
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        background.setFill(lg);
        getChildren().add(background);
        
        star1.setTranslateX(Math.random() * 7/2 * Star.BOUND);
        star1.setTranslateY(Math.random() * 7/2 * Star.BOUND);
        star2.setTranslateX(width - Math.random() * 7/2 * Star.BOUND);
        star2.setTranslateY(height - Math.random() * 7/2 * Star.BOUND);
        star3.setTranslateX(width/2 + Math.random() * 5/2 * Star.BOUND);
        star3.setTranslateY(height/2 - Math.random() * 5/2 * Star.BOUND);
        getChildren().addAll(star1, star2, star3);

    }
    
    @Override
    public void resizeWindow(double ratioWidth, double ratioHeight){
        super.resizeWindow(ratioWidth, ratioHeight);
        star1.resizeWindow(ratioWidth, ratioHeight);
        star2.resizeWindow(ratioWidth, ratioHeight);
        star3.resizeWindow(ratioWidth, ratioHeight);
    };
    

    @Override
    public void update() {
        star1.update();
        star2.update();
        star3.update();
    }

}
