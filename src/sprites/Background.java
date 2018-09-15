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
    
    
    public class Star extends Sprite{

        public double BOUND = 30;
        private Polygon body;
        private double velocityX = 1, velocityY  = 1;  
        private double width = BOUND, height = BOUND;
    
        public Star(){        
            //kao 2 pentagona, unutrasnji manji pentagon je za 180 stepeni rotiran
            body = new Polygon(xCoord(2, 0), yCoord(2, 0),
                                -xCoord(1, 3), -yCoord(1, 3),
                                xCoord(2, 1), yCoord(2, 1),
                                -xCoord(1, 4), -yCoord(1, 4),
                                xCoord(2, 2), yCoord(2, 2),
                                -xCoord(1, 0), -yCoord(1, 0),
                                xCoord(2, 3), yCoord(2, 3),
                                -xCoord(1, 1), -yCoord(1, 1),
                                xCoord(2, 4), yCoord(2, 4),
                                -xCoord(1, 2), -yCoord(1, 2));
            body.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            body.setStroke(Color.WHITE);
            body.setStrokeWidth(1.5);
            getChildren().addAll(body);
        }
        
        public double getBound(){
            return BOUND;
        }

        @Override
        public void resizeWindow(double ratioWidth, double ratioHeight){
            super.resizeWindow(ratioWidth, ratioHeight);
            width*=ratioWidth;
            height*=ratioHeight;
            velocityX *= ratioWidth;
            velocityY *= ratioHeight;
        }

        @Override
        public void update() {
            if (getTranslateX() + velocityX < width / 2 + 5) {
                setTranslateX(width / 2 + 5);
                velocityX = - velocityX;             
            } else if (getTranslateX() + velocityX > Main.WINDOW_WIDTH - width / 2 - 5) {
                setTranslateX(Main.WINDOW_WIDTH - width / 2 - 5);
                velocityX = - velocityX;
            } else {
                setTranslateX(getTranslateX() + velocityX);
            }

            if (getTranslateY() + velocityY < height / 2 + 5) {
                setTranslateY(height / 2 + 5); 
                velocityY = - velocityY;       
            } else if (getTranslateY() + velocityY > Main.WINDOW_HEIGHT - height/ 2 - 5) {
                setTranslateY(Main.WINDOW_HEIGHT - height / 2 - 5);
                velocityY = - velocityY; 
            } else {
                setTranslateY(getTranslateY() + velocityY);
            }

        }

        public double xCoord(int fact, int k){
            return fact*8.1*Math.cos(k*2*Math.PI/5);
        }

        public double yCoord(int fact, int k){
            return fact*8.1*Math.sin(k*2*Math.PI/5);
        }
    }
    
    
    public class Comet extends Group{       

        private Circle circle;
        
        public Comet(double x, double y){
            double a = Math.random(), b = Math.random();
            circle = new Circle(a * LENGTH);
            Color color;
            switch((int)(Math.random()*6)){
                case 0:
                    color = Color.ANTIQUEWHITE; break;
                case 1:
                    color = Color.AZURE; break;
                case 2:
                    color = Color.CORNSILK; break;
                case 3:
                    color = Color.FLORALWHITE; break;
                case 4:
                    color = Color.GHOSTWHITE; break;
                default:
                    color = Color.WHITE; break;
            }
            Stop [] stops = {new Stop(0, color), new Stop(1, Color.TRANSPARENT)};
            RadialGradient rg = new RadialGradient(0, 0, 0, 0, a * LENGTH, false, CycleMethod.NO_CYCLE, stops);
            circle.setFill(rg);
            getChildren().add(circle);
            if ((a>0.75) && (b>0.6)){
                ScaleTransition st = new ScaleTransition(Duration.seconds(Math.random()), circle);
                st.setFromX(1); st.setFromY(1);
                st.setToX(1.3); st.setToY(1.3);
                st.setAutoReverse(true);
                st.setCycleCount(Animation.INDEFINITE);
                st.setDelay(Duration.seconds(Math.random()));
                st.play();
            }
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
        
        double bound = star1.getBound();
        star1.setTranslateX(Math.random() * 7/2 * bound);
        star1.setTranslateY(Math.random() * 7/2 * bound);
        star2.setTranslateX(width - Math.random() * 7/2 * bound);
        star2.setTranslateY(height - Math.random() * 7/2 * bound);
        star3.setTranslateX(width/2 + Math.random() * 5/2 * bound);
        star3.setTranslateY(height/2 - Math.random() * 5/2 * bound);
        getChildren().addAll(star1, star2, star3);        
        
        for(int i=0; i < 150; i++)
            getChildren().add(new Comet(Math.random()*width, Math.random()*height));
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
