package settings;

import java.io.Serializable;
import javafx.scene.input.KeyCode;

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
