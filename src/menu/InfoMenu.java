package menu;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import main.Base;
import main.Main;
import settings.*;

public class InfoMenu extends Base{
    
    private static final String NAME = Main.constants.getName();
    private static final String AUTHOR = "Mirjana Konjikovac";
    private static final String MENTOR = "prof. dr Igor Tartalja";
    private static final String VERSION = "1.0";
    private static final String DESCRIPTION = "Battle with enemies\nfrom outer space.";
    private static final String COPY_RIGHT = "@Copyright";
    
    public InfoMenu(Labels labels){
        double width = Main.constants.getWidth();
        double height = Main.constants.getHeight();
        
        VBox vCenter = new VBox(height/20);
        vCenter.setMinWidth(width);
        vCenter.setAlignment(Pos.CENTER);
        
        vCenter.getChildren().add(infoRow(labels.getInfo().getName(), NAME, width));
        vCenter.getChildren().add(infoRow(labels.getInfo().getAuthor(), AUTHOR, width));
        vCenter.getChildren().add(infoRow(labels.getInfo().getMentor(), MENTOR, width));
        vCenter.getChildren().add(infoRow(labels.getInfo().getVersion(), VERSION, width));
        vCenter.getChildren().add(infoRow(labels.getInfo().getDescription(), DESCRIPTION, width));
        vCenter.getChildren().add(makeLabel(COPY_RIGHT, width));
        vCenter.setTranslateY(height/20);
        getChildren().add(vCenter);
    }
    
    public static VBox infoRow(String label, String text, double width){
        VBox vItem = new VBox(5, makeLabel(label, width), makeText(text, width));
        vItem.setAlignment(Pos.CENTER);
        return vItem;
    }
    
    public static Label makeLabel(String text, double width){
        Label label = new Label(text);
        label.setMinWidth(width/8);
        label.setMaxWidth(width/8);
        label.setFont(MenuGroup.FONT_S);
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);
        return label;
    }
    
    public static Text makeText(String str, double width){
        Text text = new Text(str);
        text.maxWidth(width/4);
        text.setFill(Color.CRIMSON);
        text.setStroke(Color.WHITE);
        text.setFont(MenuGroup.FONT_M);
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }
    
}
