package sprites;

import javafx.scene.transform.*;
import main.*;

public abstract class Sprite extends Base {
    
    public Sprite(){
        Scale scale = new Scale(Main.getWidth()/Main.WINDOW_WIDTH, Main.getHeight()/Main.WINDOW_HEIGHT);
        getTransforms().add(scale);
    }
    
    public abstract void update();

}
