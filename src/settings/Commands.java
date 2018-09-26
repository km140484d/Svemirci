package settings;

import java.io.*;
import javafx.scene.input.*;

public class Commands implements Serializable {
    
    public class PlayerCommands implements Serializable{
        private KeyCode up, down, left, right,
                rotate_left, rotate_right, shoot;

        public PlayerCommands(KeyCode up, KeyCode down, KeyCode left, KeyCode right, 
                KeyCode rotate_left, KeyCode rotate_right, KeyCode shoot) {
            this.up = up;
            this.down = down;
            this.left = left;
            this.right = right;
            this.rotate_left = rotate_left;
            this.rotate_right = rotate_right;
            this.shoot = shoot;
        }

        public KeyCode getUp() {
            return up;
        }

        public void setUp(KeyCode up) {
            this.up = up;
        }

        public KeyCode getDown() {
            return down;
        }

        public void setDown(KeyCode down) {
            this.down = down;
        }

        public KeyCode getLeft() {
            return left;
        }

        public void setLeft(KeyCode left) {
            this.left = left;
        }

        public KeyCode getRight() {
            return right;
        }

        public void setRight(KeyCode right) {
            this.right = right;
        }

        public KeyCode getRotate_left() {
            return rotate_left;
        }

        public void setRotate_left(KeyCode rotate_left) {
            this.rotate_left = rotate_left;
        }

        public KeyCode getRotate_right() {
            return rotate_right;
        }

        public void setRotate_right(KeyCode rotate_right) {
            this.rotate_right = rotate_right;
        }

        public KeyCode getShoot() {
            return shoot;
        }

        public void setShoot(KeyCode shoot) {
            this.shoot = shoot;
        } 
    }

    private KeyCode exit, full_screen, pause, main_menu, camera_scene, camera_player1, camera_player2;
    private PlayerCommands player1;
    private PlayerCommands player2;
            
    public Commands(KeyCode exit, KeyCode full_screen, KeyCode pause, KeyCode main_menu, 
            KeyCode camera_scene, KeyCode camera_player1, KeyCode camera_player2) {
        this.exit = exit;
        this.full_screen = full_screen;
        this.pause = pause;
        this.main_menu = main_menu;
        this.camera_scene = camera_scene;
        this.camera_player1 = camera_player1;
        this.camera_player2 = camera_player2;
    }
    
    public KeyCode getExit() {
        return exit;
    }

    public void setExit(KeyCode exit) {
        this.exit = exit;
    }

    public KeyCode getFull_screen() {
        return full_screen;
    }

    public void setFull_screen(KeyCode full_screen) {
        this.full_screen = full_screen;
    }
    
    public KeyCode getPause() {
        return pause;
    }

    public void setPause(KeyCode pause) {
        this.pause = pause;
    }

    public KeyCode getMain_menu() {
        return main_menu;
    }

    public void setMain_menu(KeyCode main_menu) {
        this.main_menu = main_menu;
    }

    public KeyCode getCamera_scene() {
        return camera_scene;
    }

    public void setCamera_scene(KeyCode camera_scene) {
        this.camera_scene = camera_scene;
    }

    public KeyCode getCamera_player1() {
        return camera_player1;
    }

    public void setCamera_player1(KeyCode camera_player1) {
        this.camera_player1 = camera_player1;
    }

    public KeyCode getCamera_player2() {
        return camera_player2;
    }

    public void setCamera_player2(KeyCode camera_player2) {
        this.camera_player2 = camera_player2;
    }

    public PlayerCommands getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerCommands player1) {
        this.player1 = player1;
    }

    public PlayerCommands getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerCommands player2) {
        this.player2 = player2;
    }
        
}