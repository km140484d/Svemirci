package menu;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import settings.*;

public class HighScores extends Group{
    
    private static final int TOP = 10;
    
    private HBox[] scoreBoxes = new HBox [TOP];
    
    public HighScores(){
        Label scoreLabel = new Label("High scores");
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setMinWidth(Main.width);
        scoreLabel.setMinHeight(Main.height / 10);
        scoreLabel.setFont(Font.font("Sylfaen", FontWeight.EXTRA_BOLD, 30));   
        VBox vb = new VBox(Main.height/25);
        Score[] scores = Main.constants.getHigh_scores();
        for(int i=0; i<TOP; i++){
            scoreBoxes[i] = new HBox(Main.width/8);
            if (i<scores.length)                
                scoreBoxes[i].getChildren().addAll(makeLabel(scores[i].getName()), 
                    makeLabel(scores[i].getTime()/60 + ":" + scores[i].getTime()%60), 
                    makeLabel(scores[i].getPoints() + ""));
            else
                scoreBoxes[i].getChildren().addAll(makeLabel("-"), makeLabel("-"), makeLabel("-"));
        } 
        vb.setTranslateY(Main.height / 8);
        vb.getChildren().addAll(scoreBoxes);
        getChildren().addAll(scoreLabel, vb);
    }
    
    public static Label makeLabel(String text){
        Label label = new Label(text);
        label.setMinWidth(Main.width / 4);
        label.setFont(Main.FONT);
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);
        return label;
    }
    
}
