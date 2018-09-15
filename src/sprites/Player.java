package sprites;

import sprites.indicators.*;
import java.util.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.transform.*;
import javafx.util.*;
import main.Main;
import settings.*;
import settings.Commands.*;
import sprites.awards.*;
import sprites.awards.Bonus.*;
import sprites.enemies.*;
import sprites.shots.*;
import sprites.shots.Shot.*;
import static sprites.shots.Shot.BasicShotType.*;

public class Player extends Sprite implements EventHandler<KeyEvent> {
    private static final int LIVES_CNT = 3;
    
    private static final double WIDTH = 60;
    private static final double HEIGHT = 60;

    private static final double GUN_OUT_RX = WIDTH/4;
    private static final double GUN_OUT_RY = HEIGHT/2;    
    private static final double BODY_OUT_RX = WIDTH/2;
    private static final double BODY_OUT_RY = HEIGHT/4;    
    private static final double TUBE_HEIGHT = HEIGHT/6;
    private static final double TUBE_WIDTH = WIDTH/10;    
    private static final double STREAM_HEIGHT = TUBE_HEIGHT*4/3;    
    private static final double SHIELD_R = WIDTH*3/4;
    
    private static final double ROTATE_ANGLE = Main.constants.getRotate_angle();    
    private static final int MAX_SHOTS = Main.constants.getPlayer_max_shots();    

    private double velocityX = 0, velocityY = 0;
    private static enum States {LEFT, RIGHT, UP, DOWN, STALL};   
    private States state = States.STALL;

    private Shape body;
    private Group gun, leftTubeGroup, rightTubeGroup; 
    private Circle shield;
    private Timeline playerProtection;
    
    private List<Life> lives = new ArrayList<>();
    private List<Life> lostLives = new ArrayList<>();        
    private List<Shot> shots = new LinkedList<>();
    private BasicShotType shotType = BasicShotType.Tri;
    private int shotCnt = 1;

    private RedBonus redBonus; 
    private boolean rotate = false, speed = false, sizeUp = false;   
  
    private BonusIndicator collectedRed = null;
    private List<BonusIndicator> collectedYellow = new ArrayList<>();   
       
    private int points = 0;
    private String points_msg = Main.constants.getLabels().getPoints();
    private Text points_text;
    
    public static enum Type {PLAYER1, PLAYER2};
    private Type playerType;
    private Color playerColor;
    private String name;
    private double posX, posY;    
            
    private Commands comms = Main.constants.getCommands();
    private PlayerCommands playerComms = comms.getPlayer1();
    
    private VBox indicators;
    private HBox lifeBox, redBox, yellowBox;
    
    public static void resetPlayerGame(){
        Enemy.setKnockOut(false);
    }
    
    public Player(String name, Type playerType, double posX, double posY) {
        //type = player1/player2
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.playerType = playerType;
        playerColor = Color.SKYBLUE;
        Pos indicatorPos = Pos.TOP_LEFT, boxPos = Pos.CENTER_LEFT;
        if (playerType == Type.PLAYER2){
            playerComms = comms.getPlayer2();
            playerColor = Color.PALEGOLDENROD;
            indicatorPos = Pos.TOP_RIGHT;
            boxPos = Pos.CENTER_RIGHT;
        }
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
        body.setFill(playerColor);
        body.setStroke(Color.BLACK);
        body.setStrokeWidth(1.2);

        leftTubeGroup = makeTube("L");
        rightTubeGroup = makeTube("R");

        Arc gun_out = new Arc(0, 0, GUN_OUT_RX, GUN_OUT_RY, -10, 200);
        gun_out.setFill(playerColor);
        gun_out.setStroke(Color.BLACK);
        gun_out.setStrokeWidth(1.2);
        Arc gun_in = new Arc(0, 0, GUN_OUT_RX/3, GUN_OUT_RY/2, -10, 200);
        gun_in.setFill(Color.BLACK);
        gun = new Group();
        gun.getChildren().addAll(gun_out, gun_in);
        
        getChildren().addAll(gun, leftTubeGroup, rightTubeGroup, body);
        
        indicators = new VBox(Main.getHeight()/200);
        indicators.setMinWidth(Main.getWidth()*19/20);
        indicators.setAlignment(indicatorPos);
        indicators.setTranslateY(Main.getHeight()/100);
        indicators.setTranslateX(Main.getWidth()/40);
        
        lifeBox = getBox(boxPos);       
        for(int i = 0; i < LIVES_CNT; i++){
            Life life = new Life();          
            lives.add(life);
            lifeBox.getChildren().add(life);            
        }      
        
        points_text = new Text(points_msg + points);
        points_text.setFill(Color.CRIMSON);
        points_text.setFont(Main.FONT_S);
        HBox pointsBox = getBox(boxPos);
        pointsBox.getChildren().add(points_text);
        
        redBox = getBox(boxPos);       
        yellowBox = getBox(boxPos); 
        
        indicators.getChildren().addAll(lifeBox, pointsBox, redBox, yellowBox);
        setTranslateX(posX);
        setTranslateY(posY);
    }
    
    //indicator boxes
    public static HBox getBox(Pos pos){
        HBox box = new HBox(1);
        box.setAlignment(pos);
        return box;
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
        int yellow = collectedYellow.size();
        for(int i=0; i < yellow; i++)
            removeYellowBonus(collectedYellow.get(0));
        setRedBonusType(null);
        setCollectedRed(null);
        setShotType(null, true);
        setTranslateX(this.posX);
        setTranslateY(this.posY);
    }
    
    //resize ---------------------------------------
    @Override
    public void resizeWindow(double ratioWidth, double ratioHeight){
        super.resizeWindow(ratioWidth, ratioHeight);        
        lives.forEach(l -> l.resizeWindow(ratioWidth, ratioHeight));
        lostLives.forEach(l -> l.resizeWindow(ratioWidth, ratioHeight));
        shots.forEach(s -> s.resizeWindow(ratioWidth, ratioHeight));
        this.posX *= ratioWidth;
        this.posY *= ratioHeight;
             
        Scale scale = new Scale();
        scale.setX(ratioWidth); 
        scale.setY(ratioHeight);
        indicators.getTransforms().add(scale);
    }
    
    //lives ----------------------------------------
    public boolean loseLife(){
        Life lostLife = lives.get(lives.size() - 1);
        points -= Life.POINTS;
        points_text.setText(points_msg + points);
        if (lives.size() == 1){
            lifeBox.getChildren().remove(lostLife);
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
                lifeBox.getChildren().remove(lostLife);
                lostLives.remove(lostLife);
            });   
            disappearingLife.play();
            return false; //player has more lives
        }
    }
    
    public void collectLife(){
        Life life = new Life();             
        lives.add(life);
        lifeBox.getChildren().add(life);
    }
    
    public int getLifeNumber(){
        return lives.size();
    }
    
    //shots ----------------------------------------
    private void makeShot() {
        Shot shot = null;
        if (redBonus != null){            
            switch(redBonus){
                case Stream:
                    shot = new Stream(getRotate(), sizeUp);
                    break;
                case Boomerang:
                    shot = new Boomerang(getRotate(), sizeUp);
                    break;
            } 
            redBonus = null;
            setCollectedRed(null);
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
                shot = new Triangle(getRotate(), angle, sizeUp);
                break;
            case Rho:
                shot = new Rhombus(getRotate(), angle, sizeUp);
                break;
            case Pen:
                shot = new Pentagon(getRotate(), angle, sizeUp);
                break;
            case Hex:
                shot = new Hexagon(getRotate(), angle, sizeUp);
        }
        shot.setTranslateX(getTranslateX() + GUN_OUT_RY*Math.cos(Math.toRadians(trans)));
        shot.setTranslateY(getTranslateY() - GUN_OUT_RY*Math.sin(Math.toRadians(trans)));
        shot.setRotate(shot.getRotate() + angle);
        shots.add(shot);
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
    
    //bonuses ------------------------------------------------
    //action
    public void consumed(Bonus bonus){
        BonusType type = bonus.getBonusType();
        if (type instanceof Bonus.RedBonus){
            collectedRed = new BonusIndicator(bonus.getBonusType(), bonus.getPath());
            actionRedBonus((Bonus.RedBonus)type);             
        }else
            if (type instanceof Bonus.YellowBonus){
                for(int i=0; i<collectedYellow.size(); i++){
                    if (collectedYellow.get(i).getType().equals(type)){
                        collectedYellow.get(i).resetBonusTime();
                        return;
                    }
                }                
                BonusIndicator yellow = new BonusIndicator(bonus.getBonusType(), bonus.getPath());
                collectedYellow.add(yellow);
                actionYellowBonus((Bonus.YellowBonus)type);
            }else
                if (type instanceof Bonus.GreenBonus)
                    actionGreenBonus((Bonus.GreenBonus) type);
                else
                    actionBlackBonus((Bonus.BlackBonus)type);
    }
    
    public void actionRedBonus(Bonus.RedBonus bonus){
        Main.setEnemyRedMark(true);
        switch(bonus){
            case Stream:
                setRedBonusType(Bonus.RedBonus.Stream);
                break;
            default:
                setRedBonusType(Bonus.RedBonus.Boomerang);
                break;
        }
    }
    
    public void actionYellowBonus(Bonus.YellowBonus bonus){
        switch(bonus){
            case Speed:
                setSpeed(true);
                break;
            case Rotation:
                setRotate(true);
                break;
            case Shield:
                setShield(true);
                break;
            case ShotGrowth:
                setSizeUp(true);
                break;
            case KnockOut:
                Enemy.setKnockOut(true);
                break;
        }
    }
    
    public void actionGreenBonus(Bonus.GreenBonus bonus){
        switch(bonus){
            case Life:
                collectLife();
                points += Life.POINTS;
                break;
            case PointS:
                points += Bonus.POINT_S;
                break;
            case PointM:
                points += Bonus.POINT_M; 
                break;
            case PointL:
                points += Bonus.POINT_L;                 
                break;       
        }
        points_text.setText(points_msg + points);
    }
    
    public void actionBlackBonus(Bonus.BlackBonus bonus){
        switch(bonus){
            case Munition:
                setShotType(getShotType(), false);
                break;
            case Triangle:
                setShotType(Shot.BasicShotType.Tri, false);
                break;
            case Rhombus:
                setShotType(Shot.BasicShotType.Rho, false);
                break;
            case Pentagon:
                setShotType(Shot.BasicShotType.Pen, false);
                break;
            case Hexagon:
                setShotType(Shot.BasicShotType.Hex, false);
                break;
        }
    }
    
    public void removeYellowBonus(BonusIndicator yellow){
        switch((YellowBonus)yellow.getType()){
            case KnockOut:
                Enemy.setKnockOut(false);
                break;
            case Rotation:
                setRotate(false);                                                              
                RotateTransition rt = new RotateTransition(Duration.seconds(1.5), this);
                rt.setToAngle(0);
                rt.play();
                break; 
            case ShotGrowth:
                setSizeUp(false);
                break;
            case Shield:
                setShield(false);
                break;
            case Speed:
                setSpeed(false);
                break;
        }
        collectedYellow.remove(yellow);
    }
    
    public void setCollectedRed(Bonus bonus){
        collectedRed = (bonus == null) ? null : new BonusIndicator(bonus.getBonusType(), bonus.getPath());
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
    
    public void setSizeUp(boolean sizeUp){
        this.sizeUp = sizeUp;
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

    //points ---------------------------------------
    public int getPoints(){
        return points;
    }
    
    public Text getPointsText(){
        return points_text;
    }
    
    public void addPoints(int p){
        this.points += p;
        points_text.setText(points_msg + points);
    }

    //getters/setters
    public Type getPlayerType() {
        return playerType;
    }

    public void setPlayerType(Type playerType) {
        this.playerType = playerType;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public VBox getIndicators() {
        return indicators;
    }

    public void setIndicators(VBox indicators) {
        this.indicators = indicators;
    }   
    
    //movement -----------------------------------
    private void setVelocity() {
        double velocity = Main.constants.getInit_speed();
        if (speed)
            velocity = 2 * Main.constants.getInit_speed();
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
        } else if (getTranslateX() + velocityX > Main.getWidth() - WIDTH / 2 - 5) {
            setTranslateX(Main.getWidth() - WIDTH / 2 - 5);
        } else {
            setTranslateX(getTranslateX() + velocityX);
        }
        
        if (getTranslateY() + velocityY < HEIGHT / 2 + 5) {
            setTranslateY(HEIGHT / 2 + 5);
        } else if (getTranslateY() + velocityY > Main.getHeight() - HEIGHT / 2 - 5) {
            setTranslateY(Main.getHeight() - HEIGHT / 2 - 5);
        } else {
            setTranslateY(getTranslateY() + velocityY);
        }

        yellowBox.getChildren().clear();
        for(int i = 0; i < collectedYellow.size(); i++){
            BonusIndicator yellow = collectedYellow.get(i);
            if (yellow.decTime())
                removeYellowBonus(yellow);
            else
                if (!yellowBox.getChildren().contains(yellow))
                    yellowBox.getChildren().add(yellow);

        }
        redBox.getChildren().clear();
        if (collectedRed != null && !Main.getCamera().getChildren().contains(collectedRed))
            redBox.getChildren().add(collectedRed);
        Main.getCamera().getChildren().add(indicators);
    }

    @Override
    public void handle(KeyEvent event) {        
        if (event.getEventType() == KeyEvent.KEY_PRESSED){
            KeyCode code = event.getCode();
            if ((code.equals(playerComms.getUp())) || (code == playerComms.getDown()) 
                    || (code == playerComms.getLeft()) || (code == playerComms.getRight())){
                if (code == playerComms.getUp()){
                    state = States.UP;
                }else{
                    if (code == playerComms.getDown())
                        state = States.DOWN;
                    else{
                        if (code == playerComms.getRight())
                            state = States.RIGHT;
                        else
                            state = States.LEFT;
                    }
                }
                setVelocity();
            }else{
                if ((code == playerComms.getRotate_left()) && rotate)
                    setRotate(getRotate() - ROTATE_ANGLE);  
                else
                    if ((code == playerComms.getRotate_right()) && rotate)
                        setRotate(getRotate() + ROTATE_ANGLE);    
            }
        }else{                
            if (event.getEventType() == KeyEvent.KEY_RELEASED){
                KeyCode code = event.getCode();
                if (code == playerComms.getUp() || code == playerComms.getDown()
                        || code == playerComms.getLeft() || code == playerComms.getRight()){
                    state = States.STALL;
                    setVelocity();
                }else{
                    if (code == playerComms.getShoot())
                        makeShot();
                }
            }
        }
    }
}