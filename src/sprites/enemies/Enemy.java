package sprites.enemies;

import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.util.Duration;
import sprites.Sprite;

public abstract class Enemy extends Sprite {
    
    protected static final double EN_WIDTH = 40;
    protected static final double EN_HEIGHT = 30;
    protected static final double EYE_WIDTH = EN_WIDTH/6;
    protected static final double EYE_HEIGHT = EN_WIDTH/10;
    protected static final double PUPIL_RADIUS = EYE_WIDTH/4;
    
    protected static final double HELMET_LINE = EYE_WIDTH*6/5;
    
    private Rectangle body;
    
    //0-left, 1-right
    private Group [] gr_eyes = new Group [2];
    private Ellipse [] eyes = new Ellipse [2]; 
    private Circle [] pupils = new Circle [2];
    
    protected Path [] ears = new Path [2]; 
    
    private Arc mouth; 
    private int strength;
    
    private boolean first = true;
    private int velocityX = -1, moves = 0;
    private boolean update = false;
    
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

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
    
    public void enemyShot(){
        strength--;
    }
    
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
        }
    }
}
