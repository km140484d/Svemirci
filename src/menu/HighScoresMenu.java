package menu;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.*;
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
        Text scoreTitle = new Text(Main.constants.getLabels().getMenu().getHigh_scores());
        scoreTitle.minWidth(Main.width);
        scoreTitle.setTextAlignment(TextAlignment.CENTER);
        scoreTitle.setFill(Color.WHITE);
        scoreTitle.setStroke(Color.CRIMSON);
        scoreTitle.setFont(MenuGroup.FONT_L);       
        
        VBox vb = new VBox(Main.height/(TOP*3));
        Score[] scores = Main.constants.getHigh_scores();
        for(int i=0; i<TOP; i++){
            scoreBoxes[i] = new HBox(Main.width/8);
            if (i<scores.length){
                Label name = makeLabel(scores[i].getName());
                Label time = makeLabel(((scores[i].getTime()/60<10)?"0":"") + scores[i].getTime()/60 + ":" + 
                        ((scores[i].getTime()%60<10)?"0":"") + scores[i].getTime()%60);
                Label points = makeLabel(scores[i].getPoints() + "");
                scoreBoxes[i].getChildren().addAll(name,time,points);
                if (i == 0){
                    animateScore(name);
                    animateScore(time);
                    animateScore(points);
                }
            }else
                scoreBoxes[i].getChildren().addAll(makeLabel("-"), makeLabel("-"), makeLabel("-"));
        } 
        
        VBox mainBox = new VBox(Main.height/30, scoreTitle, vb);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setTranslateY(Main.height/60);
        
        Rectangle border = new Rectangle(Main.width*11/12, Main.height*17/20);
        border.setArcWidth(border.getWidth()/8);
        border.setArcHeight(border.getHeight()/8);
        border.setTranslateX(Main.width/24);
        border.setTranslateY(Main.height/10);
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
        
//        
//        ScaleTransition st = new ScaleTransition(Duration.seconds(1), label);
//        st.setFromX(1); st.setFromY(1);
//        st.setToX(1.5); st.setToY(1.5);
//        st.setAutoReverse(true);
//        st.setCycleCount(Animation.INDEFINITE);
//        st.play();
    }
    
    public static Label makeLabel(String text){
        Label label = new Label(text);
        label.setMinWidth(Main.width / 4);
        label.setFont(MenuGroup.FONT_M);
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);
        return label;
    }
    
}
