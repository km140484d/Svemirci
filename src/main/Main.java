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

public class Main extends Application {  
    private static final String GAME = "Svemirci";
    public static final double WINDOW_WIDTH = 1200;//1200
    public static final double WINDOW_HEIGHT = 700;//700
    public static final double MIN_WINDOW_WIDTH = 1000;
    public static final double MIN_WINDOW_HEIGHT = 600;
    private static final int HARD = 5;
    
    private static final int ENEMIES_IN_A_ROW = 8;
    private static final int ENEMIES_IN_A_COLUMN = 4;
    
    private static final int TIME_WORTH = 1;    
    
    //Nodes on scene -----------------------------
    private Group root;
    public static Camera camera;
    private static Background background;
    private static Player player;
    private List<Shot> shots;
    private static List<Enemy> enemies;
    private static List<Enemy> shotEnemies = new ArrayList<>();
    private static List<Projectile> projs = new ArrayList<>();
    private static List<Coin> coins = new ArrayList<>();
    
    private static List<Sprite> delObjects = new ArrayList<>();

    private boolean theEnd = false;
    private double time = 0;    
    private int time_passed = 0;
    private String time_msg = "Time: ";
    private Text time_text;

    private String end_msg = " POINTS WON";
    private String life_msg = "LIFE LOST, %d REMAIN";
    private Text end_text;    
  
    private boolean rst = false; //random shoot time
    
    public static double width = WINDOW_WIDTH;
    public static double height = WINDOW_HEIGHT;
    
    @Override
    public void start(Stage primaryStage) {
        enemies = new LinkedList<>();
        root = new Group();
        camera = new Camera();
        
        background = new Background(width, height);
        root.getChildren().add(background);
        
        player = new Player();
        player.setTranslateX(width / 2);
        player.setTranslateY(height * 0.95);
        camera.getChildren().add(player);
 
        displayPoints();
        displayTime(); 
        
        for (int i = 0; i < ENEMIES_IN_A_COLUMN; i++) 
            for (int j = 0; j < ENEMIES_IN_A_ROW; j++) {
                Enemy enemy;
                if (i + j % 3 == 1)
                    enemy = new Warrior((j+1) * width / (ENEMIES_IN_A_ROW + 1),-((ENEMIES_IN_A_ROW - i-1)*Enemy.getHeight()*2),
                                    (j+1) * width / (ENEMIES_IN_A_ROW + 1),(i+1) * Enemy.getHeight()*2);
                else
                    if (i + j % 3 == 2)
                        enemy = new Commander((j+1) * width / (ENEMIES_IN_A_ROW + 1),-((ENEMIES_IN_A_ROW - i-1)*Enemy.getHeight()*2),
                                    (j+1) * width / (ENEMIES_IN_A_ROW + 1),(i+1) * Enemy.getHeight()*2);
                    else
                        enemy = new Scout((j+1) * width / (ENEMIES_IN_A_ROW + 1),-((ENEMIES_IN_A_ROW - i-1)*Enemy.getHeight()*2),
                                    (j+1) * width / (ENEMIES_IN_A_ROW + 1),(i+1) * Enemy.getHeight()*2);                
                if (i == ENEMIES_IN_A_COLUMN - 1 && j == ENEMIES_IN_A_ROW - 1)
                    enemy.markLast();
                enemy.arriveOnScene();
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
        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(player);
        scene.setOnKeyReleased(player);
        scene.widthProperty().addListener(w -> {
            resizeWindow(scene.getWidth()/width, scene.getHeight()/height); 
            width = scene.getWidth();
            height = scene.getHeight();}
        );
        scene.heightProperty().addListener(h -> {
            resizeWindow(scene.getWidth()/width,scene.getHeight()/height); 
            height = scene.getHeight();
            width = scene.getWidth();}
        );        
        primaryStage.setTitle(GAME);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(MIN_WINDOW_WIDTH);
        primaryStage.setMinHeight(MIN_WINDOW_HEIGHT);
        primaryStage.setFullScreen(false);
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
    
    public void resizeWindow(double ratioWidth, double ratioHeight){
        background.resizeWindow(ratioWidth, ratioHeight);
        player.resizeWindow(ratioWidth, ratioHeight);
        enemies.forEach(e -> e.resizeWindow(ratioWidth, ratioHeight));
        shotEnemies.forEach(e -> e.resizeWindow(ratioWidth, ratioHeight));
        coins.forEach(c -> c.resizeWindow(ratioWidth, ratioHeight));
        projs.forEach(p -> p.resizeWindow(ratioWidth, ratioHeight));
        
        Scale scale = new Scale();
        scale.setX(ratioWidth);
        scale.setY(ratioHeight);
        time_text.getTransforms().add(scale);
        time_text.setTranslateX(time_text.getTranslateX()*ratioWidth);
        time_text.setTranslateY(time_text.getTranslateY()*ratioHeight);
        if (end_text != null){
            end_text.getTransforms().add(scale);
            end_text.setTranslateX(end_text.getTranslateX()*ratioWidth);
            end_text.setTranslateY(end_text.getTranslateY()*ratioHeight);
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
            if (enemies.isEmpty() || (player.getLifeNumber() == 0)) {
                theEnd = true;                
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
                
                //display game objects ---------------------------------------
                //player
                camera.getChildren().add(player);
                
                //enemy and shots update
                shots = player.getShots(); 
                for(int i=0; i < shots.size(); i++){
                    Shot shot = shots.get(i);                
                    for (int j = 0; j < enemies.size(); j++) {
                        Enemy currentEnemy = enemies.get(j);
                        if (shot.getBoundsInParent().intersects(currentEnemy.getBoundsInParent())) {
                            if (shot instanceof Stream || shot instanceof Boomerang){
                                if (currentEnemy.isRedMark()){
                                    if (currentEnemy.enemyShot())
                                        destroyEnemy(currentEnemy);
                                    else
                                        currentEnemy.setRedMark(false);
                                }
                            }else{
                                Main.removeSprite(shot);
                                if (currentEnemy.enemyShot())
                                    destroyEnemy(currentEnemy);
                            }                                
                            break;
                        }
                    }
                }
                shots.removeAll(delObjects);
                shots.forEach(e -> e.update());
                camera.getChildren().addAll(shots); 
                
                //coins                
                coins.forEach(c -> {
                    c.update();
                    if (c.getBoundsInParent().intersects(player.getBoundsInParent())){
                        player.addPoints(1);                         
                        Main.removeSprite(c);
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
                                
                camera.updateCamera(player);
                player.setShots(shots);
                player.update();
                background.update();
                time += 1.0 / 60;
            } 
        }
    }
    
    public void updatePlayer(){
        if (!player.invincible()){
            if (!player.loseLife()){
                end_text = new Text(width/2 - 100, height/2 - 30, String.format(life_msg, player.getLifeNumber()));
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
    
    public void resetPlayer(){
        //reset player
        player.reset();
        player.setTranslateX(width / 2);
        player.setTranslateY(height * 0.95);
        Timeline playerAnotherTry = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(player.opacityProperty(), 0, Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(1), new KeyValue(player.opacityProperty(), 1, Interpolator.DISCRETE))
        );
        playerAnotherTry.play();
        
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
                            player.addPoints(enemy.enemyStrength()/HARD);// points won from kill shot
                            if (rand < 0.6){
                                if (rand < 0.1)
                                    player.addBonus(new Bonus(Bonus.pickBonus(), x, y));
                                else
                                    coins.add(new Coin(x, y));                                          
                            }
                            if (enemies.isEmpty() && shotEnemies.isEmpty()){
                                player.addPoints(-(int)time*TIME_WORTH);
                                camera.getChildren().clear();
                                camera.getChildren().addAll(shots);
                                camera.getChildren().addAll(enemies);
                                camera.getChildren().addAll(coins);
                                camera.getChildren().addAll(projs);
                                end_text = new Text(width/2 - 100, height/2 - 30, player.getPoints() + end_msg);
                                end_text.setFill(Color.ORANGERED);
                                end_text.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));
                                root.getChildren().add(end_text);
                            }
                        },
                        new KeyValue(rot.angleProperty(), 360))
        );
        tl.play();
    }
        
    public static void removeSprite(Sprite sprite){
        delObjects.add(sprite);
    }

    public static void setEnemyRedMark(boolean mark){
        enemies.forEach(e -> e.setRedMark(mark));
    }
        
    private void displayTime() {
        time_text = new Text(width/2 - 15, 20, time_msg + time_passed);
        time_text.setFill(Color.RED);
        time_text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        root.getChildren().add(time_text); 
    }
    
    
    public void displayPoints(){
        background.getChildren().add(player.getPointsText());
    }
        
    public static void displayLife(Life life){
        background.getChildren().add(life);
    }
    
    public static void removeLife(Life life){
        background.getChildren().remove(life);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
 
}
