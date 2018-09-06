package settings;

public class Constants {
    
    //basic parameters of the game
    private String name;    
    private boolean full_screen, resizable;
    private double width, height;
    
    //commands
    private Commands commands;
    
    //labels
    private Labels labels;
    
    private int difficulty, power_time;
    private double init_speed, rotate_angle, shot_max_angle;
    private int player1_max_shots, player2_max_shots;
    private double enemy_fire;
    private int scout_life, warrior_life, commander_life;
    private Score[] high_scores;

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFull_screen() {
        return full_screen;
    }

    public void setFull_screen(boolean full_screen) {
        this.full_screen = full_screen;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Commands getCommands() {
        return commands;
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getPower_time() {
        return power_time;
    }

    public void setPower_time(int power_time) {
        this.power_time = power_time;
    }

    public double getInit_speed() {
        return init_speed;
    }

    public void setInit_speed(double init_speed) {
        this.init_speed = init_speed;
    }

    public double getRotate_angle() {
        return rotate_angle;
    }

    public void setRotate_angle(double rotate_angle) {
        this.rotate_angle = rotate_angle;
    }

    public double getShot_max_angle() {
        return shot_max_angle;
    }

    public void setShot_max_angle(double shot_max_angle) {
        this.shot_max_angle = shot_max_angle;
    }

    public int getPlayer1_max_shots() {
        return player1_max_shots;
    }

    public void setPlayer1_max_shots(int player1_max_shots) {
        this.player1_max_shots = player1_max_shots;
    }

    public int getPlayer2_max_shots() {
        return player2_max_shots;
    }

    public void setPlayer2_max_shots(int player2_max_shots) {
        this.player2_max_shots = player2_max_shots;
    }

    public double getEnemy_fire() {
        return enemy_fire;
    }

    public void setEnemy_fire(double enemy_fire) {
        this.enemy_fire = enemy_fire;
    }

    public int getScout_life() {
        return scout_life;
    }

    public void setScout_life(int scout_life) {
        this.scout_life = scout_life;
    }

    public int getWarrior_life() {
        return warrior_life;
    }

    public void setWarrior_life(int warrior_life) {
        this.warrior_life = warrior_life;
    }

    public int getCommander_life() {
        return commander_life;
    }

    public void setCommander_life(int commander_life) {
        this.commander_life = commander_life;
    }

    public Score[] getHigh_scores() {
        return high_scores;
    }

    public void setHigh_scores(Score[] high_scores) {
        this.high_scores = high_scores;
    }
    
    
}