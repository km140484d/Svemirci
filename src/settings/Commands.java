package settings;

import java.io.*;
import javafx.scene.input.*;

public class Commands implements Serializable {
    private KeyCode exit, pause;
    private KeyCode main_menu;
    private KeyCode camera_scene, camera_player;
    private KeyCode player1_up, player1_down, player1_left, player1_right,
            player1_rotate_left, player1_rotate_right, player1_shoot;
    private KeyCode player2_up, player2_down, player2_left, player2_right,
            player2_rotate_left, player2_rotate_right, player2_shoot;

    public Commands(KeyCode exit, KeyCode pause, KeyCode main_menu, KeyCode camera_scene, KeyCode camera_player, 
            KeyCode player1_up, KeyCode player1_down, KeyCode player1_left, KeyCode player1_right, 
            KeyCode player1_rotate_left, KeyCode player1_rotate_right, KeyCode player1_shoot, 
            KeyCode player2_up, KeyCode player2_down, KeyCode player2_left, KeyCode player2_right, 
            KeyCode player2_rotate_left, KeyCode player2_rotate_right, KeyCode player2_shoot) {
        this.exit = exit;
        this.pause = pause;
        this.main_menu = main_menu;
        this.camera_scene = camera_scene;
        this.camera_player = camera_player;
        this.player1_up = player1_up;
        this.player1_down = player1_down;
        this.player1_left = player1_left;
        this.player1_right = player1_right;
        this.player1_rotate_left = player1_rotate_left;
        this.player1_rotate_right = player1_rotate_right;
        this.player1_shoot = player1_shoot;
        this.player2_up = player2_up;
        this.player2_down = player2_down;
        this.player2_left = player2_left;
        this.player2_right = player2_right;
        this.player2_rotate_left = player2_rotate_left;
        this.player2_rotate_right = player2_rotate_right;
        this.player2_shoot = player2_shoot;
    }
    
    

    public KeyCode getExit() {
        return exit;
    }

    public void setExit(KeyCode exit) {
        this.exit = exit;
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

    public KeyCode getCamera_player() {
        return camera_player;
    }

    public void setCamera_player(KeyCode camera_player) {
        this.camera_player = camera_player;
    }

    public KeyCode getPlayer1_up() {
        return player1_up;
    }

    public void setPlayer1_up(KeyCode player1_up) {
        this.player1_up = player1_up;
    }

    public KeyCode getPlayer1_down() {
        return player1_down;
    }

    public void setPlayer1_down(KeyCode player1_down) {
        this.player1_down = player1_down;
    }

    public KeyCode getPlayer1_left() {
        return player1_left;
    }

    public void setPlayer1_left(KeyCode player1_left) {
        this.player1_left = player1_left;
    }

    public KeyCode getPlayer1_right() {
        return player1_right;
    }

    public void setPlayer1_right(KeyCode player1_right) {
        this.player1_right = player1_right;
    }

    public KeyCode getPlayer1_rotate_left() {
        return player1_rotate_left;
    }

    public void setPlayer1_rotate_left(KeyCode player1_rotate_left) {
        this.player1_rotate_left = player1_rotate_left;
    }

    public KeyCode getPlayer1_rotate_right() {
        return player1_rotate_right;
    }

    public void setPlayer1_rotate_right(KeyCode player1_rotate_right) {
        this.player1_rotate_right = player1_rotate_right;
    }

    public KeyCode getPlayer1_shoot() {
        return player1_shoot;
    }

    public void setPlayer1_shoot(KeyCode player1_shoot) {
        this.player1_shoot = player1_shoot;
    }

    public KeyCode getPlayer2_up() {
        return player2_up;
    }

    public void setPlayer2_up(KeyCode player2_up) {
        this.player2_up = player2_up;
    }

    public KeyCode getPlayer2_down() {
        return player2_down;
    }

    public void setPlayer2_down(KeyCode player2_down) {
        this.player2_down = player2_down;
    }

    public KeyCode getPlayer2_left() {
        return player2_left;
    }

    public void setPlayer2_left(KeyCode player2_left) {
        this.player2_left = player2_left;
    }

    public KeyCode getPlayer2_right() {
        return player2_right;
    }

    public void setPlayer2_right(KeyCode player2_right) {
        this.player2_right = player2_right;
    }

    public KeyCode getPlayer2_rotate_left() {
        return player2_rotate_left;
    }

    public void setPlayer2_rotate_left(KeyCode player2_rotate_left) {
        this.player2_rotate_left = player2_rotate_left;
    }

    public KeyCode getPlayer2_rotate_right() {
        return player2_rotate_right;
    }

    public void setPlayer2_rotate_right(KeyCode player2_rotate_right) {
        this.player2_rotate_right = player2_rotate_right;
    }

    public KeyCode getPlayer2_shoot() {
        return player2_shoot;
    }

    public void setPlayer2_shoot(KeyCode player2_shoot) {
        this.player2_shoot = player2_shoot;
    }
    
    

}