package menu;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import main.Base;
import main.Main;
import settings.*;
import settings.Commands.*;

public class CommandsMenu extends Base{

    private Labels labels;
    private Commands commands;
    
    public CommandsMenu(Labels labels, Commands commands){
        this.labels = labels;
        this.commands = commands;
        
        Rectangle leftRect = new Rectangle(Main.width/4, Main.height*8/9);
        leftRect.setTranslateX(Main.width/24);
        leftRect.setTranslateY(Main.height/10);
        leftRect.setFill(Color.TRANSPARENT);
        leftRect.setStroke(Color.WHITE);
        leftRect.setArcWidth(leftRect.getWidth()/5);
        leftRect.setArcHeight(leftRect.getHeight()/8);
        
        Rectangle centerRect = new Rectangle(Main.width/4, Main.height*3/5);
        centerRect.setTranslateX(Main.width/24 + Main.width/3);
        centerRect.setTranslateY(Main.height/3);
        centerRect.setFill(Color.TRANSPARENT);
        centerRect.setStroke(Color.WHITE);
        centerRect.setArcWidth(centerRect.getWidth()/5);
        centerRect.setArcHeight(centerRect.getHeight()/8);
        
        Rectangle rightRect = new Rectangle(Main.width/4, Main.height*8/9);
        rightRect.setTranslateX(Main.width/24 + Main.width*2/3);
        rightRect.setTranslateY(Main.height/10);
        rightRect.setFill(Color.TRANSPARENT);
        rightRect.setStroke(Color.WHITE);
        rightRect.setArcWidth(rightRect.getWidth()/5);
        rightRect.setArcHeight(rightRect.getHeight()/8);
        getChildren().addAll(leftRect, centerRect, rightRect);
        
        VBox mainBox = new VBox(Main.height/15);        
        mainBox.getChildren().add(commandRow(labels.getCommands().getExit(), commands.getExit().toString()));
        mainBox.getChildren().add(commandRow(labels.getCommands().getPause(), commands.getPause().toString()));
        mainBox.getChildren().add(commandRow(labels.getCommands().getMain_menu(), commands.getMain_menu().toString()));
        mainBox.getChildren().add(commandRow(labels.getCommands().getCamera_scene(), commands.getCamera_scene().toString()));
        mainBox.getChildren().add(commandRow(labels.getCommands().getCamera_player(), commands.getCamera_player().toString()));
        
        Text title = new Text(labels.getMenu().getCommands());
        title.setWrappingWidth(Main.width/4);
        title.setFill(Color.CRIMSON);
        title.setStroke(Color.WHITE);
        title.setFont(MenuGroup.FONT_L);
        title.setTextAlignment(TextAlignment.CENTER);
        VBox centerBox = new VBox(Main.height/10, title, mainBox); 
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setTranslateX(Main.width/24 + Main.width/3);
        centerBox.setTranslateY(Main.height/5);
        getChildren().add(centerBox);
        
        playerHelp(labels.getMenu().getPlayer1(), commands.getPlayer1(), Main.width/48);
        playerHelp(labels.getMenu().getPlayer2(), commands.getPlayer2(), Main.width/48 + Main.width*2/3);       
    }
    
    public void playerHelp(String title, PlayerCommands player, double translX){
        VBox vb = new VBox(Main.height/15);
        vb.setMaxWidth(Main.width/5);
        vb.setAlignment(Pos.CENTER);
        Label titleLab = new Label(title);
        titleLab.setMinWidth(Main.width*11/36);
        titleLab.setMaxWidth(Main.width*11/36);
        titleLab.setTextFill(Color.WHITE);
        titleLab.setAlignment(Pos.CENTER);
        titleLab.setFont(MenuGroup.FONT_M);
        vb.getChildren().add(titleLab);
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_up(), player.getUp().toString()));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_down(), player.getDown().toString()));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_left(), player.getLeft().toString()));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_right(), player.getRight().toString()));
        Separator sep = new Separator();
        sep.setMaxWidth(Main.width/5);
        vb.getChildren().add(sep);  
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_rotate_left(), player.getRotate_left().toString()));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_rotate_right(), player.getRotate_right().toString()));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_shoot(), player.getShoot().toString()));
        vb.setTranslateX(translX);
        vb.setTranslateY(Main.height/80);
        getChildren().add(vb);
    }
    
    public HBox commandRow(String label, String command){
        HBox hb = new HBox( makeLabel(label, "L"), makeLabel(command, "C"));
        hb.setAlignment(Pos.CENTER);
        return hb;
    }
    
    public static Label makeLabel(String text, String type){
        Label label = new Label(text);
        label.setMinWidth(Main.width/8);
        label.setMaxWidth(Main.width/8);
        if (type.equals("L")){            
            label.setFont(MenuGroup.FONT_M);
            label.setTextFill(Color.CRIMSON);
        }
        else{
            label.setFont(MenuGroup.FONT_S);
            label.setTextFill(Color.WHITE);
        }
        label.setAlignment(Pos.CENTER);
        return label;
    }
}
