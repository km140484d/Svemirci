package settings;

import java.io.*;

public class Labels implements Serializable{
 
    public class CommandLabels{
        private String exit, pause, main_menu, camera_scene, camera_player;
        private String player_up, player_down, player_left, player_right, 
                player_rotate_left, player_rotate_right, player_shoot;

        public CommandLabels(String exit, String pause, String main_menu, String camera_scene, String camera_player, String player_up, String player_down, String player_left, String player_right, String player_rotate_left, String player_rotate_right, String player_shoot) {
            this.exit = exit;
            this.pause = pause;
            this.main_menu = main_menu;
            this.camera_scene = camera_scene;
            this.camera_player = camera_player;
            this.player_up = player_up;
            this.player_down = player_down;
            this.player_left = player_left;
            this.player_right = player_right;
            this.player_rotate_left = player_rotate_left;
            this.player_rotate_right = player_rotate_right;
            this.player_shoot = player_shoot;
        }

        public String getExit() {
            return exit;
        }

        public void setExit(String exit) {
            this.exit = exit;
        }

        public String getPause() {
            return pause;
        }

        public void setPause(String pause) {
            this.pause = pause;
        }

        public String getMain_menu() {
            return main_menu;
        }

        public void setMain_menu(String main_menu) {
            this.main_menu = main_menu;
        }

        public String getCamera_scene() {
            return camera_scene;
        }

        public void setCamera_scene(String camera_scene) {
            this.camera_scene = camera_scene;
        }

        public String getCamera_player() {
            return camera_player;
        }

        public void setCamera_player(String camera_player) {
            this.camera_player = camera_player;
        }

        public String getPlayer_up() {
            return player_up;
        }

        public void setPlayer_up(String player_up) {
            this.player_up = player_up;
        }

        public String getPlayer_down() {
            return player_down;
        }

        public void setPlayer_down(String player_down) {
            this.player_down = player_down;
        }

        public String getPlayer_left() {
            return player_left;
        }

        public void setPlayer_left(String player_left) {
            this.player_left = player_left;
        }

        public String getPlayer_right() {
            return player_right;
        }

        public void setPlayer_right(String player_right) {
            this.player_right = player_right;
        }

        public String getPlayer_rotate_left() {
            return player_rotate_left;
        }

        public void setPlayer_rotate_left(String player_rotate_left) {
            this.player_rotate_left = player_rotate_left;
        }

        public String getPlayer_rotate_right() {
            return player_rotate_right;
        }

        public void setPlayer_rotate_right(String player_rotate_right) {
            this.player_rotate_right = player_rotate_right;
        }

        public String getPlayer_shoot() {
            return player_shoot;
        }

        public void setPlayer_shoot(String player_shoot) {
            this.player_shoot = player_shoot;
        }      
    }
    
    public class MenuLabels{
        private String start, commands, top_10, info, help, exit;
        private String high_scores, player1, player2;

        public MenuLabels(String start, String commands, String top_10, String info, 
                String help, String exit, String high_scores, String player1, String player2) {
            this.start = start;
            this.commands = commands;
            this.top_10 = top_10;
            this.info = info;
            this.help = help;
            this.exit = exit;
            this.high_scores = high_scores;
            this.player1 = player1;
            this.player2 = player2;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getCommands() {
            return commands;
        }

        public void setCommands(String commands) {
            this.commands = commands;
        }

        public String getTop_10() {
            return top_10;
        }

        public void setTop_10(String top_10) {
            this.top_10 = top_10;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getHelp() {
            return help;
        }

        public void setHelp(String help) {
            this.help = help;
        }

        public String getExit() {
            return exit;
        }

        public void setExit(String exit) {
            this.exit = exit;
        }

        public String getHigh_scores() {
            return high_scores;
        }

        public void setHigh_scores(String high_scores) {
            this.high_scores = high_scores;
        }

        public String getPlayer1() {
            return player1;
        }

        public void setPlayer1(String player1) {
            this.player1 = player1;
        }

        public String getPlayer2() {
            return player2;
        }

        public void setPlayer2(String player2) {
            this.player2 = player2;
        }
    }
    
    public class InfoLabels{
        private String name, author, mentor, version, description;

        public InfoLabels(String name, String author, String mentor, String version, String description) {
            this.name = name;
            this.author = author;
            this.mentor = mentor;
            this.version = version;
            this.description = description;
        }
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getMentor() {
            return mentor;
        }

        public void setMentor(String mentor) {
            this.mentor = mentor;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        
        
    }
    
    private String start, final_score, victory, defeat, life;
    private String time, points, high_score;//start, high_score
    private CommandLabels commands;
    private MenuLabels menu;
    private InfoLabels info;

    public Labels(String start, String final_score, String victory, String defeat, String life, 
            String time, String points, String high_score) {
        this.start = start;
        this.final_score = final_score;
        this.victory = victory;
        this.defeat = defeat;
        this.life = life;
        this.time = time;
        this.points = points;
        this.high_score = high_score;
    } 

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinal_score() {
        return final_score;
    }

    public void setFinal_score(String final_score) {
        this.final_score = final_score;
    }

    public String getVictory() {
        return victory;
    }

    public void setVictory(String victory) {
        this.victory = victory;
    }

    public String getDefeat() {
        return defeat;
    }

    public void setDefeat(String defeat) {
        this.defeat = defeat;
    }

    public String getLife() {
        return life;
    }

    public void setLife(String life) {
        this.life = life;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getHigh_score() {
        return high_score;
    }

    public void setHigh_score(String high_score) {
        this.high_score = high_score;
    }

    public CommandLabels getCommands() {
        return commands;
    }

    public void setCommands(CommandLabels commands) {
        this.commands = commands;
    }

    public MenuLabels getMenu() {
        return menu;
    }

    public void setMenu(MenuLabels menu) {
        this.menu = menu;
    }

    public InfoLabels getInfo() {
        return info;
    }

    public void setInfo(InfoLabels info) {
        this.info = info;
    }
  
}
