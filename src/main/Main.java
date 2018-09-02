package main;

import sprites.shots.*;
import sprites.*;
import sprites.enemies.*;
import cameras.Camera;
import java.util.*;
import javafx.animation.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.transform.*;
import javafx.stage.*;
import javafx.util.Duration;
import sprites.awards.*;
import sprites.awards.Bonus.*;

public class Main extends Application {    
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 700;
    
    public static final int ENEMIES_IN_A_ROW = 8;
    public static final int ENEMIES_IN_A_COLUMN = 4;
    
    private static final int HARD = 5;
    
    private static final String GAME = "Svemirci";
    
    private static final double RED_IND_HEIGHT = 115;
    private static final double YELLOW_IND_HEIGHT = 70;

    private Background background;
    private static Player player;
    private static List<Enemy> enemies;
    private static List<Enemy> shotEnemies = new ArrayList<>();
    private List<Shot> shots;
    
    public static Camera camera;
    
    private Group root;
    private double time = 0;
    private boolean theEnd = false;
    
    private int time_passed = 0;
    private String time_msg = "Time: ";
    private Text time_text;
    
    private static int points = 0;
    private String end_msg = " POINTS WON";
    private String life_msg = "LIFE LOST, %d REMAIN";
    private Text end_text;    
    private static List<Coin> coins = new ArrayList<>();
    
    private static List<Bonus> bonuses = new ArrayList<>();
    private static BonusIndicator collectedRed = null;
    private static List<BonusIndicator> collectedYellow = new ArrayList<>();
        
    private boolean rst = false; //random shoot time
    private static List<Projectile> projs = new ArrayList<>();
    
    private static String points_msg = " Points: ";
    private static Text points_text;
    
    private static List<Sprite> delObjects = new ArrayList<>();
    private static List<Enemy> delEnemies = new ArrayList<>();
    
    int choose = 0;
    
    @Override
    public void start(Stage primaryStage) {
        enemies = new LinkedList<>();
        root = new Group();
        camera = new Camera();
        
        background = new Background(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.getChildren().add(background);
        
        displayTime();        
        displayPoints();
        
        player = new Player();
        player.setTranslateX(WINDOW_WIDTH / 2);
        player.setTranslateY(WINDOW_HEIGHT * 0.95);
        camera.getChildren().add(player);
        
        for (int i = 0; i < ENEMIES_IN_A_COLUMN; i++) 
            for (int j = 0; j < ENEMIES_IN_A_ROW; j++) {
                Enemy enemy;
                if (i + j % 3 == 1)
                    enemy = new Warrior();
                else
                    if (i + j % 3 == 2)
                        enemy = new Commander();
                    else
                        enemy = new Scout();
                enemy.setTranslateX((j+1) * WINDOW_WIDTH / (ENEMIES_IN_A_ROW + 1));
                Timeline arrival = new Timeline(
                        new KeyFrame(Duration.ZERO, 
                                new KeyValue(enemy.translateYProperty(), -((ENEMIES_IN_A_ROW - i-1)*100), Interpolator.EASE_OUT)), //ENEMIES_SPACE = 100
                        new KeyFrame(Duration.seconds(4), 
                                new KeyValue(enemy.translateYProperty(), (i+1) * 100, Interpolator.EASE_BOTH))//ENEMIES_SPACE = 100
                );                
                if (i == ENEMIES_IN_A_COLUMN - 1 && j == ENEMIES_IN_A_ROW - 1)
                    arrival.setOnFinished(e -> Enemy.setUpdate(true));
                arrival.play();
                enemy.showBar(camera);
                camera.getChildren().add(enemy);
                
                //blinking
                ScaleTransition st;
                if ((i + j) % 2 == 0){
                   st = new ScaleTransition(Duration.seconds(2), enemy.getLeftEye()); 
                }else{
                   st = new ScaleTransition(Duration.seconds(2), enemy.getRightEye());
                }
                st.setFromY(1);
                st.setToY(0.2);
                st.setAutoReverse(true);
                st.setCycleCount(Animation.INDEFINITE);
                st.play();                
                enemies.add(enemy);
            }
        
        root.getChildren().add(camera);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(player);
        scene.setOnKeyReleased(player);
        
        primaryStage.setTitle(GAME);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {                
                update();
                if (time_passed < (int)time){
                    time_passed++;
                    time_text.setText(time_msg + time_passed);
                    if (Math.random() < 0.15){
                        rst = true;
                    }
                }
            }
        }.start();
    }
    
    public void resetPlayer(){
        //reset bonuses, remove them
        int yellow = collectedYellow.size();
        for(int i=0; i < yellow; i++)
            removeYellowBonus(collectedYellow.get(0));
        player.setRedBonusType(null);
        //reset player
        player.reset();
        player.setTranslateX(WINDOW_WIDTH / 2);
        player.setTranslateY(WINDOW_HEIGHT * 0.95);
        Timeline playerAnotherShot = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(player.opacityProperty(), 0, Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(1), new KeyValue(player.opacityProperty(), 1, Interpolator.DISCRETE))
        );
        playerAnotherShot.play();
        
    }
    
    public void updatePlayer(){
        if (!player.invincible()){
            if (!background.loseLife()){
                end_text = new Text(WINDOW_WIDTH/2 - 100, WINDOW_HEIGHT/2 - 30, String.format(life_msg, background.getLifeNumber()));
                end_text.setFill(Color.ORANGERED);
                end_text.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));
                root.getChildren().add(end_text);
                FadeTransition ft = new FadeTransition(Duration.seconds(2), end_text);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.play();
                resetPlayer();
            }
        }
    }
    
    public void update() {
        if (theEnd == false) {            
            camera.getChildren().clear();             
            //enemy player update
            for(int i = 0; i < enemies.size(); i++){
                Enemy enemy = enemies.get(i);
                if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())){
                    updatePlayer();
                    break;
                }
            }          
             
            if (enemies.isEmpty() || (background.getLifeNumber() == 0)) {
                theEnd = true;
                camera.getChildren().clear();
                camera.getChildren().addAll(shots);
                camera.getChildren().addAll(enemies);
                camera.getChildren().addAll(coins);
                camera.getChildren().addAll(projs);
                end_text = new Text(WINDOW_WIDTH/2 - 100, WINDOW_HEIGHT/2 - 30, points + end_msg);
                end_text.setFill(Color.ORANGERED);
                end_text.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));
                root.getChildren().add(end_text);
            }else {  
                //random enemy shooting
                if (rst){
                    int randEnemy = (int)(Math.random() * (enemies.size() - 1));
                    Enemy e = enemies.get(randEnemy);
                    double x = e.getTranslateX() + e.getBody().getWidth()/2;
                    double y = e.getTranslateY() + e.getBody().getHeight()/2;
                    projs.add(new Projectile(x, y));
                    rst = false;
                }                  
                
                //display game objects
                //player
                camera.getChildren().add(player);
                
                //enemy and shots update
                shots = player.getShots(); 
                shots.forEach(s -> {              
                    for (int j = 0; j < enemies.size(); j++) {
                        Enemy currentEnemy = enemies.get(j);
                        if (s.getBoundsInParent().intersects(currentEnemy.getBoundsInParent())) {
                            if (s instanceof Stream || s instanceof Boomerang){
                                if (currentEnemy.isRedMark()){
                                    currentEnemy.enemyShot();
                                    currentEnemy.setRedMark(false);
                                }
                            }else{
                                Main.removeSprite(s);
                                currentEnemy.enemyShot();
                            }                                
                            break;
                        }
                    }
                });
                delEnemies.forEach(e -> destroyEnemy((Enemy)e));
                delEnemies.clear();
                shots.removeAll(delObjects);
                shots.forEach(e -> e.update());
                camera.getChildren().addAll(shots); 
                
                //coins                
                coins.forEach(c -> {
                    c.update();
                    if (c.getBoundsInParent().intersects(player.getBoundsInParent())){
                        points++; points_text.setText(points_msg + points); Main.removeSprite(c);
                    }                    
                });
                coins.removeAll(delObjects);
                camera.getChildren().addAll(coins);
                
                //enemies
                camera.getChildren().addAll(shotEnemies);
                camera.getChildren().addAll(enemies);  
                enemies.forEach(e -> {e.update(); e.showBar(camera);});
                
                //projectiles                                
                projs.forEach(p -> {
                    p.update();
                    if (p.getBoundsInParent().intersects(player.getBoundsInParent()))
                            updatePlayer();
                });
                projs.removeAll(delObjects);
                camera.getChildren().addAll(projs);
                
                //bonuses        
                for(int i = 0; i < bonuses.size(); i++){
                    Bonus bonus = bonuses.get(i);
                    bonus.update();
                    if (bonus.getBoundsInParent().intersects(player.getBoundsInParent())){
                        consumed(bonus);
                        bonuses.remove(bonus);
                        break;
                    }
                }
                bonuses.removeAll(delObjects);
                camera.getChildren().addAll(bonuses);
                
                for(int i = 0; i < collectedYellow.size(); i++){
                    BonusIndicator yellow = collectedYellow.get(i);
                    if (yellow.decTime())
                        removeYellowBonus(yellow);
                    else
                        if (!camera.getChildren().contains(yellow))
                            camera.getChildren().add(yellow);
                                    
                }
                collectedYellow.forEach(y -> {
                    y.setTranslateX(BonusIndicator.getWidth()/2 + 
                        collectedYellow.indexOf(y)*BonusIndicator.getWidth());
                });
                
                if (collectedRed != null && !camera.getChildren().contains(collectedRed))
                    camera.getChildren().add(collectedRed);
                
                camera.updateCamera(player);
                player.setShots(shots);
                player.update();
                background.update();
                time += 1.0 / 60;
            }   

        }
    }
    
    public void removeYellowBonus(BonusIndicator yellow){
        switch((YellowBonus)yellow.getType()){
            case KnockOut:
                Enemy.setUpdate(true);
                break;
            case Rotation:
                player.setRotate(false);                                                              
                RotateTransition rt = new RotateTransition(Duration.seconds(1.5), player);
                rt.setToAngle(0);
                rt.play();
                break; 
            case ShotGrowth:
                Shot.setEnlarge(false);
                break;
            case Shield:
                player.setShield(false);
                break;
            case Speed:
                player.setSpeed(false);
                break;
        }
        collectedYellow.remove(yellow);
    }
    
    public static void removeSprite(Sprite sprite){
        delObjects.add(sprite);
    }
    
    public static void removeEnemy(Enemy enemy){
        delEnemies.add(enemy);
    }
    
    public void destroyEnemy(Enemy enemy){
        enemies.remove(enemy);
        shotEnemies.add(enemy);                        
        Rotate rot = new Rotate();
        enemy.getTransforms().add(rot);
        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(rot.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(1),
                        t -> {
                            double x = enemy.getTranslateX() + enemy.getBody().getWidth()/2;
                            double y = enemy.getTranslateY() + enemy.getBody().getHeight()/2;
                            shotEnemies.remove(enemy);
                            camera.getChildren().remove(enemy);
                            double rand = Math.random();
                            points += enemy.enemyStrength()/HARD;// points won from kill shot
                            points_text.setText(points_msg + points);
//                            if (rand < 0.6){
//                                if (rand < 0.1){
                                    //bonuses.add(new Bonus(Bonus.pickBonus(), x, y));
                                    Bonus bonus;
                                    if (choose == 0)
                                        bonus = new Bonus(Bonus.GreenBonus.Life, x, y);
                                    else
                                        if (choose == 1)
                                            bonus = new Bonus(Bonus.YellowBonus.Shield, x, y);
                                        else
                                            bonus = new Bonus(Bonus.YellowBonus.KnockOut, x, y);
                                    bonuses.add(bonus);
                                    choose = (choose + 1) %3;
//                                }else
//                                    coins.add(new Coin(x, y));                                          
//                            }
                        },
                        new KeyValue(rot.angleProperty(), 360))
        );
        tl.play();
    }
    
    //action
    public void consumed(Bonus bonus){
        BonusType type = bonus.getBonusType();
        if (type instanceof Bonus.RedBonus){
            collectedRed = new BonusIndicator(bonus.getBonusType(), bonus.getPath());
            collectedRed.setTranslateX(BonusIndicator.getWidth()/2);
            collectedRed.setTranslateY(Main.RED_IND_HEIGHT);
            actionRedBonus((Bonus.RedBonus)type);             
        }else
            if (type instanceof Bonus.YellowBonus){
                for(int i=0; i<collectedYellow.size(); i++){
                    if (collectedYellow.get(i).getType().equals(type)){
                        collectedYellow.get(i).reset();
                        return;
                    }
                }                
                BonusIndicator yellow = new BonusIndicator(bonus.getBonusType(), bonus.getPath());
                collectedYellow.add(yellow);
                yellow.setTranslateX(BonusIndicator.getWidth()/2 + 
                        collectedYellow.indexOf(yellow)*BonusIndicator.getWidth());
                yellow.setTranslateY(Main.YELLOW_IND_HEIGHT);
                actionYellowBonus((Bonus.YellowBonus)type);
            }else
                if (type instanceof Bonus.GreenBonus)
                    actionGreenBonus((Bonus.GreenBonus) type);
                else
                    actionBlackBonus((Bonus.BlackBonus)type);
    }
    
    public void actionRedBonus(Bonus.RedBonus bonus){
        setEnemyRedMark(true);
        switch(bonus){
            case Stream:
                player.setRedBonusType(Bonus.RedBonus.Stream);
                break;
            default:
                player.setRedBonusType(Bonus.RedBonus.Boomerang);
                break;
        }
    }
    
    public void actionYellowBonus(Bonus.YellowBonus bonus){
        switch(bonus){
            case Speed:
                player.setSpeed(true);
                break;
            case Rotation:
                player.setRotate(true);
                break;
            case Shield:
                player.setShield(true);
                break;
            case ShotGrowth:
                Shot.setEnlarge(true);
                break;
            case KnockOut:
                Enemy.setUpdate(false);
                break;
        }
    }
    
    public void actionGreenBonus(Bonus.GreenBonus bonus){
        switch(bonus){
            case Life:
                background.collectLife();
                break;
            case PointS:
                break;
            case PointM:
                break;
            case PointL:
                break;       
        }
    }
    
    public void actionBlackBonus(Bonus.BlackBonus bonus){
        
    }
    
    public static void setEnemyRedMark(boolean mark){
        enemies.forEach(e -> e.setRedMark(mark));
    }
    
    public static void setCollectedRed(Bonus bonus){
        collectedRed = (bonus == null) ? null : new BonusIndicator(bonus.getBonusType(), bonus.getPath());
    }
    
    private void displayTime() {
        time_text = new Text(WINDOW_WIDTH/2 - 15, 20, time_msg + time_passed);
        time_text.setFill(Color.RED);
        time_text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        root.getChildren().add(time_text); 
    }
    
    
    public void displayPoints(){
        points_text = new Text(5, Life.getHeght()*2 + 20, points_msg + points);
        points_text.setFill(Color.RED);
        points_text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        root.getChildren().add(points_text); 
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    
}
