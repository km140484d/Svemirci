package sprites.enemies;

import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.util.Duration;
import main.Main;
import sprites.*;

public abstract class Enemy extends Sprite {
    
    protected static final double EN_WIDTH = 40;
    protected static final double EN_HEIGHT = 30;
    protected static final double EYE_WIDTH = EN_WIDTH/6;
    protected static final double EYE_HEIGHT = EN_WIDTH/10;
    protected static final double PUPIL_RADIUS = EYE_WIDTH/4;
    
    protected static final double HELMET_LINE = EYE_WIDTH*6/5;
    
    protected static final int SCOUT = 5;
    protected static final int WARRIOR = 15;
    protected static final int COMMANDER = 25;
    
    private Rectangle body;
    
    //0-left, 1-right
    private Group [] gr_eyes = new Group [2];
    private Ellipse [] eyes = new Ellipse [2]; 
    private Circle [] pupils = new Circle [2];
    protected Path [] ears = new Path [2];     
    
    private Arc mouth; 
    
    private Group stat;
    //0 - black, 1 - green
    private Rectangle [] bars = new Rectangle [2];
    
    protected int strength;
    
    private boolean first = true;
    private int velocityX = -1, moves = 0;
    private static boolean update = false;
    
    private boolean redMark;
    
    public Enemy() {
        //ears
        for(int i = 0; i < ears.length; i++){
            ears[i] = new Path(
                        new MoveTo(i==0 ? -EN_WIDTH/2+5 : EN_WIDTH/2-5, 0), 
                        new LineTo(i==0 ? -EN_WIDTH*7/6+5 : EN_WIDTH*7/6-5, -EN_HEIGHT/2-5),
                        new VLineTo(EN_HEIGHT/2+5),
                        new ClosePath()
            );
            double angle = 15;
            if (i == 1){
                angle = -angle;
            }
            ears[i].setFill(Color.ALICEBLUE);
            
            //flying animation
            Rotate fly_rot = new Rotate();
            fly_rot.setPivotY(0);
            fly_rot.setPivotX(i==0 ? -EN_WIDTH/2+5 : EN_WIDTH/2-5);
            ears[i].getTransforms().add(fly_rot);
            Timeline tl = new Timeline(
                    new KeyFrame( Duration.seconds(0),
                            new KeyValue(fly_rot.angleProperty(), angle, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame( Duration.seconds(2),
                            new KeyValue(fly_rot.angleProperty(), -angle, Interpolator.EASE_BOTH)
                    )
            );
            tl.setAutoReverse(true);
            tl.setCycleCount(Animation.INDEFINITE);
            tl.play();
            getChildren().add(ears[i]);
        }
        
        //body
        body = new Rectangle(0, 0, EN_WIDTH, EN_HEIGHT);
        body.setArcWidth(EN_WIDTH/2);
        body.setArcHeight(EN_HEIGHT/2);
        body.setFill(Color.YELLOW);
        body.setTranslateX(-EN_WIDTH/2);
        body.setTranslateY(-EN_HEIGHT/2);
        getChildren().add(body);

        //eyes
        for(int i = 0; i < eyes.length; i++){ 
            eyes[i] = new Ellipse(EYE_WIDTH, EYE_HEIGHT);
            eyes[i].setFill(Color.WHITE);
            eyes[i].setStroke(Color.BLACK);
            eyes[i].setStrokeWidth(0.2);
            pupils[i] = new Circle(PUPIL_RADIUS);
            gr_eyes[i] = new Group();
            gr_eyes[i].getChildren().addAll(eyes[i], pupils[i]);
            gr_eyes[i].setTranslateX((i == 0? -1:1) * 1.3 *EYE_WIDTH);
            gr_eyes[i].setTranslateY(-EN_HEIGHT/2 + EYE_HEIGHT*3);
            getChildren().add(gr_eyes[i]);           
        }
        
        //mouth
        mouth = new Arc(0, EN_HEIGHT/7, EN_WIDTH/3, EN_HEIGHT/4, 180, 180); 
        getChildren().addAll(mouth);
        
        //bar
        stat = new Group();
        for(int i = 0; i < bars.length; i++){
            bars[i] = new Rectangle(EN_WIDTH/2, EN_HEIGHT/8);
            bars[i].setFill(i==1 ? Color.CHARTREUSE : Color.BLACK);
        }
        stat.getChildren().addAll(bars);
        stat.setTranslateX(-EN_WIDTH/4);
        stat.setTranslateY(-EN_HEIGHT/4 - EN_HEIGHT*2/3);
    }
    
    public void showBar(Group root){
        root.getChildren().add(stat);
    }
    
    public Rectangle getBody(){
        return body;
    }
    
    public Group getLeftEye(){
        return gr_eyes[0];
    }
    
    public Group getRightEye(){
        return gr_eyes[1];
    }

    public static boolean isUpdate() {
        return update;
    }

    public static void setUpdate(boolean state) {
        update = state;
    }
    
    public void enemyShot(){
        strength--;
        if (strength <= 0)
            Main.removeEnemy(this);
        else{
            ScaleTransition hit = new ScaleTransition(Duration.seconds(0.1), this);
            hit.setFromX(1); hit.setByX(0.1);
            hit.setFromY(1); hit.setByY(0.1);
            hit.setAutoReverse(true);
            hit.setCycleCount(2);
            hit.play();
            bars[1].setWidth(bars[1].getWidth() - EN_WIDTH/(2*enemyStrength()));
        }
    }
    
    public boolean isRedMark(){
        return redMark;
    }  
    
    public void setRedMark(boolean value){
        redMark = value;
    }
    
    public abstract int enemyStrength();
    
    @Override
    public void update() {
        if (update){
            if (first)
                moves--;
            else{
                if (velocityX > 0)
                    moves++;
                else
                    moves--;
            }
            if (first){
                if (moves < -120){
                    moves = 0;
                    velocityX = - velocityX;
                    first = false;
                }
            }else{
                if ((moves < -180) || (moves > 180)){
                    moves = 0;
                    velocityX = - velocityX;
                }
            }
            setTranslateX(getTranslateX() + velocityX); 
            stat.setTranslateX(getTranslateX() - EN_WIDTH/4);
            stat.setTranslateY(getTranslateY() - EN_HEIGHT/4 - EN_HEIGHT*2/3);
        }
    }
}
