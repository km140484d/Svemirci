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
        VBox vCenter = new VBox(Main.height/20);
        vCenter.setMinWidth(Main.width);
        vCenter.setAlignment(Pos.CENTER);
        
        vCenter.getChildren().add(infoRow(labels.getInfo().getName(), NAME));
        vCenter.getChildren().add(infoRow(labels.getInfo().getAuthor(), AUTHOR));
        vCenter.getChildren().add(infoRow(labels.getInfo().getMentor(), MENTOR));
        vCenter.getChildren().add(infoRow(labels.getInfo().getVersion(), VERSION));
        vCenter.getChildren().add(infoRow(labels.getInfo().getDescription(), DESCRIPTION));
        vCenter.getChildren().add(makeLabel(COPY_RIGHT));
        vCenter.setTranslateY(Main.height/20);
        getChildren().add(vCenter);
    }
    
    public static VBox infoRow(String label, String text){
        VBox vItem = new VBox(5, makeLabel(label), makeText(text));
        vItem.setAlignment(Pos.CENTER);
        return vItem;
    }
    
    public static Label makeLabel(String text){
        Label label = new Label(text);
        label.setMinWidth(Main.width/8);
        label.setMaxWidth(Main.width/8);
        label.setFont(MenuGroup.FONT_S);
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);
        return label;
    }
    
    public static Text makeText(String str){
        Text text = new Text(str);
        text.maxWidth(Main.width/4);
        text.setFill(Color.CRIMSON);
        text.setStroke(Color.WHITE);
        text.setFont(MenuGroup.FONT_M);
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }
    
}
