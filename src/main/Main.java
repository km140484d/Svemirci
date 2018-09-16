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
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.transform.*;
import javafx.stage.*;
import javafx.util.Duration;
import menu.*;
import settings.deserializers.*;
import sprites.awards.*;

public class Main extends Application {  
    public static final double WINDOW_WIDTH = 1200;//1200
    public static final double WINDOW_HEIGHT = 700;//700
    public static final Font FONT_S = Font.font("Sylfaen", FontWeight.BOLD, 18);
    private static final Font FONT_L = Font.font("Sylfaen", FontWeight.EXTRA_BOLD, 36);
    private static final double MIN_WINDOW_WIDTH = 1000;
    private static final double MIN_WINDOW_HEIGHT = 600;    
    private static final String SETTINGS_FILE = "settings/config.json";
    
    //timers
    private static AnimationTimer gameTimer, menuTimer;
    private static boolean gameRunning = false;
    
    //Nodes on scene -----------------------------
    private static Scene scene;
    private static MainMenu menu;
    private static Group gameGroup, menuGroup;
    private static Base currentMenu;
    private static Camera camera;
    private static Background gameBackground, menuBackground;
    private static List<Player> players, shotPlayers;
    private static List<EventHandler> playerHandlers;
    private static List<Bonus> bonuses = new ArrayList<>(); //static
    private static List<Enemy> enemies = new LinkedList<>();
    private static List<Enemy> shotEnemies = new ArrayList<>();
    private static List<Projectile> projs = new ArrayList<>();
    private static List<Coin> coins = new ArrayList<>();    
    private static List<Sprite> delObjects = new ArrayList<>();

    private static boolean theEnd = false, goodbye = false;
    private static double time = 0;    
    private static int time_passed = 0;
    private static Text time_text, msg_text;    
    private static VBox msgBox, timeBox;   
  
    private static boolean rst = false; //random shoot time
    private static boolean shoot = false; //commander order shoot
    private static boolean attack = false; //enemy to the front line
    
    private static double width;
    private static double height;
    
    public static Constants constants;    
    private static Gson gson;    
    private static String sceneType = "M";    
    private static EventHandler<KeyEvent> basicGameHandler, basicMenuHandler;
    
    //style
    private static final String mouseEntered = "-fx-background-color:crimson; -fx-font-weight:bold;"
                + "-fx-border-color:white; -fx-text-fill:white; -fx-text-alignment:CENTER;",
            mouseExited = "-fx-background-color:transparent; -fx-font-weight:bold;"
                + "-fx-border-color:white; -fx-text-fill:white; -fx-text-alignment:CENTER;";
    
    //pause
    private static Group pauseGroup;
    private static VBox pauseBox;
    
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
    
    @Override
    public void start(Stage primaryStage) {    
        if (!fileInitialization())
            return;      
        width = constants.getWidth();
        height = constants.getHeight();
        basicGameHandler = (KeyEvent event) -> {
            if (event.getEventType() == KeyEvent.KEY_RELEASED){
                KeyCode code = event.getCode();
                if (code == constants.getCommands().getFull_screen()){
                    primaryStage.setFullScreen(true);                        
                }else{
                    if (code == constants.getCommands().getPause()){
                        if (!theEnd){
                            if (gameRunning){
                                gameTimer.stop();
                                if (pauseGroup == null)
                                    pauseGroup = makePauseMenu();
                                camera.getChildren().add(pauseGroup);                         
                            }else{
                                camera.getChildren().remove(pauseGroup);
                                gameTimer.start();   
                            }
                            gameRunning = !gameRunning;
                        }
                    }else{
                        if (code == constants.getCommands().getCamera_scene())
                            Main.camera.setDefault();
                        else{
                            if (code == constants.getCommands().getCamera_player1()){
                                Player player = Main.getPlayer(Player.Type.PLAYER1);
                                if (player != null)
                                    Main.camera.setPlayerBound(player);
                            }else{
                                if (code == constants.getCommands().getCamera_player2()){
                                    Player player = Main.getPlayer(Player.Type.PLAYER2);
                                    if (player != null)
                                        Main.camera.setPlayerBound(player); 
                                }else
                                    if (code == constants.getCommands().getMain_menu())
                                        Main.startMenu(null);
                            } 
                        }
                    }
                }
            }
        };
        
        basicMenuHandler = (KeyEvent event) -> {
            KeyCode code = event.getCode();
            Commands commands = Main.constants.getCommands();
            if (code == commands.getMain_menu()){
                menuGroup.getChildren().remove(currentMenu);
                menuGroup.getChildren().add(menu);
                currentMenu = menu;
            }else{
                if (code == commands.getFull_screen())
                    primaryStage.setFullScreen(true);
            }
        };

        menuBackground = new Background();
        menu = new MainMenu(width*2/5, height/4);
        currentMenu = menu;
        menuGroup = new Group(menuBackground, menu);
        scene = new Scene(menuGroup, width, height); 
        scene.addEventHandler(KeyEvent.KEY_RELEASED, basicMenuHandler);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, menu);
        scene.widthProperty().addListener(w -> {
            if (sceneType.equals("M"))
                resizeMenuWindow(scene.getWidth()/width, scene.getHeight()/height);
            else
                resizeGameWindow(scene.getWidth()/width, scene.getHeight()/height);
            width = scene.getWidth();
            height = scene.getHeight();}
        );
        scene.heightProperty().addListener(h -> {
            if (sceneType.equals("M"))
                resizeMenuWindow(scene.getWidth()/width, scene.getHeight()/height);
            else
                resizeGameWindow(scene.getWidth()/width, scene.getHeight()/height);
            height = scene.getHeight();
            width = scene.getWidth();}
        );
      
        primaryStage.setTitle(constants.getName());
        primaryStage.setResizable(constants.isResizable());
        primaryStage.setMinWidth(MIN_WINDOW_WIDTH);
        primaryStage.setMinHeight(MIN_WINDOW_HEIGHT);
        primaryStage.setFullScreen(constants.isFull_screen());
        primaryStage.setScene(scene);
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
    
    public static void startMenuItem(Base base){
        //bottom label for returning to main menu
        Text retMenu = new Text(String.format(constants.getLabels().getMenu().getRet_menu(), 
                constants.getCommands().getMain_menu()));
        retMenu.setFill(Color.LAVENDER);
        retMenu.setFont(Font.font("Ariel", FontPosture.ITALIC, 18));
        retMenu.setTextAlignment(TextAlignment.CENTER);
        Button button = new Button(constants.getLabels().getMenu().getMain_menu());
        button.setStyle(mouseExited);
        button.setOnMouseClicked(c -> {
            menuGroup.getChildren().remove(currentMenu);
            menuGroup.getChildren().add(menu);
            currentMenu = menu;
        });        
        button.setOnMouseEntered(e -> button.setStyle(mouseEntered));  
        button.setOnMouseExited(e -> button.setStyle(mouseExited));
        HBox hBox = new HBox(retMenu, button);
        hBox.setMinWidth(constants.getWidth());
        hBox.setAlignment(Pos.CENTER);
        
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setMinWidth(constants.getWidth());
        vBox.setMinHeight(constants.getHeight()/9);
        vBox.setTranslateY(constants.getHeight()*8/9);
        //if player reached top 10
        if (base instanceof Announcement){
            Text levelMode = new Text(String.format(constants.getLabels().getMenu().getLevel_mode(), 
                KeyCode.HOME, KeyCode.TAB, KeyCode.ENTER));
            levelMode.setFill(Color.LAVENDER);
            levelMode.setFont(Font.font("Ariel", FontPosture.ITALIC, 18));
            levelMode.setTextAlignment(TextAlignment.CENTER);
            vBox.getChildren().add(levelMode);
        }
        vBox.getChildren().add(hBox);
        base.getChildren().add(vBox);
        
        menuGroup.getChildren().remove(currentMenu);
        menuGroup.getChildren().add(base);
        currentMenu = base;
        currentMenu.resizeWindow(Main.width/constants.getWidth(), Main.height/constants.getHeight());
    }
    
    public static void startMenu(Base base){  
        sceneType = "M";
        menu = new MainMenu(constants.getWidth()*2/5, constants.getHeight()/4);
        menu.resizeWindow(Main.width/constants.getWidth(), Main.height/constants.getHeight());
        currentMenu = menu;
        menuBackground = new Background();
        menuGroup = new Group(menuBackground, currentMenu);    
        if (base != null)
            startMenuItem(base);        
        scene.setRoot(menuGroup);
        for(EventHandler handler: playerHandlers){
            scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
            scene.removeEventHandler(KeyEvent.KEY_RELEASED, handler);
        }
        scene.removeEventHandler(KeyEvent.KEY_RELEASED, basicGameHandler);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, basicMenuHandler);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, menu); 
        gameTimer.stop();
        menuTimer.start();
    }  
    
    public static boolean containsMenu(){
        return menuGroup.getChildren().contains(menu);
    }
    
    public Group makePauseMenu(){
        Rectangle rect = new Rectangle(width/4, height/4);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.WHITE);
        rect.setTranslateX(width*3/8);
        rect.setStrokeWidth(3);
        rect.setArcWidth(width*3/64);
        rect.setArcHeight(height*3/64);

        Text pauseText = new Text(constants.getLabels().getPause());
        pauseText.setFill(Color.WHITE);
        pauseText.setFont(FONT_L);
        
        Button retButton = new Button(constants.getLabels().getMenu().getMain_menu() + 
                "\n(" + constants.getCommands().getMain_menu() + ")");
        retButton.setMinWidth(width/12);
        retButton.setStyle(mouseExited);
        retButton.setOnMouseEntered(e -> retButton.setStyle(mouseEntered));
        retButton.setOnMouseExited(e -> retButton.setStyle(mouseExited));   
        retButton.setOnMouseClicked(c -> {
            camera.getChildren().remove(pauseGroup);
            startMenu(null);
            gameRunning = false;
        });        
        
        Button resumeButton = new Button(constants.getLabels().getMenu().getResume() + 
                "\n(" + constants.getCommands().getPause() + ")");
        resumeButton.setMinWidth(width/12);
        resumeButton.setStyle(mouseExited);
        resumeButton.setOnMouseEntered(e -> resumeButton.setStyle(mouseEntered));
        resumeButton.setOnMouseExited(e -> resumeButton.setStyle(mouseExited));
        resumeButton.setOnMouseClicked(c -> {
            camera.getChildren().remove(pauseGroup);
            gameTimer.start();
            gameRunning = true;
        });
        
        HBox hbox = new HBox(width/20, retButton, resumeButton);
        hbox.setAlignment(Pos.CENTER);
        pauseBox = new VBox(height/20, pauseText, hbox);
        pauseBox.setAlignment(Pos.CENTER);
        pauseBox.setMinWidth(width);
        pauseBox.setTranslateY(height/16);
        
        Group group = new Group(rect, pauseBox);
        group.setTranslateY(height*3/8);
        return group;
    }
    
    public static void startGame(){
        sceneType = "G";
        scene.removeEventHandler(KeyEvent.KEY_RELEASED, basicMenuHandler);
        scene.removeEventHandler(KeyEvent.KEY_RELEASED, menu);
        currentMenu = menu;
        gameTimer.start();
        gameRunning = true;
        menuTimer.stop();
    }

    private static void resizeMenuWindow(double ratioWidth, double ratioHeight) {
        menuBackground.resizeWindow(ratioWidth, ratioHeight);
        currentMenu.resizeWindow(ratioWidth, ratioHeight);
        if (currentMenu != menu)
            menu.resizeWindow(ratioWidth, ratioHeight);
        if (pauseGroup != null){
            Scale scale = new Scale(ratioWidth, ratioHeight);
            pauseGroup.getTransforms().add(scale);
            pauseGroup.setTranslateY(pauseGroup.getTranslateY()*ratioHeight);
        }
    }
    
    public static void resizeGameWindow(double ratioWidth, double ratioHeight){
        gameBackground.resizeWindow(ratioWidth, ratioHeight);
        players.forEach(p -> p.resizeWindow(ratioWidth, ratioHeight));
        enemies.forEach(e -> e.resizeWindow(ratioWidth, ratioHeight));
        Enemy.resizeMovement(ratioWidth);
        shotEnemies.forEach(e -> e.resizeWindow(ratioWidth, ratioHeight));
        coins.forEach(c -> c.resizeWindow(ratioWidth, ratioHeight));
        projs.forEach(p -> p.resizeWindow(ratioWidth, ratioHeight));
        bonuses.forEach(b -> b.resizeWindow(ratioWidth, ratioHeight));
        
        Scale scale = new Scale(ratioWidth, ratioHeight);
        timeBox.getTransforms().add(scale);
        if (msgBox != null)
            msgBox.getTransforms().add(scale);
        if (pauseGroup != null){
            pauseGroup.getTransforms().add(scale);
            pauseGroup.setTranslateY(pauseGroup.getTranslateY()*ratioHeight);
        }        
    }
    
    public static void resetGame(){
        msg_text = null;
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
        bonuses = new ArrayList<>();
        gameRunning = false;
    }
  
    public static void createGame(Configuration config, String name1, String name2){
        gameGroup = new Group();
        camera = new Camera();
        gameBackground = new Background();
        gameGroup.getChildren().add(gameBackground);
        
        players = new ArrayList<>();
        if (name2 == null){
            players.add(new Player(name1, Player.Type.PLAYER1, width/2, height*0.95));
        }else{
            players.add(new Player(name1, Player.Type.PLAYER1, width/3, height*0.95));
            players.add(new Player(name2, Player.Type.PLAYER2, width*2/3, height*0.95));
        }
        camera.getChildren().addAll(players);
        shotPlayers = new ArrayList<>();

        displayTime();
        
        Enemy.setMovement((width - config.getWidth() * width)/2);
        List<Commander> commanders = new ArrayList<>();
        makeEnemies(config.getCommanders(), "C", config, commanders);
        makeEnemies(config.getWarriors(), "W", config, commanders);
        makeEnemies(config.getScouts(), "S", config, commanders);

        gameGroup.getChildren().add(camera);
        scene.setRoot(gameGroup);

        playerHandlers = new ArrayList<>();
        for(Player player: players){
            playerHandlers.add(player);
            scene.addEventHandler(KeyEvent.KEY_PRESSED, player);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, player);
        } 
        scene.addEventHandler(KeyEvent.KEY_RELEASED, basicGameHandler);
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
        
    public void updateGame() {
        if (!theEnd) {            
            camera.getChildren().clear();             
            //enemy player update
            for(Player player: players){
                for(int i = 0; i < enemies.size(); i++){
                    Enemy enemy = enemies.get(i);
                    if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())){
                        updatePlayer(player);
                        if (theEnd)
                            return;
                        else
                            break;
                    }
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
            camera.getChildren().addAll(players);

            //enemy and shots update
            for(Player player: players){
                List<Shot> shots = player.getShots();
                for(int i=0; i < shots.size(); i++){
                    Shot shot = shots.get(i);                
                    for (int j = 0; j < enemies.size(); j++) {
                        Enemy currentEnemy = enemies.get(j);
                        if (shot.getBoundsInParent().intersects(currentEnemy.getBoundsInParent())) {
                            if (shot instanceof Stream || shot instanceof Boomerang){
                                if (currentEnemy.isRedMark()){
                                    if (currentEnemy.enemyShot(shot.getShotStrength()))
                                        destroyEnemy(currentEnemy, player);
                                    else
                                        currentEnemy.setRedMark(false);
                                }
                            }else{                            
                                if (currentEnemy.enemyShot(shot.getShotStrength()))
                                    destroyEnemy(currentEnemy, player);
                                Main.removeSprite(shot);
                            }                                
                            break;
                        }
                    }
                }
                shots.removeAll(delObjects);
                shots.forEach(e -> e.update());
                camera.getChildren().addAll(shots);
                player.setShots(shots);
            }

            //coins                
            coins.forEach(c -> {
                c.update();
                for(Player player: players){
                    if (c.getBoundsInParent().intersects(player.getBoundsInParent())){
                        player.addPoints(1);                         
                        Main.removeSprite(c);
                    }   
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
                for(int i=0; i<players.size(); i++){
                    if (p.getBoundsInParent().intersects(players.get(i).getBoundsInParent())){
                            updatePlayer(players.get(i));
                            if (theEnd)
                                return;
                            else
                                break;
                    }
                }
            });
            projs.removeAll(delObjects);
            camera.getChildren().addAll(projs);
            
            //bouses
            for(Player player: players){
                for(int i = 0; i < bonuses.size(); i++){
                    Bonus bonus = bonuses.get(i);
                    if ((bonus.getTranslateY() + bonus.getVelocityY()) > Main.height)
                        bonuses.remove(bonus);
                    else{
                        bonus.update();
                        if (bonus.getBoundsInParent().intersects(player.getBoundsInParent())){
                            player.consumed(bonus);
                            bonuses.remove(bonus);
                            break;
                        }
                    }
                }                
            }
            camera.getChildren().addAll(bonuses);

            camera.updateCamera(players.get(0));
            
            players.forEach(p -> p.update());        
            time += 1.0 / 60;             
        }else{
            if (!goodbye){ 
                camera.getChildren().clear();
                camera.getChildren().addAll(enemies);
                goodbye = true;                
               
                List<Score> scores = new ArrayList<>(Arrays.asList(constants.getHigh_scores()));
                List<Player> all = new ArrayList<>();
                all.addAll(players);
                all.addAll(shotPlayers);
                List<Score> playerScores = new ArrayList<>();
                all.forEach(p -> {
                    p.addPoints(-(int)time/constants.getDifficulty());
                    playerScores.add(new Score(p.getPlayerColor(), p.getName(), p.getPoints(), time_passed));
                });
                for(Score score: playerScores){                                        
                    scores.add(score);
                    scores.sort((Score o1, Score o2) -> {
                        if (o1.getPoints()==o2.getPoints())
                            return 0;
                        else
                            if (o1.getPoints() > o2.getPoints())
                                return -1;
                            else
                                return 1;
                    });                    
                }
                Score[] write = new Score [scores.size()<10?scores.size():10];
                    for(int i=0; i < scores.size(); i++){
                        if (i < 10){
                            write[i] = scores.get(i);
                        }
                    }
                constants.setHigh_scores(write);

                for(int i=0; i< playerScores.size(); i++)
                    if (scores.indexOf(playerScores.get(i)) >= 10)
                        playerScores.remove(playerScores.get(i));
                
                if(!playerScores.isEmpty())
                    startMenu(new HighScoresMenu(constants.getWidth(), constants.getHeight()*8/9, playerScores));                
            }
        }
        gameBackground.update();
    }
           
    public void updatePlayer(Player player){
        if (!player.invincible()){
            if (!player.loseLife()){
                Main.setMessageText(String.format(constants.getLabels().getLife(), player.getName(), 
                        player.getLifeNumber()), true, null);
                player.reset();
                Timeline playerAnotherTry = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(player.opacityProperty(), 0, Interpolator.EASE_IN)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(player.opacityProperty(), 1, Interpolator.EASE_IN))
                );
                playerAnotherTry.play();
            }else{
                //player loses
                players.remove(player);
                playerHandlers.remove(player);
                shotPlayers.add(player);
                if (players.isEmpty()){
                    theEnd = true; 
                    setMessageText(constants.getLabels().getDefeat(), true, 
                            h -> {
                                String str;
                                if (shotPlayers.size() == 2){
                                    str = String.format(constants.getLabels().getFinal_score2(), 
                                            shotPlayers.get(0).getName(), shotPlayers.get(0).getPoints(),
                                            shotPlayers.get(1).getName(), shotPlayers.get(1).getPoints());
                                }else{
                                    str = String.format(constants.getLabels().getFinal_score1(), 
                                            shotPlayers.get(0).getName(), shotPlayers.get(0).getPoints());
                                }
                                msg_text.setText(str);
                                ScaleTransition st = new ScaleTransition(Duration.seconds(5), msg_text);
                                st.setFromX(1);
                                st.setToX(0);
                                st.setOnFinished(f -> Main.startMenu(null));
                                st.play();                                  
                            });
                }else{
                    Main.setMessageText(
                            String.format(constants.getLabels().getPlayer_lost(), players.get(0).getName(), shotPlayers.get(0).getName()), true, null);
                }
            }
        }
    }

    public void destroyEnemy(Enemy enemy, Player player){
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
                            player.addPoints(enemy.enemyStrength()*constants.getDifficulty());// points won from kill shot
                            if (rand < 0.6){
                                if (rand < 0.25)
                                    bonuses.add(new Bonus(Bonus.pickBonus(), x, y));
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
                            String str;
                            List<Player> pls = new ArrayList<>();
                            pls.addAll(players);
                            pls.addAll(shotPlayers);
                            if (pls.size() == 2){
                                    str = String.format(constants.getLabels().getFinal_score2(), 
                                            pls.get(0).getName(), pls.get(0).getPoints(),
                                            pls.get(1).getName(), pls.get(1).getPoints());
                            }else{
                                str = String.format(constants.getLabels().getFinal_score1(), 
                                        pls.get(0).getName(), pls.get(0).getPoints());
                            }
                            msg_text.setText(str);
                            ScaleTransition st = new ScaleTransition(Duration.seconds(5), msg_text);
                            st.setFromX(1);
                            st.setToX(0);
                            st.setOnFinished(f -> Main.startMenu(null));
                            st.play();      
                        });
            });
        }        
        tl.play();
    }
    
    public static Player getPlayer(Player.Type type){
        for(Player p: players){
            if (p.getPlayerType() == type)
                return p;
        }  
        return null;
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
            Player player = players.get((int)(Math.random()*players.size()));
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
            Player player = players.get((int)(Math.random()*players.size()));
            commanders.get(randCommander).orderAttack(player.getTranslateX(), player.getTranslateY());
        }
    }
    
    public static void setEnemyRedMark(boolean mark){
        enemies.forEach(e -> e.setRedMark(mark));
    }
    
    public static void endAttack(){
        attack = false;
    }
    
    public static void addProjectile(Projectile proj){
        projs.add(proj);
    }
        
    public static void removeSprite(Sprite sprite){
        delObjects.add(sprite);
    }
    
    public static void setMessageText(String msg, boolean fade, EventHandler<ActionEvent> handler){
        if (msg_text == null){
            msg_text = new Text(msg);
            msgBox = new VBox(msg_text);
            msgBox.setAlignment(Pos.CENTER);
            msgBox.setMinWidth(Main.width);
            msgBox.setMinHeight(Main.height);
            msg_text.setFill(Color.CRIMSON);
            msg_text.setStroke(Color.WHITE);
            msg_text.setFont(FONT_L);
            msg_text.setTextAlignment(TextAlignment.CENTER);
            gameGroup.getChildren().add(msgBox);
        }
        else
            msg_text.setText(msg); 
        msg_text.setOpacity(1);
        if (fade){
            ScaleTransition st = new ScaleTransition(Duration.seconds(3), msg_text);
            st.setFromX(1);
            st.setToX(0);
            st.setOnFinished(handler);
            st.play();            
        }
    }
    
    private static void displayTime() {       
        time_text = new Text(String.format(constants.getLabels().getTime(), time_passed));
        time_text.setFill(Color.CRIMSON);
        time_text.setFont(FONT_S);
        timeBox = new VBox(time_text);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.setMinWidth(Main.width);
        timeBox.setMinHeight(Main.height/30);
        gameGroup.getChildren().add(timeBox); 
    }

    public static Gson getGson() {
        return gson;
    }

    public static Camera getCamera() {
        return camera;
    }

    public static double getWidth() {
        return width;
    }

    public static double getHeight() {
        return height;
    }
   
    public static void main(String[] args) {
        launch(args);
    }
 
}
