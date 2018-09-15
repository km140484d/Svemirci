package menu;

import java.util.*;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import main.*;
import settings.*;

public class Announcement extends Base{
    
    public class Level extends Group{
        
        private Rectangle border;
        private Runnable script;
        
        public Level(double width, double height, Configuration config){            
            border = new Rectangle(width, height);
            border.setArcWidth(width/8);
            border.setArcHeight(height/8);
            border.setStroke(Color.WHITE);
            border.setFill(new ImagePattern(new Image("/resources/menu/" + config.getName() + ".png")));
            script = () -> {
                Main.resetGame();
                String p1 = !player1Name.getText().equals("") ? player1Name.getText() : PLAYER1;
                String p2 = centerBox.getChildren().contains(player2Box) ?
                                (!player2Name.getText().equals("") ? player2Name.getText() : PLAYER2) : null;
                Main.createGame(config, p1, p2);
                Main.startGame();
            };
            this.setOnMouseClicked(c -> script.run());
            this.setOnMouseEntered(e ->{
                removeFocus(levels.get(focused).getBorder(), width, height);
                focused = levels.indexOf(this);
                setFocus(border);
            });
            this.setOnMouseExited(e -> {
                if (!playerFocus){
                    removeFocus(border, width, height);
                    focused = 0;
                }
            });            
            getChildren().add(border);
        }
        
        public Rectangle getBorder(){
            return border;
        }
        
        public Runnable getScript(){
            return script;
        }
        
    }
    
    private static final String PLAYER1 = Main.constants.getLabels().getMenu().getPlayer1();
    private static final String PLAYER2 = Main.constants.getLabels().getMenu().getPlayer2();
    
    private List<Level> levels = new ArrayList<>();
    private TextField player1Name = new TextField(), player2Name = new TextField();
    private VBox player1Box, player2Box, centerBox;
    private boolean playerFocus = false, shift = false;
    private int focused = 0;
    
    public Announcement(Configuration[] configs, double menuWidth, double menuHeight){
        double windowWidth = menuWidth, windowHeight = menuHeight;
        int col_row, cnt=0;
        if (configs.length <= 8)
            col_row = 3;
        else
            col_row = 1 + configs.length/4 + (configs.length%4 == 0?0:1);
        double width = windowWidth / (col_row), height = windowHeight / (col_row); 
        
        player1Box = makePlayerBox(PLAYER1, player1Name, col_row, windowWidth, height);
        player2Box = makePlayerBox(PLAYER2, player2Name, col_row, windowWidth, height);
        
        javafx.scene.control.CheckBox check = new CheckBox("Second player"); 
        check.setFont(MainMenu.FONT_S);
        check.setAllowIndeterminate(false);
        check.setSelected(false);
        check.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (centerBox.getChildren().contains(player2Box))
                centerBox.getChildren().remove(player2Box);
            else
                centerBox.getChildren().add(player2Box);            
        });
        check.setOnKeyReleased(k ->{
            KeyCode code = k.getCode();
            switch(code){
                case ENTER:
                    if (!playerFocus)
                        check.setSelected(!check.isSelected());
                    break;
            }
        });
        
        check.setTextFill(Color.WHITE);
        check.setScaleShape(true);
        
        Rectangle player1Border = new Rectangle(width, height);
        player1Border.setFill(Color.TRANSPARENT);
        player1Border.setStroke(Color.WHITE);
        player1Border.setStrokeWidth(3);
        player1Border.setTranslateX(windowWidth/2 - width/2);
        player1Border.setTranslateY(windowHeight/2 - height/2);      
        getChildren().add(player1Border);
        
        centerBox = new VBox(height/15);
        centerBox.setMinHeight(0);
        centerBox.setMinWidth(width);
        centerBox.setTranslateX(windowWidth/2 - width/2);
        centerBox.setTranslateY(windowHeight/2);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(player1Box, check);           
        getChildren().add(centerBox);       

        outer:for(int i=0; i<col_row; i++){
            int col = 0;
            for(int j=0; j<col_row; j++){                 
                if (((i!=0) && (i!=col_row-1)) && ((j!=0) && (j!=col_row-1)))
                    continue;
                Level level = new Level(windowWidth / (col_row+1), windowHeight / (col_row+1), configs[i*col_row + col]);
                level.setTranslateX(width*(j-1/2) + (windowWidth / col_row - windowWidth / (col_row+1))/2);
                level.setTranslateY(height*(i+1/2) + (windowHeight / col_row - windowHeight / (col_row+1))/2);
                levels.add(level);
                col++; cnt++;
                if (cnt >= configs.length)
                    break outer;
            }
        }
        getChildren().addAll(levels);    
        
        this.setOnKeyReleased(k ->{
            KeyCode code = k.getCode();
            switch(code){
                case HOME:
                    if (playerFocus){
                        removeFocus(levels.get(focused).getBorder(), width, height);
                        centerBox.setDisable(false);
                        player1Box.requestFocus();
                    }else{
                        setFocus(levels.get(focused).getBorder());
                        centerBox.setDisable(true);
                        levels.get(focused).getBorder().requestFocus();
                    }
                    playerFocus = !playerFocus;                    
                    break;
                case TAB:
                    if (playerFocus){
                        removeFocus(levels.get(focused).getBorder(), width, height);
                        if (shift)
                            focused = (focused + (levels.size() - 1)) % levels.size();
                        else
                            focused = (focused + 1) % levels.size();
                        setFocus(levels.get(focused).getBorder());
                    }
                    break;
                case ENTER:
                    if (playerFocus)
                        levels.get(focused).getScript().run();                    
                    break;
                case SHIFT:
                    shift = false;
                    break;
            }
        }); 
        this.setOnKeyPressed(p -> {
            KeyCode code = p.getCode();
            switch(code){
                case SHIFT:
                    if (!shift)
                        shift = true;
                    break;
            }
        });
        
    }
    
    public void removeFocus(Rectangle border, double width, double height){
        border.setArcWidth(width/8);
        border.setArcHeight(height/8);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(1);
    }
    
    public void setFocus(Rectangle border){
        border.setArcWidth(0);
        border.setArcHeight(0);
        border.setStroke(Color.CRIMSON);
        border.setStrokeWidth(2);
    }
    
    @Override
    public void resizeWindow(double ratioWidth, double ratioHeight){
        super.resizeWindow(ratioWidth, ratioHeight);
        centerBox.setMaxHeight(centerBox.getMinHeight()*ratioHeight);
    }
    
    public static VBox makePlayerBox(String label, TextField playerName, int col_row, double windowWidth, double height){
        Text player = new Text(label);
        player.minWidth(windowWidth / (col_row + 1));
        player.setFont(MainMenu.FONT_S);
        player.setFill(Color.WHITE);
        player.setStroke(Color.CRIMSON);
        playerName.setFont(MainMenu.FONT_S);
        playerName.setAlignment(Pos.CENTER);
        playerName.setMaxWidth(windowWidth / (col_row*2));
        playerName.setMinHeight(height/6);
        VBox playerBox = new VBox(height/25);
        playerBox.setAlignment(Pos.CENTER);
        playerBox.getChildren().addAll(player, playerName);
        return playerBox;
    }
    
}
