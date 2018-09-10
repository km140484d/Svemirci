package menu;

import java.util.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import main.Main;
import sprites.shots.*;

public class Menu extends VBox implements EventHandler<KeyEvent>{
    
    private static final double VERT_SPACE = 20;
    private static final double HOR_SPACE = 15;
    
    public class MenuItem extends HBox{      
        private Shot[] icons = new Shot[2];
        private Text text;
        private Runnable code;
        
        public MenuItem(String string, Runnable code){
            super(HOR_SPACE);
            this.setAlignment(Pos.CENTER);
            for(int i=0; i < icons.length; i++)
                this.icons[i] = new Hexagon(0,0);
            this.text = new Text(string);
            this.text.setFont(Main.FONT);
            this.code = code;
            //this.text.setEffect(new GaussianBlur(2));
            getChildren().addAll(this.icons[0], text, this.icons[1]);
            setItemActive(false);
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
  
    public Menu(double width, double height){
        super(VERT_SPACE);
        this.setAlignment(Pos.CENTER);
        items.add(new MenuItem("START", () -> {
            Main.startMenuItem(new Announcement(Main.constants.getConfigurations()));
        }));
        items.add(new MenuItem("COMMANDS", null));
        items.add(new MenuItem("TOP 10", () -> {
            Main.startMenuItem(new HighScores());
        }));
        items.add(new MenuItem("INFO", null));
        items.add(new MenuItem("HELP", null));
        items.add(new MenuItem("EXIT", () -> {System.exit(0);}));
        
        items.get(0).setItemActive(true);
        
        getChildren().addAll(items);
        
        setTranslateX(width);
        setTranslateY(height);
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
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
