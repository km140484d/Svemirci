package menu;

import java.util.List;
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
import sprites.Player;
import sprites.Player.Type;

public class HighScoresMenu extends Base{
    
    private static final int TOP = 10;
    
    private HBox[] scoreBoxes = new HBox [TOP];
    
    public HighScoresMenu(double menuWidth, double menuHeight, List<Score> playerScores){
        double width = menuWidth, height = menuHeight;

        Text scoreTitle = new Text(Main.constants.getLabels().getMenu().getHigh_scores());
        scoreTitle.setTextAlignment(TextAlignment.CENTER);
        scoreTitle.setFill(Color.WHITE);
        scoreTitle.setStroke(Color.CRIMSON);
        scoreTitle.setFont(MainMenu.FONT_L);       

        HBox labelBox = new HBox();//width/8);
        labelBox.setMaxWidth(width*10/11);
        labelBox.getChildren().add(makeLabel(Main.constants.getLabels().getPlayer(), Color.CRIMSON, width));
        labelBox.getChildren().add(makeLabel(Main.constants.getLabels().getFinished(), Color.CRIMSON, width));
        labelBox.getChildren().add(makeLabel(Main.constants.getLabels().getScore(), Color.CRIMSON, width));
        Separator sep = new Separator();
        sep.setMaxWidth(width*7/8);
        
        VBox vb = new VBox(height/(TOP*3), labelBox, sep);
        vb.setMaxWidth(width);
        vb.setAlignment(Pos.CENTER);
        Score[] scores = Main.constants.getHigh_scores();
        for(int i=0; i<TOP; i++){
            scoreBoxes[i] = new HBox();//width/8);
            scoreBoxes[i].setMaxWidth(width*10/11);
            if (i<scores.length){
                Label name = makeLabel(scores[i].getName(), Color.WHITE, width);
                Label time = makeLabel(((scores[i].getTime()/60<10)?"0":"") + scores[i].getTime()/60 + ":" + 
                        ((scores[i].getTime()%60<10)?"0":"") + scores[i].getTime()%60, Color.WHITE, width);
                Label points = makeLabel(scores[i].getPoints() + "", Color.WHITE, width);
                scoreBoxes[i].getChildren().addAll(name,time,points);
                if (playerScores != null && !playerScores.isEmpty()){
                    for(Score s:playerScores){
                        if (s.equals(scores[i])){
                            Color color = Color.SKYBLUE;
                            if (s.getType().equals(Type.PLAYER2))
                                color = Color.PALEGOLDENROD;
                            String webFormat = String.format("#%02x%02x%02x",
                                (int) (255 * color.getRed()),
                                (int) (255 * color.getGreen()),
                                (int) (255 * color.getBlue()));
                            scoreBoxes[i].setStyle("-fx-border-color:" + webFormat + ";"
                            + "-fx-border-width:2;");
                            playerScores.remove(s);
                            break;
                        }
                    }
                }

                if (i == 0){
                    animateScore(name);
                    animateScore(time);
                    animateScore(points);
                }
            }else
                scoreBoxes[i].getChildren().addAll(makeLabel("-", Color.WHITE, width),
                        makeLabel("-", Color.WHITE, width), makeLabel("-", Color.WHITE, width));
        }         
        VBox mainBox = new VBox(height/40, scoreTitle, vb);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setMinWidth(width);
        mainBox.setTranslateY(height/60);
        
        Rectangle border = new Rectangle(width*11/12, height*9/10);
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
        label.setMinWidth(width*7/24);
        label.setFont(MainMenu.FONT_S);
        label.setTextFill(color);
        label.setAlignment(Pos.CENTER);
        return label;
    }
    
}
