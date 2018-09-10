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
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.transform.*;
import javafx.stage.*;
import javafx.util.Duration;
import menu.*;
import settings.deserializers.*;
import sprites.awards.*;

public class Main extends Application {  
    public static final Font FONT = Font.font("Sylfaen", FontWeight.BOLD, 24);
    public static final Font FONT_S = Font.font("Sylfaen", FontWeight.MEDIUM, 18);
    public static final double WINDOW_WIDTH = 1200;//1200
    public static final double WINDOW_HEIGHT = 700;//700
    public static final double MIN_WINDOW_WIDTH = 1000;
    public static final double MIN_WINDOW_HEIGHT = 600;    
    
    public static final String SETTINGS_FILE = "settings/config.json";
    
    //timers
    private static AnimationTimer gameTimer, menuTimer;
    
    //Nodes on scene -----------------------------
    private static Stage stage;
    private static Scene gameScene, menuScene;
    private static MainMenu menu;
    private static Group gameGroup;
    private static MenuGroup menuGroup;
    private static Base currentMenu;
    public static Camera camera;
    private static Background gameBackground, menuBackground;
    private static Player player;
    private static List<Enemy> enemies = new LinkedList<>();
    private static List<Enemy> shotEnemies = new ArrayList<>();
    private static List<Projectile> projs = new ArrayList<>();
    private static List<Coin> coins = new ArrayList<>();    
    private static List<Sprite> delObjects = new ArrayList<>();

    private static boolean theEnd = false, goodbye = false;
    private static double time = 0;    
    private static int time_passed = 0;
    private static Text time_text;

    private static Text msg_text;    
  
    private static boolean rst = false; //random shoot time
    private static boolean shoot = false; //commander order shoot
    private static boolean attack = false; //enemy to the front line
    
    public static double width;
    public static double height;
    
    public static Constants constants;
    
    public static Gson gson;
    
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;        
        if (!fileInitialization())
            return;       
        
        width = constants.getWidth();
        height = constants.getHeight();

        menuBackground = new Background();
        menu = new MainMenu(width*2/5, height/4);
        currentMenu = menu;
        menuGroup = new MenuGroup(menuBackground, menu);
        menuScene = new Scene(menuGroup, width, height); 
        menuScene.addEventHandler(KeyEvent.KEY_RELEASED, menuGroup);
        menuScene.addEventHandler(KeyEvent.KEY_RELEASED, menu);
        menuScene.widthProperty().addListener(w -> {
            resizeMenuWindow(menuScene.getWidth()/width, menuScene.getHeight()/height); 
            width = menuScene.getWidth();
            height = menuScene.getHeight();}
        );
        menuScene.heightProperty().addListener(h -> {
            resizeMenuWindow(menuScene.getWidth()/width,menuScene.getHeight()/height); 
            height = menuScene.getHeight();
            width = menuScene.getWidth();}
        );
      
        primaryStage.setTitle(constants.getName());
        primaryStage.setResizable(constants.isResizable());
        primaryStage.setMinWidth(MIN_WINDOW_WIDTH);
        primaryStage.setMinHeight(MIN_WINDOW_HEIGHT);
        primaryStage.setFullScreen(constants.isFull_screen());
        primaryStage.setScene(menuScene);
        primaryStage.show();
        
        menuTimer = new AnimationTimer(){
            @Override
            public void handle(long now) {
                menuBackground.update();
            }
        };
        menuTimer.start();
        
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {                
                updateGame();
                if (time_passed < (int)time){
                    time_passed++;
                    time_text.setText(String.format(constants.getLabels().getTime(), time_passed));
                    double rand = Math.random();
                    if (!theEnd && (rand < constants.getEnemy_fire()*constants.getDifficulty())){
                        if (rand < constants.getEnemy_fire()/5*constants.getDifficulty()){
                            shoot = true;
                        }else
                            if ((!attack) && (rand < constants.getEnemy_fire()*2/5*constants.getDifficulty()))
                                attack = true;                                
                            else
                                rst = true;
                    }
                }
            }
        };        
    }
    
    public static void setCurrentMenu(Base base) {
        currentMenu = base;
    }
    
    private void resizeMenuWindow(double ratioWidth, double ratioHeight) {
        menuBackground.resizeWindow(ratioWidth, ratioHeight);
        currentMenu.resizeWindow(ratioWidth, ratioHeight);
        if (currentMenu != menu)
            menu.resizeWindow(ratioWidth, ratioHeight);
    }
    
    public boolean fileInitialization(){
        try(InputStream in = getClass().getClassLoader().getResourceAsStream(SETTINGS_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));){            
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
            gsonBuilder.registerTypeAdapter(Commands.class, new CommandsDeserializer());
            gsonBuilder.registerTypeAdapter(Labels.class, new LabelsDeserializer());
            gsonBuilder.registerTypeAdapter(Score.class, new ScoreDeserializer());
            gsonBuilder.registerTypeAdapter(Configuration.class, new ConfigurationDeserializer());
            gson = gsonBuilder.create();            
            constants = new Gson().fromJson(br, Constants.class);            
            if (constants.getCommands() != null && constants.getCommands().getPlayer1() != null &&
                    constants.getLabels() != null && constants.getHigh_scores() != null && constants.getConfigurations() != null)
            {
                //System.out.println(constants.getConfigurations()[0].getName());
                return true;
            }
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
    
    public static void createGame(Configuration config, String name){
        gameGroup = new Group();
        camera = new Camera();
        gameBackground = new Background();
        gameGroup.getChildren().add(gameBackground);
        
        player = new Player(name);
        player.setTranslateX(width / 2);
        player.setTranslateY(height * 0.95);
        camera.getChildren().add(player);
 
        displayPoints();
        displayTime();
        
        Enemy.setMovement((width - config.getWidth() * width)/2);
        List<Commander> commanders = new ArrayList<>();
        makeEnemies(config.getCommanders(), "C", config, commanders);
        makeEnemies(config.getWarriors(), "W", config, commanders);
        makeEnemies(config.getScouts(), "S", config, commanders);

        gameGroup.getChildren().add(camera);
        gameScene = new Scene(gameGroup, width, height);

        gameScene.widthProperty().addListener(w -> {
            resizeGameWindow(gameScene.getWidth()/width, gameScene.getHeight()/height); 
            width = gameScene.getWidth();
            height = gameScene.getHeight();}
        );
        gameScene.heightProperty().addListener(h -> {
            resizeGameWindow(gameScene.getWidth()/width,gameScene.getHeight()/height); 
            height = gameScene.getHeight();
            width = gameScene.getWidth();}
        );
        
        gameScene.setOnKeyPressed(player);
        gameScene.setOnKeyReleased(player);       
    }
    
    public static void resetGame(){
        time_passed = 0; time = 0;
        theEnd = false; goodbye = false;
        shoot = false; attack = false; rst = false;
        enemies = new LinkedList<>();
        shotEnemies = new ArrayList<>();
        projs = new ArrayList<>();
        coins = new ArrayList<>();
        delObjects = new ArrayList<>();
        Enemy.resetEnemyGame();
        Player.resetPlayerGame();
    }
    
    public static void makeEnemies(Position[] positions, String type, Configuration config, List<Commander> commanders){
        int enColumns = config.getColumns();
        int enRows = config.getRows();
        double enWidth = config.getWidth() * width / enColumns;
        double enHeight = config.getHeight() * height / enRows;
        for(Position p: positions){
            Enemy enemy;
            switch(type){
                case "C":
                    enemy = new Commander((width - config.getWidth() * width)/2 + (p.getX() - 0.5) * enWidth,p.getY() * enHeight, config.getHeight()*height);
                    commanders.add((Commander)enemy);
                    break;
                case "W":
                    enemy = new Warrior((width - config.getWidth() * width)/2 + (p.getX() - 0.5) * enWidth,p.getY() * enHeight, config.getHeight()*height);
                    int[] comms = Arrays.asList(p.getCommanders().split(",")).stream().mapToInt(Integer::parseInt).toArray();
                    for(int i=0; i < comms.length; i++)
                        ((Warrior)enemy).addCommander(commanders.get(comms[i]));
                    break;
                default:
                    enemy = new Scout((width - config.getWidth() * width)/2 + (p.getX() - 0.5) * enWidth,p.getY() * enHeight, config.getHeight()*height);
                    break;
            }
            enemy.showBar(camera);
            camera.getChildren().add(enemy);
            enemies.add(enemy);
            if (p.isLast())
                enemy.markLast();
        }
    }
    
    public static void startMenuItem(Base base){
        menuGroup.getChildren().remove(menu);
        menuGroup.getChildren().add(base);
        currentMenu = base;
    }
    
    public static void startGame(){
        stage.setScene(gameScene);
        gameTimer.start();
        menuTimer.stop();
    }
    
    public static void startMenu(){
        menuGroup.getChildren().clear();
        menuGroup.getChildren().addAll(menuBackground, menu);
        stage.setScene(menuScene);        
        gameTimer.stop();
        menuTimer.start();
    }
    
    public static void startGameTimer(){
        gameTimer.start();
    }
    
    public static void endAttack(){
        attack = false;
    }
        
    public void updateGame() {
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
                    if (!enemies.isEmpty())
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
            List<Shot> shots = player.getShots();
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
            gameBackground.update();
            time += 1.0 / 60;             
        }else{
            if (!goodbye){
                player.addPoints(-(int)time/constants.getDifficulty());
                camera.getChildren().clear();
                camera.getChildren().addAll(player.getShots());
                camera.getChildren().addAll(enemies);
                camera.getChildren().addAll(coins);
                camera.getChildren().addAll(projs);
                goodbye = true;
                List<Score> scores = new ArrayList<>(Arrays.asList(constants.getHigh_scores()));
                scores.add(new Score(player.getName(), player.getPoints(), time_passed));
                scores.sort((Score o1, Score o2) -> {
                    if (o1.getPoints()==o2.getPoints())
                        return 0;
                    else
                        if (o1.getPoints() > o2.getPoints())
                            return -1;
                        else
                            return 1;
                });
                Score[] write = new Score [scores.size()<10?scores.size():10];
                for(int i=0; i < scores.size(); i++){
                    if (i < 10){
                        write[i] = scores.get(i);
                    }
                }
                constants.setHigh_scores(write);
            }
        }
    }
    
    public static void resizeGameWindow(double ratioWidth, double ratioHeight){
        gameBackground.resizeWindow(ratioWidth, ratioHeight);
        player.resizeWindow(ratioWidth, ratioHeight);
        enemies.forEach(e -> e.resizeWindow(ratioWidth, ratioHeight));
        Enemy.resizeMovement(ratioWidth);
        shotEnemies.forEach(e -> e.resizeWindow(ratioWidth, ratioHeight));
        coins.forEach(c -> c.resizeWindow(ratioWidth, ratioHeight));
        projs.forEach(p -> p.resizeWindow(ratioWidth, ratioHeight));
        
        Scale scale = new Scale();
        scale.setX(ratioWidth);
        scale.setY(ratioHeight);
        time_text.getTransforms().add(scale);
        time_text.setTranslateX(time_text.getTranslateX()*ratioWidth);
        time_text.setTranslateY(time_text.getTranslateY()*ratioHeight);
        if (msg_text != null){
            msg_text.getTransforms().add(scale);
            msg_text.setTranslateX(msg_text.getTranslateX()*ratioWidth);
            msg_text.setTranslateY(msg_text.getTranslateY()*ratioHeight);
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
                Main.setMessageText(String.format(constants.getLabels().getLife(), player.getLifeNumber()), true, null);
                resetPlayer();
            }else{
                theEnd = true; 
                setMessageText(constants.getLabels().getDefeat(), true, 
                        h -> {
                            msg_text.setText(String.format(constants.getLabels().getFinal_score(), player.getPoints()));
                            msg_text.setScaleX(1);
                        });
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
        if (enemy instanceof Warrior)
            ((Warrior)enemy).notifyCommanders();
        else
            if (enemy instanceof Commander)
                ((Commander)enemy).notifyWarriors();
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
                            player.addPoints(enemy.enemyStrength()/constants.getDifficulty());// points won from kill shot
                            if (rand < 0.6){
                                if (rand < 0.25)
                                    player.addBonus(new Bonus(Bonus.pickBonus(), x, y));
                                else
                                    coins.add(new Coin(x, y));                                          
                            }
                        },
                        new KeyValue(rot.angleProperty(), 360))
        );
        if (enemies.isEmpty() && shotEnemies.indexOf(enemy) == shotEnemies.size() - 1){
            tl.setOnFinished(t -> {
                theEnd = true;
                setMessageText(constants.getLabels().getVictory(), true, 
                        h -> {
                            msg_text.setText(String.format(constants.getLabels().getFinal_score(), player.getPoints()));
                            msg_text.setScaleX(1);
                        });
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
        
    public static void setMessageText(String msg, boolean fade, EventHandler<ActionEvent> handler){
        if (msg_text == null){
            msg_text = new Text(width/2 - 100, height/2 - 30, msg);
            msg_text.setFill(Color.ORANGERED);
            msg_text.setFont(FONT);
            gameGroup.getChildren().add(msg_text);
        }
        else
            msg_text.setText(msg); 
        msg_text.setOpacity(1);
        if (fade){
            ScaleTransition st = new ScaleTransition(Duration.seconds(2), msg_text);
            st.setFromX(1);
            st.setToX(0);
            st.setOnFinished(handler);
            st.play();            
        }
    }
    
    private static void displayTime() {        
        time_text = new Text(width/2 - 15, 20, String.format(constants.getLabels().getTime(), time_passed));
        time_text.setFill(Color.RED);
        time_text.setFont(FONT_S);
        time_text.minWidth(width);
        gameGroup.getChildren().add(time_text); 
    }
    
    
    public static void displayPoints(){
        gameBackground.getChildren().add(player.getPointsText());
    }
        
    public static void displayLife(Life life){
        gameBackground.getChildren().add(life);
    }
    
    public static void removeLife(Life life){
        gameBackground.getChildren().remove(life);
    }

    
    public static void main(String[] args) {
        launch(args);
    }
 
}
