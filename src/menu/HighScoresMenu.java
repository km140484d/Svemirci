package menu;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;
import main.*;
import settings.*;

public class HighScoresMenu extends Base{
    
    private static final int TOP = 10;
    
    private HBox[] scoreBoxes = new HBox [TOP];
    
    public HighScoresMenu(){
        double width = Main.constants.getWidth();
        double height = Main.constants.getHeight();
        
        Text scoreTitle = new Text(Main.constants.getLabels().getMenu().getHigh_scores());
        scoreTitle.minWidth(width);
        scoreTitle.setTextAlignment(TextAlignment.CENTER);
        scoreTitle.setFill(Color.WHITE);
        scoreTitle.setStroke(Color.CRIMSON);
        scoreTitle.setFont(MenuGroup.FONT_L);       
        
        VBox vb = new VBox(height/(TOP*3));
        HBox labelBox = new HBox(width/8);
        labelBox.getChildren().add(makeLabel(Main.constants.getLabels().getPlayer(), Color.CRIMSON, width));
        labelBox.getChildren().add(makeLabel(Main.constants.getLabels().getFinished(), Color.CRIMSON, width));
        labelBox.getChildren().add(makeLabel(Main.constants.getLabels().getScore(), Color.CRIMSON, width));
        vb.getChildren().add(labelBox);
        Score[] scores = Main.constants.getHigh_scores();
        for(int i=0; i<TOP; i++){
            scoreBoxes[i] = new HBox(width/8);
            if (i<scores.length){
                Label name = makeLabel(scores[i].getName(), Color.WHITE, width);
                Label time = makeLabel(((scores[i].getTime()/60<10)?"0":"") + scores[i].getTime()/60 + ":" + 
                        ((scores[i].getTime()%60<10)?"0":"") + scores[i].getTime()%60, Color.WHITE, width);
                Label points = makeLabel(scores[i].getPoints() + "", Color.WHITE, width);
                scoreBoxes[i].getChildren().addAll(name,time,points);
                if (i == 0){
                    animateScore(name);
                    animateScore(time);
                    animateScore(points);
                }
            }else
                scoreBoxes[i].getChildren().addAll(makeLabel("-", Color.WHITE, width),
                        makeLabel("-", Color.WHITE, width), makeLabel("-", Color.WHITE, width));
        } 
        
        VBox mainBox = new VBox(height/30, scoreTitle, vb);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setTranslateY(height/60);
        
        Rectangle border = new Rectangle(width*11/12, height*17/20);
        border.setArcWidth(border.getWidth()/8);
        border.setArcHeight(border.getHeight()/8);
        border.setTranslateX(width/24);
        border.setTranslateY(height/10);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.WHITE);
        vb.getChildren().addAll(scoreBoxes);
        getChildren().addAll(border, mainBox);
    }
    
    public static void animateScore(Label label){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), 
                        new KeyValue(label.scaleXProperty(), 1, Interpolator.EASE_IN),
                        new KeyValue(label.scaleYProperty(), 1, Interpolator.EASE_IN),
                        new KeyValue(label.textFillProperty(), Color.WHITE, Interpolator.LINEAR)
                ),
                new KeyFrame(Duration.seconds(1), 
                        new KeyValue(label.scaleXProperty(), 1.5, Interpolator.EASE_IN),
                        new KeyValue(label.scaleYProperty(), 1.5, Interpolator.EASE_IN),
                        new KeyValue(label.textFillProperty(), Color.CRIMSON, Interpolator.LINEAR)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    public static Label makeLabel(String text, Color color, double width){
        Label label = new Label(text);
        label.setMinWidth(width / 4);
        label.setFont(MenuGroup.FONT_S);
        label.setTextFill(color);
        label.setAlignment(Pos.CENTER);
        return label;
    }
    
}
