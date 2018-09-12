package menu;

import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.input.*;
import javafx.scene.text.*;
import main.Main;
import settings.Commands;
import sprites.*;

public class MenuGroup extends Group implements EventHandler<KeyEvent>{
    
    public static final Font FONT_S = Font.font("Sylfaen", FontWeight.MEDIUM, 20);
    public static final Font FONT_M = Font.font("Sylfaen", FontWeight.EXTRA_BOLD, 24);
    public static final Font FONT_L = Font.font("Sylfaen", FontWeight.SEMI_BOLD, 40);
    
    public static enum MenuState{MAIN, ANNOUNCEMENT, COMMANDS, TOP, INFO, HELP};
    private static MenuState state = MenuState.MAIN;    
    private MainMenu menu;
    
    private Background background;

    public MenuGroup(Background background, MainMenu menu){
        this.background = background;
        this.menu = menu;
        getChildren().addAll(this.background, this.menu);
    }
    
    public static void setMenuState(MenuState state){
        MenuGroup.state = state;
    }
    
    public static MenuState getMenuState(){
        return MenuGroup.state;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            KeyCode code = event.getCode();
            Commands commands = Main.constants.getCommands();
            if (code == commands.getMain_menu()){
                getChildren().clear();
                getChildren().addAll(background, menu);
                Main.setCurrentMenu(menu);
                state = MenuState.MAIN;
            }else{
                if (code == commands.getExit()){
                    System.exit(0);
                }else{
                    if (code == commands.getFull_screen())
                        Main.stage.setFullScreen(!Main.stage.isFullScreen());
                }
            }
        }
    }    
}
