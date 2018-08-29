package sprites;

import java.util.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.*;
import main.Main;
import sprites.shots.*;

public class Player extends Sprite implements EventHandler<KeyEvent> {
    
    private static enum States {LEFT, RIGHT, UP, DOWN, STALL};
    private static final double PLAYER_VELOCITY = 10;
    
    private List<Shot> shots = new LinkedList<>();
    
    private double velocityX = 0, velocityY = 0;
    private States state = States.STALL;
    
    private static final double SHIELD_R = 50;
    
    private static final double GUN_OUT_RX = 15;
    private static final double GUN_OUT_RY = 30;
    
    private Shape body;
    private Group gun;
    
    private Rectangle playerShape;
    
    private Group leftTubeGroup;
    private Group rightTubeGroup;
    
    private Circle shield;
    
    
    public Player() {  
        
        shield = new Circle(SHIELD_R);
        shield.setFill(Color.color(0.4, 0.6, 0));
        shield.setTranslateY(10);
        
        playerShape = new Rectangle(0, 0, 60, 20);
        playerShape.setTranslateX(-30);
        playerShape.setFill(Color.TRANSPARENT);
        
        Ellipse ell_out = new Ellipse(30, 20);
        Ellipse ell_in = new Ellipse(30, 20);
        ell_in.setTranslateY(15);
        
        body = Shape.subtract(ell_out, ell_in);
        body.setFill(Color.SKYBLUE);
        body.setTranslateY(25);
        
        Rectangle tubeLeft = new Rectangle(7.5, 15);
        tubeLeft.setFill(Color.BLACK);
        Rectangle tubeRight = new Rectangle(7.5, 15);
        tubeRight.setFill(Color.BLACK);       
        
        Path streamLeft = new Path(new MoveTo(0, 15), 
                new QuadCurveTo(-2, 17.5, -1,18), 
                new QuadCurveTo(-1, 19.5, 2, 21),
                new QuadCurveTo(3, 23, 5.5, 21),
                new QuadCurveTo(9.5, 19.5, 8.5, 18),
                new QuadCurveTo(9.5, 17.5, 7.5, 15),
                new ClosePath());
        streamLeft.setFill(Color.WHITESMOKE);
        Path streamRight = new Path(new MoveTo(0, 15), 
                new QuadCurveTo(-2, 17.5, -1,18), 
                new QuadCurveTo(-1, 19.5, 2, 21),
                new QuadCurveTo(3, 23, 5.5, 21),
                new QuadCurveTo(9.5, 19.5, 8.5, 18),
                new QuadCurveTo(9.5, 17.5, 7.5, 15),
                new ClosePath());
        streamRight.setFill(Color.WHITESMOKE);
        
        leftTubeGroup = new Group(tubeLeft, streamLeft);
        rightTubeGroup = new Group(tubeRight, streamRight);
        
        ScaleTransition stLeft = new ScaleTransition(Duration.seconds(.5), leftTubeGroup);
        stLeft.setFromY(1);
        stLeft.setToY(2);
        stLeft.setAutoReverse(true);
        stLeft.setCycleCount(Animation.INDEFINITE);
        stLeft.play();     
        
        ScaleTransition stRight = new ScaleTransition(Duration.seconds(.5), rightTubeGroup);
        stRight.setFromY(1);
        stRight.setToY(2);
        stRight.setAutoReverse(true);
        stRight.setCycleCount(Animation.INDEFINITE);
        stRight.play();  

        leftTubeGroup.setTranslateX(-10);
        leftTubeGroup.setTranslateY(20);
        
        rightTubeGroup.setTranslateX(5);
        rightTubeGroup.setTranslateY(20);
        
        Arc gun_out = new Arc(0, 0, GUN_OUT_RX, GUN_OUT_RY, -45, 270);
        gun_out.setFill(Color.SKYBLUE);
        Arc gun_in = new Arc(0, 0, GUN_OUT_RX/3, GUN_OUT_RY/2, -45, 270);
        gun_in.setFill(Color.BLACK);
        gun = new Group();
        gun.getChildren().addAll(gun_out, gun_in);
        
        getChildren().addAll(gun, leftTubeGroup, rightTubeGroup, body, playerShape);
    }
    
    public void reset(){
        getChildren().clear();
        getChildren().addAll(shield, gun, leftTubeGroup, rightTubeGroup, body, playerShape);
        Timeline playerProtection = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(shield.opacityProperty(), 1, Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(0.75), new KeyValue(shield.opacityProperty(), 0, Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(1), new KeyValue(shield.opacityProperty(), 0))
        );
        playerProtection.setCycleCount(5);
        playerProtection.setOnFinished(t -> {getChildren().remove(shield);});
        playerProtection.play();
    }
    
    public boolean invincible(){
        if (getChildren().contains(shield))
            return true;
        return false;
    }
        
    private void setVelocity() {
        switch (state) {
            case STALL:
                velocityX = 0;
                velocityY = 0;
                break;
            case RIGHT:
                velocityX = PLAYER_VELOCITY;
                break;
            case LEFT:
                velocityX = - PLAYER_VELOCITY;
                break;
            case UP:
                velocityY = - PLAYER_VELOCITY;
                break;
            case DOWN:
                velocityY = PLAYER_VELOCITY;
                break;
            default:
                break;
        }
    }
    
    public void loseGame(){
        
    }
    
    public List<Shot> getShots() {
        return shots;
    }
    
    public void setShots(List<Shot> s) {
        shots = s;
    }
    
    private void makeShot() {
        Shot shot = new Triangle();
        shot.setTranslateX(getTranslateX());
        shot.setTranslateY(getTranslateY() - GUN_OUT_RY);
        shots.add(shot);
    }
    
    @Override
    public void update() {
        if (getTranslateX() + velocityX < playerShape.getWidth() / 2 + 5) {
            setTranslateX(playerShape.getWidth() / 2 + 5);
        } else if (getTranslateX() + velocityX > Main.WINDOW_WIDTH - playerShape.getWidth() / 2 - 5) {
            setTranslateX(Main.WINDOW_WIDTH - playerShape.getWidth() / 2 - 5);
        } else {
            setTranslateX(getTranslateX() + velocityX);
        }
        
        if (getTranslateY() + velocityY < playerShape.getHeight() / 2 + 5) {
            setTranslateY(playerShape.getHeight() / 2 + 5);
        } else if (getTranslateY() + velocityY > Main.WINDOW_HEIGHT - playerShape.getHeight() / 2 - 5) {
            setTranslateY(Main.WINDOW_HEIGHT - playerShape.getHeight() / 2 - 5);
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
                    case UP: case DOWN: case RIGHT: case LEFT:
                        state = States.STALL;
                        setVelocity();
                        break;
                    case SPACE:
                        makeShot();
                        break;
                }
            }
        }
        
//        if (event.getCode() == KeyCode.RIGHT && event.getEventType() == KeyEvent.KEY_PRESSED) {
//            if (event.getCode() == KeyCode.UP && event.getEventType() == KeyEvent.KEY_PRESSED){
//                state = States.UP;
//                setVelocity();
//            }else{
//                if (event.getCode() == KeyCode.DOWN && event.getEventType() == KeyEvent.KEY_PRESSED){
//                    state = States.DOWN;
//                    setVelocity();
//                }                
//            }
//            state = States.RIGHT;
//            setVelocity();
//        } else if (event.getCode() == KeyCode.LEFT && event.getEventType() == KeyEvent.KEY_PRESSED) {
//            if (event.getCode() == KeyCode.UP && event.getEventType() == KeyEvent.KEY_PRESSED){
//                state = States.UP;
//                setVelocity();
//            }else{
//                if (event.getCode() == KeyCode.DOWN && event.getEventType() == KeyEvent.KEY_PRESSED){
//                    state = States.DOWN;
//                    setVelocity();
//                }                
//            }
//            state = States.LEFT;
//            setVelocity();
//        } else if (event.getCode() == KeyCode.UP && event.getEventType() == KeyEvent.KEY_PRESSED) {
//            state = States.UP;
//            setVelocity();
//        } else if (event.getCode() == KeyCode.DOWN && event.getEventType() == KeyEvent.KEY_PRESSED) {
//            state = States.DOWN;
//            setVelocity();
//        } else if ((event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) 
//                && event.getEventType() == KeyEvent.KEY_RELEASED) {
//            state = States.STALL;
//            setVelocity();
//        } else if ((event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) 
//                && event.getEventType() == KeyEvent.KEY_RELEASED) {
//            state = States.STALL;
//            setVelocity();
//        } else if (event.getCode() == KeyCode.SPACE && event.getEventType() == KeyEvent.KEY_RELEASED) {
//            makeShot();
//        } else if (event.getCode() == KeyCode.DIGIT1 && event.getEventType() == KeyEvent.KEY_PRESSED) {
//            Main.camera.setDefault();            
//        } else if (event.getCode() == KeyCode.DIGIT2 && event.getEventType() == KeyEvent.KEY_PRESSED) {
//            Main.camera.setPlayerBound(this);
//        }
    }
    
}
