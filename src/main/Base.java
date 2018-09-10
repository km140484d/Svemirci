package main;

import javafx.scene.*;
import javafx.scene.transform.*;

public class Base extends Group{
    
    public Base(){
        
    }
    
    public void resizeWindow(double ratioWidth, double ratioHeight){
        Scale scale = new Scale();
        scale.setX(ratioWidth);
        scale.setY(ratioHeight);
        getTransforms().add(scale);
        setTranslateX(getTranslateX()*ratioWidth);
        setTranslateY(getTranslateY()*ratioHeight);
    };
    
}
