package sprites;

import javafx.scene.transform.Scale;
import main.*;

public abstract class Sprite extends Base {
    
    public Sprite(){
        Scale scale = new Scale();
        scale.setX(Main.width/Main.WINDOW_WIDTH);
        scale.setY(Main.height/Main.WINDOW_HEIGHT);
        getTransforms().add(scale);
    }
    
    public abstract void update();

}
