package sprites;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
import main.Main;

public class Background extends Sprite {
    
    private Star star1 = new Star();
    private Star star2 = new Star();
    private Star star3 = new Star();
    
    private final double width = Main.WINDOW_WIDTH;
    private final double height = Main.WINDOW_HEIGHT;
    
    private static final double LENGTH = Main.constants.getWidth()/100;
    
    public class Comet extends Group{       

        private Circle circle;
        
        public Comet(double x, double y){
            double radius = Math.random() * LENGTH;
            circle = new Circle(radius);
            Color color;
            switch((int)Math.random()*6){
                case 0:
                    color = Color.ANTIQUEWHITE;
                    break;
                case 1:
                    color = Color.AZURE;
                    break;
                case 2:
                    color = Color.CORNSILK;
                    break;
                case 3:
                    color = Color.FLORALWHITE;
                    break;
                case 4:
                    color = Color.GHOSTWHITE;
                default:
                    color = Color.WHITE;
                    break;
            }
            Stop [] stops = {
                new Stop(0, color),
                new Stop(1, Color.TRANSPARENT)
            };
            RadialGradient rg = new RadialGradient(0, 0, 0, 0, radius, false, CycleMethod.NO_CYCLE, stops);
            circle.setFill(rg);
            getChildren().add(circle);
            ScaleTransition st = new ScaleTransition(Duration.seconds(Math.random()), circle);
            st.setFromX(1); st.setFromY(1);
            st.setToX(1.3); st.setToY(1.3);
            st.setAutoReverse(true);
            st.setCycleCount(Animation.INDEFINITE);
            st.setDelay(Duration.seconds(Math.random()));
            st.play();
            setTranslateX(x);
            setTranslateY(y);
        }
    }
        
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
        
        
        for(int i=0; i < 150; i++){
            getChildren().add(new Comet(Math.random()*width, Math.random()*height));
        }

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
