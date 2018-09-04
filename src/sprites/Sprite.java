package sprites;

import javafx.scene.Group;
import javafx.scene.transform.Scale;
import main.*;

public abstract class Sprite extends Group {
    
    public Sprite(){
        Scale scale = new Scale();
        scale.setX(Main.width/Main.WINDOW_WIDTH);
        scale.setY(Main.height/Main.WINDOW_HEIGHT);
        getTransforms().add(scale);
    }
    
    public abstract void update();

    public void resizeWindow(double ratioWidth, double ratioHeight){
        Scale scale = new Scale();
        scale.setX(ratioWidth);
        scale.setY(ratioHeight);
        getTransforms().add(scale);
        setTranslateX(getTranslateX()*ratioWidth);
        setTranslateY(getTranslateY()*ratioHeight);
    };
    
}
