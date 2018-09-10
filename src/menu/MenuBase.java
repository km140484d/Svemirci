package menu;

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import sprites.*;

public class MenuBase extends Group implements EventHandler<KeyEvent>{
    
    public static final Font FONT_S = Font.font("Sylfaen", FontWeight.MEDIUM, 20);
    public static final Font FONT_M = Font.font("Sylfaen", FontWeight.EXTRA_BOLD, 24);
    public static final Font FONT_L = Font.font("Sylfaen", FontWeight.SEMI_BOLD, 40);
    
    public static enum MenuState{MAIN, ANNOUNCEMENT, COMMANDS, TOP, INFO, HELP};
    private static MenuState state = MenuState.MAIN;    
    private MainMenu menu;
    
    private Background background;

    public MenuBase(Background background, MainMenu menu){
        this.background = background;
        this.menu = menu;
        getChildren().addAll(this.background, this.menu);
    }
    
    public static void setMenuState(MenuState state){
        MenuBase.state = state;
    }
    
    public static MenuState getMenuState(){
        return MenuBase.state;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            KeyCode code = event.getCode();
            switch(code){
                case TAB:
                    getChildren().clear();
                    getChildren().addAll(background, menu);
                    state = MenuState.MAIN;
                    break;
            }
        }
    }
    
}
