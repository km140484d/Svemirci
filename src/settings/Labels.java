package settings;

import java.io.*;

public class Labels implements Serializable{
 
    public class CommandLabels{
        private String exit, pause, main_menu, camera_scene, camera_player1, camera_player2;
        private String player_up, player_down, player_left, player_right, 
                player_rotate_left, player_rotate_right, player_shoot;

        public CommandLabels(String exit, String pause, String main_menu, String camera_scene, String camera_player1, String camera_player2,
                String player_up, String player_down, String player_left, String player_right, String player_rotate_left,
                String player_rotate_right, String player_shoot) {
            this.exit = exit;
            this.pause = pause;
            this.main_menu = main_menu;
            this.camera_scene = camera_scene;
            this.camera_player1 = camera_player1;
            this.camera_player2 = camera_player2;
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

        public String getCamera_player1() {
            return camera_player1;
        }

        public void setCamera_player1(String camera_player1) {
            this.camera_player1 = camera_player1;
        }

        public String getCamera_player2() {
            return camera_player2;
        }

        public void setCamera_player2(String camera_player2) {
            this.camera_player2 = camera_player2;
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
        private String ret_menu, level_mode, resume, main_menu, start, commands, top_10, info, help, exit;
        private String high_scores, player1, player2;

        public MenuLabels(String ret_menu, String level_mode, String resume, String main_menu, String start, String commands, 
                String top_10, String info, String help, String exit, String high_scores, String player1, String player2) {
            this.ret_menu = ret_menu;
            this.level_mode = level_mode;
            this.resume = resume;
            this.main_menu = main_menu;
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

        public String getRet_menu() {
            return ret_menu;
        }

        public void setRet_menu(String ret_menu) {
            this.ret_menu = ret_menu;
        }

        public String getLevel_mode() {
            return level_mode;
        }

        public void setLevel_mode(String level_mode) {
            this.level_mode = level_mode;
        }

        public String getResume() {
            return resume;
        }

        public void setResume(String resume) {
            this.resume = resume;
        }

        public String getMain_menu() {
            return main_menu;
        }

        public void setMain_menu(String main_menu) {
            this.main_menu = main_menu;
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
    
    private String start, final_score1, final_score2, victory, defeat, player_lost, life;
    private String time, points, player, finished, score, pause;//start, high_score
    private CommandLabels commands;
    private MenuLabels menu;
    private InfoLabels info;

    public Labels(String start, String final_score1, String final_score2, String victory, String defeat, 
            String player_lost, String life, String time, String points,
            String player, String finished, String score, String pause) {
        this.start = start;
        this.final_score1 = final_score1;
        this.final_score2 = final_score2;
        this.victory = victory;
        this.defeat = defeat;
        this.player_lost = player_lost;
        this.life = life;
        this.time = time;
        this.points = points;
        this.player = player;
        this.finished = finished;
        this.points = points;
        this.pause = pause;
    } 

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinal_score1() {
        return final_score1;
    }

    public void setFinal_score1(String final_score1) {
        this.final_score1 = final_score1;
    }

    public String getFinal_score2() {
        return final_score2;
    }

    public void setFinal_score2(String final_score2) {
        this.final_score2 = final_score2;
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

    public String getPlayer_lost() {
        return player_lost;
    }

    public void setPlayer_lost(String player_lost) {
        this.player_lost = player_lost;
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

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
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
