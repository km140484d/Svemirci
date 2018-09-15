package menu;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import main.Base;
import settings.*;
import settings.Commands.*;

public class CommandsMenu extends Base{

    private Labels labels;
    private Commands commands;
    
    public CommandsMenu(Labels labels, Commands commands, double menuWidth, double menuHeight){
        this.labels = labels;
        this.commands = commands;
        
        double width = menuWidth, height = menuHeight;
        
        Rectangle leftRect = new Rectangle(width/4, height*8/9);
        leftRect.setTranslateX(width/24);
        leftRect.setTranslateY(height/10);
        leftRect.setFill(Color.TRANSPARENT);
        leftRect.setStroke(Color.WHITE);
        leftRect.setArcWidth(leftRect.getWidth()/5);
        leftRect.setArcHeight(leftRect.getHeight()/8);
        
        Rectangle centerRect = new Rectangle(width/4, height*2/3);
        centerRect.setTranslateX(width/24 + width/3);
        centerRect.setTranslateY(height*7/24);
        centerRect.setFill(Color.TRANSPARENT);
        centerRect.setStroke(Color.WHITE);
        centerRect.setArcWidth(centerRect.getWidth()/5);
        centerRect.setArcHeight(centerRect.getHeight()/8);
        
        Rectangle rightRect = new Rectangle(width/4, height*8/9);
        rightRect.setTranslateX(width/24 + width*2/3);
        rightRect.setTranslateY(height/10);
        rightRect.setFill(Color.TRANSPARENT);
        rightRect.setStroke(Color.WHITE);
        rightRect.setArcWidth(rightRect.getWidth()/5);
        rightRect.setArcHeight(rightRect.getHeight()/8);
        getChildren().addAll(leftRect, centerRect, rightRect);
        
        VBox mainBox = new VBox(height/15);        
        mainBox.getChildren().add(commandRow(labels.getCommands().getExit(), commands.getExit().toString(), width));
        mainBox.getChildren().add(commandRow(labels.getCommands().getPause(), commands.getPause().toString(), width));
        mainBox.getChildren().add(commandRow(labels.getCommands().getMain_menu(), commands.getMain_menu().toString(), width));
        mainBox.getChildren().add(commandRow(labels.getCommands().getCamera_scene(), commands.getCamera_scene().toString(), width));
        mainBox.getChildren().add(commandRow(labels.getCommands().getCamera_player1(), commands.getCamera_player1().toString(), width));
        mainBox.getChildren().add(commandRow(labels.getCommands().getCamera_player2(), commands.getCamera_player2().toString(), width));
        
        Text title = new Text(labels.getMenu().getCommands());
        title.setWrappingWidth(width/4);
        title.setFill(Color.CRIMSON);
        title.setStroke(Color.WHITE);
        title.setFont(MainMenu.FONT_L);
        title.setTextAlignment(TextAlignment.CENTER);
        VBox centerBox = new VBox(height/15, title, mainBox); 
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setTranslateX(width/24 + width/3);
        centerBox.setTranslateY(height/6);
        getChildren().add(centerBox);
        
        playerHelp(labels.getMenu().getPlayer1(), commands.getPlayer1(), width/48, width, height);
        playerHelp(labels.getMenu().getPlayer2(), commands.getPlayer2(), width/48 + width*2/3, width, height);       
    }
    
    public void playerHelp(String title, PlayerCommands player, double translX, double width, double height){
        VBox vb = new VBox(height/15);
        vb.setMaxWidth(width/5);
        vb.setAlignment(Pos.CENTER);
        Label titleLab = new Label(title);
        titleLab.setMinWidth(width*11/36);
        titleLab.setMaxWidth(width*11/36);
        titleLab.setTextFill(Color.WHITE);
        titleLab.setAlignment(Pos.CENTER);
        titleLab.setFont(MainMenu.FONT_M);
        vb.getChildren().add(titleLab);
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_up(), player.getUp().toString(), width));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_down(), player.getDown().toString(), width));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_left(), player.getLeft().toString(), width));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_right(), player.getRight().toString(), width));
        Separator sep = new Separator();
        sep.setMaxWidth(width/5);
        vb.getChildren().add(sep);  
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_rotate_left(), player.getRotate_left().toString(), width));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_rotate_right(), player.getRotate_right().toString(), width));
        vb.getChildren().add(commandRow(labels.getCommands().getPlayer_shoot(), player.getShoot().toString(), width));
        vb.setTranslateX(translX);
        vb.setTranslateY(height/80);
        getChildren().add(vb);
    }
    
    public HBox commandRow(String label, String command, double width){
        HBox hb = new HBox( makeLabel(label, "L", width), makeLabel(command, "C", width));
        hb.setAlignment(Pos.CENTER);
        return hb;
    }
    
    public static Label makeLabel(String text, String type, double width){
        Label label = new Label(text);
        label.setMinWidth(width/8);
        label.setMaxWidth(width/8);
        if (type.equals("L")){            
            label.setFont(MainMenu.FONT_M);
            label.setTextFill(Color.CRIMSON);
        }
        else{
            label.setFont(MainMenu.FONT_S);
            label.setTextFill(Color.WHITE);
        }
        label.setAlignment(Pos.CENTER);
        return label;
    }
}
