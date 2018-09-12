package menu;

import java.util.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import main.Base;
import main.Main;
import settings.*;

public class Announcement extends Base{
    
    public class Level extends Group{
        
        private Rectangle border;
        
        public Level(double width, double height, Configuration config){            
            border = new Rectangle(width, height);
            border.setArcWidth(width/8);
            border.setArcHeight(height/8);
            border.setStroke(Color.WHITE);
            border.setFill(new ImagePattern(new Image("/resources/menu/" + config.getName() + ".png")));
            border.setOnMouseClicked(c -> {
                Main.resetGame();
                String p1 = !player1Name.getText().equals("") ? player1Name.getText() : PLAYER1;
                String p2 = centerBox.getChildren().contains(player2Box) ?
                                (!player2Name.getText().equals("") ? player2Name.getText() : PLAYER2) : null;
                Main.createGame(config, p1, p2);
                Main.startGame();
            });
            getChildren().add(border);
        }
        
    }
    
    private static final String PLAYER1 = Main.constants.getLabels().getMenu().getPlayer1();
    private static final String PLAYER2 = Main.constants.getLabels().getMenu().getPlayer2();
    
    private List<Level> levels = new ArrayList<>();
    private TextField player1Name = new TextField(), player2Name = new TextField();
    private VBox player1Box, player2Box, centerBox;
    
    public Announcement(Configuration[] configs){
        double windowWidth = Main.constants.getWidth();
        double windowHeight = Main.constants.getHeight();
        int col_row, cnt=0;
        if (configs.length <= 8)
            col_row = 3;
        else
            col_row = 1 + configs.length/4 + (configs.length%4 == 0?0:1);
        double width = windowWidth / (col_row), height = windowHeight / (col_row); 
        
        player1Box = makePlayerBox(PLAYER1, player1Name, col_row, windowWidth, height);
        player2Box = makePlayerBox(PLAYER2, player2Name, col_row, windowWidth, height);
        
        centerBox = new VBox(height/10);
        centerBox.setMaxHeight(height*4/5);
        centerBox.setTranslateX(windowWidth*3/8);
        centerBox.setTranslateY(windowHeight/(col_row));
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(player1Box);        
        getChildren().add(centerBox);
        
        Rectangle player1Border = new Rectangle(width, height);
        player1Border.setFill(Color.TRANSPARENT);
        player1Border.setStroke(Color.CRIMSON);
        player1Border.setStrokeWidth(2);
        player1Border.setTranslateX(windowWidth/2 - width/2);
        player1Border.setTranslateY(windowHeight/2 - height/2);
        player1Border.setOnMouseClicked(m -> {
            if (centerBox.getChildren().contains(player2Box))
                centerBox.getChildren().remove(player2Box);
            else
                centerBox.getChildren().add(player2Box);            
        });        
        getChildren().add(player1Border);

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
        
    }
    
    @Override
    public void resizeWindow(double ratioWidth, double ratioHeight){
        super.resizeWindow(ratioWidth, ratioHeight);
        centerBox.setMaxHeight(centerBox.getMinHeight()*ratioHeight);
        Scale scale = new Scale();
        scale.setX(ratioWidth);
        scale.setY(ratioHeight);
        //centerBox.getTransforms().add(scale);
    }
    
    public static VBox makePlayerBox(String label, TextField playerName, int col_row, double windowWidth, double height){
        Label player = new Label(label);
        player.setMinWidth(windowWidth / (col_row + 1));
        player.setFont(MenuGroup.FONT_M);
        player.setTextFill(Color.WHITE);
        player.setAlignment(Pos.CENTER);
        playerName.setFont(MenuGroup.FONT_M);
        playerName.setAlignment(Pos.CENTER);
        playerName.setMaxWidth(windowWidth / (col_row + 2));
        playerName.setMaxHeight(height/20);
        VBox playerBox = new VBox(height/20);
        playerBox.setAlignment(Pos.CENTER);
        playerBox.getChildren().addAll(player, playerName);
        return playerBox;
    }
    
}
