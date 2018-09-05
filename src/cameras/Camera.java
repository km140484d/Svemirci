package cameras;

import javafx.scene.Group;
import javafx.scene.transform.*;
import sprites.*;
import main.Main;

public class Camera extends Group {
    
    private boolean playerBound = false;
    
    public Camera() {
        getTransforms().clear();
    }
    
    public void updatePosition(Player p){
        getTransforms().clear();
        getTransforms().add(new Translate(Main.width / 2 - p.getTranslateX(), 0));
    }
    
    public void setPlayerBound(Player p){
        playerBound = true;
        updatePosition(p);
    }   
    
    public void setDefault(){
        playerBound = false;
        getTransforms().clear();
    }
    
    public void updateCamera(Player p){
        if (playerBound)
            updatePosition(p);
    }
}
