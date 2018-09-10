package menu;

import java.util.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
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
                Main.createGame(config, !player1Name.getText().equals("")?player1Name.getText():LABEL);
                Main.startGame();
            });
            getChildren().add(border);
        }
        
    }
    
    private static final String LABEL = Main.constants.getLabels().getMenu().getPlayer1();
    
    private List<Level> levels = new ArrayList<>();
    private TextField player1Name;
    
    
    public Announcement(Configuration[] configs){
        
        int col_row, cnt=0;
        if (configs.length <= 8)
            col_row = 3;
        else
            col_row = 1 + configs.length/4 + (configs.length%4 == 0?0:1);
        double width = Main.width / (col_row), height = Main.height / (col_row); 
        
        Label player1 = new Label(LABEL);
        player1.setMinWidth(Main.width / (col_row + 1));
        player1.setFont(Main.FONT);
        player1.setTextFill(Color.WHITE);
        player1.setAlignment(Pos.CENTER);
        player1Name = new TextField();
        player1Name.setFont(Main.FONT);
        player1Name.setAlignment(Pos.CENTER);
        player1Name.setMaxWidth(Main.width / (col_row + 1));
        player1Name.setMaxHeight(height/6);
        VBox vb = new VBox(height/8);
        vb.getChildren().addAll(player1, player1Name);
        vb.setTranslateX(Main.width*3/8);
        vb.setTranslateY(Main.height*2/5);
        getChildren().add(vb);

        outer:for(int i=0; i<col_row; i++){
            int col = 0;
            for(int j=0; j<col_row; j++){                 
                if (((i!=0) && (i!=col_row-1)) && ((j!=0) && (j!=col_row-1)))
                    continue;
                Level level = new Level(Main.width / (col_row+1), Main.height / (col_row+1), configs[i*col_row + col]);
                level.setTranslateX(width*(j-1/2) + (Main.width / col_row - Main.width / (col_row+1))/2);
                level.setTranslateY(height*(i+1/2) + (Main.height / col_row - Main.height / (col_row+1))/2);
                levels.add(level);
                col++; cnt++;
                if (cnt >= configs.length)
                    break outer;
            }
        }
        getChildren().addAll(levels);    
        
    }
    
}
