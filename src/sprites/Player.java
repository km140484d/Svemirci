package sprites;

import java.util.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.util.*;
import main.Main;
import sprites.awards.Bonus.*;
import sprites.shots.*;
import sprites.shots.Shot.*;
import static sprites.shots.Shot.BasicShotType.*;

public class Player extends Sprite implements EventHandler<KeyEvent> {
    
    private static enum States {LEFT, RIGHT, UP, DOWN, STALL};
    private static final double PLAYER_VELOCITY = 10;
    
    private List<Shot> shots = new LinkedList<>();
    
    private double velocityX = 0, velocityY = 0;
    private States state = States.STALL;
    
    private static final double WIDTH = 60;
    private static final double HEIGHT = 60;

    private static final double GUN_OUT_RX = WIDTH/4;
    private static final double GUN_OUT_RY = HEIGHT/2;
    
    private static final double BODY_OUT_RX = WIDTH/2;
    private static final double BODY_OUT_RY = HEIGHT/4; //3/5
    
    private static final double TUBE_HEIGHT = HEIGHT/6;
    private static final double TUBE_WIDTH = WIDTH/10;
    
    private static final double STREAM_HEIGHT = TUBE_HEIGHT*4/3;
    
    private static final double SHIELD_R = WIDTH*3/4;
    
    private static final double ROTATE_ANGLE = 3;
    
    private static final int MAX_SHOTS = 5;

    private Shape body;
    private Group gun;    
    private Group leftTubeGroup;
    private Group rightTubeGroup;  
    private Circle shield;
    private Timeline playerProtection;

    private RedBonus redBonus; 
    private boolean rotate = false;
    private boolean speed = false;
    
    private BasicShotType shotType = BasicShotType.Tri;
    private int shotCnt = 1;
    
    public Player() { 
        Stop [] stops = {
            new Stop(0, Color.YELLOW),
            new Stop(1, Color.TRANSPARENT)
        };
        RadialGradient rg = new RadialGradient(0, 0, 0, 0, SHIELD_R, false, CycleMethod.NO_CYCLE, stops);        
        shield = new Circle(SHIELD_R);
        shield.setFill(rg);
        
        playerProtection = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(shield.opacityProperty(), 1, Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(0.75), new KeyValue(shield.opacityProperty(), 0, Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(1), new KeyValue(shield.opacityProperty(), 0))
        );
        playerProtection.setCycleCount(5);
        playerProtection.setOnFinished(t -> {getChildren().remove(shield);});

        Ellipse e1 = new Ellipse(0, BODY_OUT_RY, BODY_OUT_RX, BODY_OUT_RY);
        Ellipse e2 = new Ellipse(0, BODY_OUT_RY*7/4, BODY_OUT_RX, BODY_OUT_RY);
        body = Shape.subtract(e1, e2);
        body.setFill(Color.SKYBLUE);
        body.setStroke(Color.BLACK);
        body.setStrokeWidth(1.2);

        leftTubeGroup = makeTube("L");
        rightTubeGroup = makeTube("R");

        Arc gun_out = new Arc(0, 0, GUN_OUT_RX, GUN_OUT_RY, -10, 200);
        gun_out.setFill(Color.SKYBLUE);
        gun_out.setStroke(Color.BLACK);
        gun_out.setStrokeWidth(1.2);
        Arc gun_in = new Arc(0, 0, GUN_OUT_RX/3, GUN_OUT_RY/2, -10, 200);
        gun_in.setFill(Color.BLACK);
        gun = new Group();
        gun.getChildren().addAll(gun_out, gun_in);
        
        getChildren().addAll(gun, leftTubeGroup, rightTubeGroup, body);
    }
    
    public static Group makeTube(String type){
         Path tube = new Path(
                new MoveTo(-TUBE_WIDTH/2, 0),
                new QuadCurveTo(-TUBE_WIDTH*4/3, TUBE_HEIGHT/2, -TUBE_WIDTH/2, TUBE_HEIGHT),
                new HLineTo(TUBE_WIDTH/2),
                new QuadCurveTo(TUBE_WIDTH*4/3, TUBE_HEIGHT/2, TUBE_WIDTH/2, 0),
                new ClosePath()
        );
        tube.setFill(Color.BLACK);
        Ellipse ring = new Ellipse(0, TUBE_HEIGHT, TUBE_WIDTH/2, TUBE_HEIGHT/10);
        ring.setFill(Color.WHITE);         
        Path stream = new Path(
                new MoveTo(-TUBE_WIDTH/2, 0),
                new QuadCurveTo(-TUBE_WIDTH*3/2, STREAM_HEIGHT/2, 0, STREAM_HEIGHT),
                new QuadCurveTo(TUBE_WIDTH*3/2, STREAM_HEIGHT/2, TUBE_WIDTH/2, 0)
        );
        stream.setFill(new ImagePattern(new Image("/resources/player/fire.gif")));
        stream.setTranslateY(TUBE_HEIGHT);
        
        Scale scale = new Scale();
        stream.getTransforms().add(scale);
        
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(scale.yProperty(), 1, Interpolator.EASE_IN)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(scale.yProperty(), 2, Interpolator.EASE_OUT))
        );
        time.setAutoReverse(true);
        time.setCycleCount(Animation.INDEFINITE);
        time.play();

        Group tubeGroup = new Group(tube, ring, stream);
        tubeGroup.setTranslateY(BODY_OUT_RY*4/5);
        
        if (type.equals("L"))
            tubeGroup.setTranslateX(-TUBE_WIDTH*3/2);
        else
            tubeGroup.setTranslateX(TUBE_WIDTH*3/2);
        return tubeGroup;
    }
    
    public void reset(){
        getChildren().clear();
        getChildren().addAll(shield, gun, leftTubeGroup, rightTubeGroup, body);        
        playerProtection.play();
    }
   
    private void makeShot() {
        Shot shot = null;
        if (redBonus != null){            
            switch(redBonus){
                case Stream:
                    shot = new Stream(getRotate());
                    break;
                case Boomerang:
                    shot = new Boomerang(getRotate());
                    break;
            } 
            redBonus = null;
            Main.setCollectedRed(null);
            shot.setTranslateX(getTranslateX() + GUN_OUT_RY*Math.tan(Math.toRadians(getRotate())));
            shot.setTranslateY(getTranslateY() - GUN_OUT_RY);
            shots.add(shot);
        }else{
            int half = shotCnt/2;
            double delta = shotType.getAngle()/(2*half);
            double angle = delta;
            for(int i=0; i<shotCnt/2 + shotCnt%2; i++){
                if (half == 0)
                    shoot(0, 90 - getRotate());
                else{
                    shoot(angle, -getRotate() + 90 - angle);
                    shoot(-angle, -getRotate() + 90 + angle);
                    angle += delta;
                    half--;
                }             
            }                      
        }
    }
    
    public void shoot(double angle, double trans){
        Shot shot = null;
        switch(shotType){
            case Tri:
                shot = new Triangle(getRotate(), angle);
                break;
            case Rho:
                shot = new Rhombus(getRotate(), angle);
                break;
            case Pen:
                shot = new Pentagon(getRotate(), angle);
                break;
            case Hex:
                shot = new Hexagon(getRotate(), angle);
        }
        shot.setTranslateX(getTranslateX() + GUN_OUT_RY*Math.cos(Math.toRadians(trans)));
        shot.setTranslateY(getTranslateY() - GUN_OUT_RY*Math.sin(Math.toRadians(trans)));
        shot.setRotate(shot.getRotate() + angle);
        shots.add(shot);
    }
    
    public void setRedBonusType(RedBonus type){
        redBonus = type;
    }
    
    public void setRotate(boolean rotate){
        this.rotate = rotate;
    }
    
    public void setSpeed(boolean speed){
        this.speed = speed;
    }
 
    public boolean invincible(){
        if (getChildren().contains(shield))
            return true;
        return false;
    }
    
    public void setShield(boolean status){
        if (status){
            shield.setOpacity(1);
            if (playerProtection.getStatus() == Animation.Status.RUNNING)
                playerProtection.stop();           
            else{
                getChildren().clear();
                getChildren().addAll(shield, gun, leftTubeGroup, rightTubeGroup, body);
            }
        }else
            getChildren().remove(shield);        
    }
    
    public void setShotType(BasicShotType type, boolean reset){
        if (reset)
            shotCnt = 1;
        else{
            if (shotType.equals(type) && shotCnt < MAX_SHOTS)
                shotCnt++;
            else
                shotType = type;
        }
    }
    
    public BasicShotType getShotType(){
        return shotType;
    }

    public List<Shot> getShots() {
        return shots;
    }
    
    public void setShots(List<Shot> s) {
        shots = s;
    }
    
    private void setVelocity() {
        double velocity = PLAYER_VELOCITY;
        if (speed)
            velocity = 2*PLAYER_VELOCITY;
        switch (state) {
            case STALL:
                velocityX = 0;
                velocityY = 0;
                break;
            case RIGHT:
                velocityX = velocity;
                break;
            case LEFT:
                velocityX = - velocity;
                break;
            case UP:
                velocityY = - velocity;
                break;
            case DOWN:
                velocityY = velocity;
                break;
            default:
                break;
        }
    }
    
    @Override
    public void update() {
        if (getTranslateX() + velocityX < WIDTH / 2 + 5) {
            setTranslateX(WIDTH / 2 + 5);
        } else if (getTranslateX() + velocityX > Main.WINDOW_WIDTH - WIDTH / 2 - 5) {
            setTranslateX(Main.WINDOW_WIDTH - WIDTH / 2 - 5);
        } else {
            setTranslateX(getTranslateX() + velocityX);
        }
        
        if (getTranslateY() + velocityY < HEIGHT / 2 + 5) {
            setTranslateY(HEIGHT / 2 + 5);
        } else if (getTranslateY() + velocityY > Main.WINDOW_HEIGHT - HEIGHT / 2 - 5) {
            setTranslateY(Main.WINDOW_HEIGHT - HEIGHT / 2 - 5);
        } else {
            setTranslateY(getTranslateY() + velocityY);
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED){
            KeyCode code = event.getCode();
            switch (code) {                    
                    case UP:
                        state = States.UP;
                        setVelocity();
                        break;
                    case DOWN:
                        state = States.DOWN;
                        setVelocity();
                        break;
                    case RIGHT:
                        state = States.RIGHT;
                        setVelocity();
                        break;
                    case LEFT:
                        state = States.LEFT;
                        setVelocity();
                        break;
                    case A:
                        if (rotate)
                            setRotate(getRotate() - ROTATE_ANGLE);                            
                        break;
                    case S:
                        if (rotate)
                            setRotate(getRotate() + ROTATE_ANGLE);
                        break;
                    case DIGIT1:
                        Main.camera.setDefault();
                        break;
                    case DIGIT2:
                        Main.camera.setPlayerBound(this);
                        break;
                }
        }else{                
            if (event.getEventType() == KeyEvent.KEY_RELEASED){
                KeyCode code = event.getCode();
                switch (code) {
                    case UP: case DOWN: case LEFT: case RIGHT:
                        state = States.STALL;
                        setVelocity();
                        break;
                    case SPACE:
                        makeShot();
                        break;
                }
            }
        }

    }
    
}
