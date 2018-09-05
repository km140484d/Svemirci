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
    
    protected Rectangle body;
    
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
    private boolean redMark;
    private boolean last = false;
    
    private boolean entry = true;
    private static boolean update = false, knockOut = false;
    private double velocityX = -1, velocityY = 2;   
    private double posX, posY;

    private double currX, currY, playerX, playerY;
    private boolean chosen = false;
    private double oldVelocityX, attackVelocityX, attackVelocityY;
    private boolean destination = false, animation = false;
    
    private static double movement;
    
    private ScaleTransition st;
    
    public Enemy(double fromX, double fromY, double toX, double toY) {
        this.posX = toX;
        this.posY = toY;
        //ears
        for(int i = 0; i < ears.length; i++){
            ears[i] = new Path(
                        new MoveTo(i==0 ? -EN_WIDTH*3/8 : EN_WIDTH*3/8, 0), 
                        new LineTo(i==0 ? -EN_WIDTH*25/24 : EN_WIDTH*25/24, -EN_HEIGHT*2/3),
                        new VLineTo(EN_HEIGHT*2/3),
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
            fly_rot.setPivotX(i==0 ? -EN_WIDTH*3/8 : EN_WIDTH*3/8);
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
        body.setStroke(Color.BLACK);
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
        setTranslateX(fromX);
        setTranslateY(fromY);
        
        //bar
        stat = new Group();
        for(int i = 0; i < bars.length; i++){
            bars[i] = new Rectangle(EN_WIDTH/2, EN_HEIGHT/8);
            bars[i].setFill(i==1 ? Color.CHARTREUSE : Color.BLACK);
        }
        stat.getChildren().addAll(bars);
        stat.setTranslateX(getTranslateX()-EN_WIDTH/4);
        stat.setTranslateY(getTranslateY()-EN_HEIGHT/4 - EN_HEIGHT*2/3);       
    }
    
    public void startBlinking(Group eye){
        st = new ScaleTransition(Duration.seconds(2), eye); 
        st.setFromY(1);
        st.setToY(0.2);
        st.setAutoReverse(true);
        st.setCycleCount(Animation.INDEFINITE);
        st.play();         
    }
    
    public static void setMovement(double move){
        movement = move;
    }
    
    public void moveOnPlayer(double playerX, double playerY){
        this.playerX = playerX;
        this.playerY = playerY;
        this.currX = getTranslateX();
        this.currY = getTranslateY();
        update = false;
        chosen = true;
        this.oldVelocityX = this.velocityX;
        double x = (playerX - getTranslateX());
        double y = playerY - getTranslateY();
        double alpha1 = Math.atan(x/y); //proj above player
        double alpha2 = Math.atan(Math.abs(y/x)); //proj below player
        if (getTranslateY() > playerY){
            if (playerX < getTranslateX())
                this.velocityX = -2*Math.cos(alpha2);
            else
                this.velocityX = 2*Math.cos(alpha2);
            this.velocityY = 2*Math.sin(-Math.abs(alpha2));
        }else{
            this.velocityX = 2*Math.sin(alpha1);
            this.velocityY = 2*Math.cos(alpha1);
        }
    }
    
    public Projectile shootProjectile(){
        double x = getTranslateX() + getWidth()/2;
        double y = getTranslateY() + getHeight()/2;
        return new Projectile(x, y);
    }
    
    public void markLast(){
        last = true;
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
    
    public static void setKnockOut(boolean state){
        knockOut = state;
    }
    
    public boolean isChosen(){
        return chosen;
    }
    
    public boolean enemyShot(int shotStrength){
        strength-=shotStrength;
        if (strength <= 0)
            return true;
        else{
            ScaleTransition hit = new ScaleTransition(Duration.seconds(0.1), this);
            hit.setFromX(1); hit.setByX(0.1);
            hit.setFromY(1); hit.setByY(0.1);
            hit.setAutoReverse(true);
            hit.setCycleCount(2);
            hit.play();
            bars[1].setWidth(bars[1].getWidth() - (EN_WIDTH/(2*enemyStrength())) * shotStrength);
            return false;
        }
    }
    
    public boolean isRedMark(){
        return redMark;
    }  
    
    public void setRedMark(boolean value){
        redMark = value;
    }
    
    public static double getWidth(){
        return EN_WIDTH*7/3*Main.width/Main.WINDOW_WIDTH;
    }
    
    public static double getHeight(){
        return EN_HEIGHT*4/3*Main.height/Main.WINDOW_HEIGHT;
    }
    
    public abstract int enemyStrength();
    
    @Override
    public void resizeWindow(double ratioWidth, double ratioHeight){
        super.resizeWindow(ratioWidth, ratioHeight);
        posX *= ratioWidth;
        posY *= ratioHeight;
        currX *= ratioWidth;
        currY *= ratioHeight;
        playerX *= ratioWidth;
        playerY *= ratioHeight;
        velocityX *= ratioWidth;
        velocityY *= ratioHeight;
        oldVelocityX *= ratioWidth;
        attackVelocityX *= ratioWidth;
        attackVelocityY *= ratioHeight;
        Scale scale = new Scale();
        scale.setX(ratioWidth);
        scale.setY(ratioHeight);
        stat.getTransforms().add(scale);
        stat.setTranslateX(stat.getTranslateX()*ratioWidth);
        stat.setTranslateY(stat.getTranslateY()*ratioHeight);
    };
    
    @Override
    public void update() {
        double x = getTranslateX();
        double y = getTranslateY();
        stat.setTranslateX(x - EN_WIDTH/4);
        stat.setTranslateY(y - EN_HEIGHT/4 - EN_HEIGHT*2/3);
        if (!knockOut){
            if (entry){
                if (y >= posY){
                    entry = false;
                    setTranslateY(posY);
                    if (last){
                        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), this);
                        tt.setToX(getTranslateX());
                        tt.setOnFinished(t -> update = true);
                        tt.play();
                    }
                }else
                    setTranslateY(y + velocityY);
            }else{
                if (update){
                    setTranslateX(x + velocityX);
                    if (x <= posX - movement){
                        velocityX = Math.abs(velocityX);
                    }else{
                        if (x >= posX + movement)
                            velocityX = -Math.abs(velocityX);
                    }  
                }else{
                    if (chosen){
                        setTranslateX(x + velocityX);
                        setTranslateY(y + velocityY);
                        if (!destination){
                            if ((((currX < playerX) && (x >= playerX)) || ((currX > playerX) && (x <= playerX))) && 
                                    (((currY < playerY) && (y >= playerY)) || ((currY > playerY) && (y <= playerY)))){
                                Timeline time = new Timeline(
                                        new KeyFrame(Duration.ZERO, 
                                                new KeyValue(mouth.scaleXProperty(), 1)),
                                        new KeyFrame(Duration.seconds(2),
                                                new KeyValue(mouth.scaleXProperty(), .2))
                                );  
                                time.setAutoReverse(true);
                                time.setCycleCount(2);
                                time.setOnFinished(a -> {
                                    velocityX = -attackVelocityX;
                                    velocityY = -attackVelocityY;
                                    animation = true;
                                });
                                time.play();
                                attackVelocityX = velocityX; attackVelocityY = velocityY;
                                velocityX = 0; velocityY = 0;
                                destination = true;
                            }
                        }else{
                            if (animation){
                                if ((((currX < playerX) && (x <= currX)) || ((currX > playerX) && (x >= currX))) && 
                                        (((currY < playerY) && (y <= currY)) || ((currY > playerY) && (y >= currY)))){
                                    velocityX = oldVelocityX;
                                    update = true;
                                    chosen = false;
                                    setTranslateX(currX);
                                    setTranslateY(currY);
                                    Main.endAttack();
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
