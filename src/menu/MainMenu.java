package menu;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import main.Base;
import main.Main;
import settings.Constants;
import settings.Labels.*;
import sprites.shots.*;

public class MainMenu extends Base implements EventHandler<KeyEvent>{
    
    private static final double VERT_SPACE = 20;
    private static final double HOR_SPACE = 15;
    
    public static final Font FONT_S = Font.font("Sylfaen", FontWeight.MEDIUM, 18);
    public static final Font FONT_M = Font.font("Sylfaen", FontWeight.EXTRA_BOLD, 22);
    public static final Font FONT_L = Font.font("Sylfaen", FontWeight.SEMI_BOLD, 40);
    
    public class MenuItem extends HBox{      
        private Shot[] icons = new Shot[2];
        private Text text;
        private Runnable code;
        
        public MenuItem(String string, Runnable code){
            super(HOR_SPACE);
            this.setAlignment(Pos.CENTER);
            for(int i=0; i < icons.length; i++)
                this.icons[i] = new Hexagon(0,0, false);
            this.text = new Text(string);
            this.text.setFont(FONT_M);
            this.code = code;
            getChildren().addAll(this.icons[0], text, this.icons[1]);
            setItemActive(false);
            setOnMouseClicked(m -> {code.run();});
            setOnMouseEntered(m ->{
                items.get(active).setItemActive(false);   
                active = items.indexOf(this);
                items.get(active).setItemActive(true);
                
            });
        }
        
        public void setItemActive(boolean state){
            for(int i=0; i < icons.length; i++)
                icons[i].setVisible(state);
            text.setFill(state ? Color.WHITE : Color.LIGHTGRAY);
            this.setScaleX(state ? 1.2 : 1);
            this.setScaleY(state ? 1.2 : 1);
        }
        
        public void activate(){
            if (code != null)
                code.run();
        }
        
    }

    private List<MenuItem> items = new ArrayList<>();
    private int active = 0;
  
    public MainMenu(double width, double height){
        Constants consts = Main.constants;
        VBox vBox = new VBox(VERT_SPACE);
        MenuLabels menuLabs = consts.getLabels().getMenu();
        vBox.setAlignment(Pos.CENTER);
        items.add(new MenuItem(menuLabs.getStart(), () -> {
            Main.startMenuItem(new Announcement(consts.getConfigurations(), consts.getWidth(), consts.getHeight()*8/9));
        }));
        items.add(new MenuItem(menuLabs.getCommands(), () -> {
            Main.startMenuItem(new CommandsMenu(consts.getLabels(), consts.getCommands(), 
                    consts.getWidth(), consts.getHeight()*8/9));
        }));
        items.add(new MenuItem(menuLabs.getTop_10(), () -> {
            Main.startMenuItem(new HighScoresMenu(consts.getWidth(), consts.getHeight()*8/9, null));
        }));
        items.add(new MenuItem(menuLabs.getInfo(), () -> {
            Main.startMenuItem(new InfoMenu(consts.getLabels(), consts.getWidth(), consts.getHeight()*8/9));
        }));
        items.add(new MenuItem(menuLabs.getHelp(), () -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(new File("data\\Aliens.pdf")); //data\\Aliens.pdf
                } else {
                    System.out.println("Open is not supported");
                }
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }));
        items.add(new MenuItem(menuLabs.getExit(), () -> {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(Main.SETTINGS_FILE))){                
                writer.write(Main.getGson().toJson(Main.constants));
                writer.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            System.exit(0);
        }));
        
        items.get(0).setItemActive(true);
        
        vBox.getChildren().addAll(items);
        getChildren().add(vBox);
        setTranslateX(width);
        setTranslateY(height);
    }

    @Override
    public void handle(KeyEvent event) {
        if (Main.containsMenu() && event.getEventType() == KeyEvent.KEY_RELEASED){
            KeyCode code = event.getCode();
            switch(code){
                case UP:
                    if (active > 0){
                        items.get(active).setItemActive(false);                        
                        items.get(active - 1).setItemActive(true);
                        active--;
                    }                    
                    break;
                case DOWN:
                    if (active < items.size()-1){
                        items.get(active).setItemActive(false);                        
                        items.get(active + 1).setItemActive(true);
                        active++;
                    }
                    break;
                case ENTER:
                    items.get(active).activate();
                    break;
            
            }
        }
    }

}
