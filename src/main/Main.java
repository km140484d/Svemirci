package main;

import settings.*;
import sprites.shots.*;
import sprites.*;
import sprites.enemies.*;
import cameras.Camera;
import com.google.gson.*;
import java.io.*;
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
import settings.deserializers.*;
import sprites.awards.*;
import static sprites.enemies.Enemy.getWidth;

public class Main extends Application {  
    private static final String GAME = "Svemirci";
    public static final double WINDOW_WIDTH = 1200;//1200
    public static final double WINDOW_HEIGHT = 700;//700
    public static final double MIN_WINDOW_WIDTH = 1000;
    public static final double MIN_WINDOW_HEIGHT = 600;
    private static final boolean FULL_SCREEN = false;
    private static final int HARD = 5;
    
    private static final int ENEMIES_IN_A_ROW = 8;
    private static final int ENEMIES_IN_A_COLUMN = 4;
    
    private static final int TIME_WORTH = 1; 
    
    private static final String SETTINGS_FILE = "settings/config.json";
    
    //Nodes on scene -----------------------------
    private Scene scene;
    private Group root = new Group();
    public static Camera camera = new Camera();
    private static Background background;
    private static Player player;
    private List<Shot> shots;
    private static List<Enemy> enemies = new LinkedList<>();
    private static List<Enemy> shotEnemies = new ArrayList<>();
    private static List<Projectile> projs = new ArrayList<>();
    private static List<Coin> coins = new ArrayList<>();
    
    private static List<Sprite> delObjects = new ArrayList<>();

    private boolean theEnd = false, goodbye = false;
    private double time = 0;    
    private int time_passed = 0;
    private String time_msg = "Time: ";
    private Text time_text;

    private String end_msg = " POINTS WON";
    private String life_msg = "LIFE LOST, %d REMAIN";
    private Text end_text;    
  
    private boolean rst = false; //random shoot time
    private boolean shoot = false; //commander order shoot
    private static boolean attack = false; //enemy to the front line
    
    public static double width = WINDOW_WIDTH;
    public static double height = WINDOW_HEIGHT;
    
    public static Constants constants;
    
    @Override
    public void start(Stage primaryStage) {
        if (!fileInitialization())
            return;       
        
        background = new Background(width, height);
        root.getChildren().add(background);
        
        player = new Player();
        player.setTranslateX(width / 2);
        player.setTranslateY(height * 0.95);
        camera.getChildren().add(player);
 
        displayPoints();
        displayTime();
        
        boolean first = true;
        
        Commander commander = null;
        for (int i = 0; i < ENEMIES_IN_A_COLUMN; i++) 
            for (int j = 0; j < ENEMIES_IN_A_ROW; j++) {
                Enemy enemy;
                if ((i + j) % 3 == 2)
                    enemy = new Warrior((j+1) * width / (ENEMIES_IN_A_ROW + 1),-((ENEMIES_IN_A_ROW - i-1)*Enemy.getHeight()*2),
                                    (j+1) * width / (ENEMIES_IN_A_ROW + 1),(i+1) * Enemy.getHeight()*2, commander);
                else
                    if ((i + j) % 3 == 1){
                        if (commander == null){
                            enemy = new Commander((j+1) * width / (ENEMIES_IN_A_ROW + 1),-((ENEMIES_IN_A_ROW - i-1)*Enemy.getHeight()*2),
                                        (j+1) * width / (ENEMIES_IN_A_ROW + 1),(i+1) * Enemy.getHeight()*2);
                            commander = (Commander)enemy;
                        }else{
                            enemy = new Warrior((j+1) * width / (ENEMIES_IN_A_ROW + 1),-((ENEMIES_IN_A_ROW - i-1)*Enemy.getHeight()*2),
                                    (j+1) * width / (ENEMIES_IN_A_ROW + 1),(i+1) * Enemy.getHeight()*2, commander);
                        }
                    }else
                        enemy = new Scout((j+1) * width / (ENEMIES_IN_A_ROW + 1),-((ENEMIES_IN_A_ROW - i-1)*Enemy.getHeight()*2),
                                    (j+1) * width / (ENEMIES_IN_A_ROW + 1),(i+1) * Enemy.getHeight()*2);                
                enemy.showBar(camera);
                camera.getChildren().add(enemy);
                
                if (i == 0 && first){
                    Enemy.setMovement(enemy.getTranslateX() - getWidth()*3/4);
                    first = false;
                }
                
                if (i == ENEMIES_IN_A_COLUMN - 1 && j == ENEMIES_IN_A_ROW - 1)
                    enemy.markLast();
                
                //blinking
                if ((i + j) % 2 == 0)
                   enemy.startBlinking(enemy.getLeftEye()); 
                else
                   enemy.startBlinking(enemy.getRightEye());             
                enemies.add(enemy);
            }
        
        root.getChildren().add(camera);
        scene = new Scene(root, width, height);
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
        primaryStage.setFullScreen(FULL_SCREEN);
        primaryStage.show();
        
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {                
                update();
                if (time_passed < (int)time){
                    time_passed++;
                    time_text.setText(time_msg + time_passed);
                    double rand = Math.random();
                    if (!theEnd && (rand < 0.20)){
                        if (rand < 0.04)
                            shoot = true;
                        else
                            if ((!attack) && (rand < 0.07))
                                attack = true;                                
                            else
                                rst = true;
                    }
                }
            }
        }.start();
    }
    
    public boolean fileInitialization(){
        try(InputStream in = getClass().getClassLoader().getResourceAsStream(SETTINGS_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));){
            
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
            gsonBuilder.registerTypeAdapter(Commands.class, new CommandsDeserializer());
            gsonBuilder.registerTypeAdapter(Labels.class, new LabelsDeserializer());
            gsonBuilder.registerTypeAdapter(Score.class, new ScoreDeserializer());
            Gson gson = gsonBuilder.create();            
            constants = new Gson().fromJson(br, Constants.class);
            Commands comm = constants.getCommands();
            Labels labels = constants.getLabels();
            Score[] scores = constants.getHigh_scores();
            System.out.println("GSON object " + constants.getName()+ ", " + constants.getWidth() + ", " 
                    + constants.getHeight());
//            if (comm != null){
//                System.out.println("COMMANDS " + comm.getPlayer2_down());
//            }            
//            if (labels != null){
//                System.out.println("LABELS " + labels.getStart());
//            }
//            if (scores != null && scores.length != 0){
//                System.out.println("SCORES " + scores[0].getName());
//            }
            if (comm != null && labels != null && scores != null)
                return true;
            else
                return false;
            
        }catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '"
                    + SETTINGS_FILE + "'");
            return false;
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                    + SETTINGS_FILE + "'");
            return false;
        }
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
    
    public static void endAttack(){
        attack = false;
    }
        
    public void update() {
        if (!theEnd) {            
            camera.getChildren().clear();             
            //enemy player update
            for(int i = 0; i < enemies.size(); i++){
                Enemy enemy = enemies.get(i);
                if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())){
                    updatePlayer();
                    break;
                }
            }              
            //commander orders attack
            if (shoot){                    
                pickCommander();
                shoot = false;
            }else{
                int randEnemy = (int)(Math.random() * (enemies.size() - 1));
                //random enemy shooting
                if (rst){                        
                    projs.add(enemies.get(randEnemy).shootProjectile());
                    rst = false;
                }else{
                    //enemy going forward
                    if ((attack) && Enemy.isUpdate())
                        pickScout();

                }
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
                                if (currentEnemy.enemyShot(shot.getShotStrength()))
                                    destroyEnemy(currentEnemy);
                                else
                                    currentEnemy.setRedMark(false);
                            }
                        }else{                            
                            if (currentEnemy.enemyShot(shot.getShotStrength()))
                                destroyEnemy(currentEnemy);
                            Main.removeSprite(shot);
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
        }else{
            if (!goodbye){
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
                goodbye = true;
            }
        }
    }
    
    public static void addProjectile(Projectile proj){
        projs.add(proj);
    }
    
    public void pickScout(){
        List<Scout> scouts = new ArrayList<>();
        enemies.forEach(e -> {
            if (e instanceof Scout){
                scouts.add((Scout)e);
            }
        });
        if (!scouts.isEmpty()){
            int randScout = (int)(Math.random() * (scouts.size() - 1));
            scouts.get(randScout).moveOnPlayer(player.getTranslateX(), player.getTranslateY());
        }
    }
    
    public void pickCommander(){//stream
        List<Commander> commanders = new ArrayList<>();
        enemies.forEach(e -> {
            if (e instanceof Commander){
                commanders.add((Commander)e);
            }
        });
        if (!commanders.isEmpty()){
            int randCommander = (int)(Math.random() * (commanders.size() - 1));
            commanders.get(randCommander).orderAttack(player.getTranslateX(), player.getTranslateY());
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
            }else
                theEnd = true; 
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
        if (enemy instanceof Warrior)
            ((Warrior)enemy).notifyCommander();
        if (enemy.isChosen()){
            Main.endAttack();
            Enemy.setUpdate(true);
        }
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
                                if (rand < 0.2)
                                    player.addBonus(new Bonus(Bonus.pickBonus(), x, y));
                                else
                                    coins.add(new Coin(x, y));                                          
                            }
                        },
                        new KeyValue(rot.angleProperty(), 360))
        );
        if (enemies.isEmpty() && shotEnemies.size() == 1){
            tl.setOnFinished(t -> {
                theEnd = true;
            });
        }        
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
