package settings.deserializers;

import com.google.gson.*;
import java.lang.reflect.Type;
import settings.Labels;

public class LabelsDeserializer implements JsonDeserializer<Labels>{

    @Override
    public Labels deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jObject = (JsonObject) je;
        Labels labs = new Labels(jObject.get("start").getAsString(),
                jObject.get("final_score").getAsString(),
                jObject.get("victory").getAsString(),
                jObject.get("defeat").getAsString(),
                jObject.get("life").getAsString(),
                jObject.get("time").getAsString(),
                jObject.get("score").getAsString(),
                jObject.get("high_score").getAsString());
        
         JsonObject jCommands =  jObject.get("commands").getAsJsonObject();
         Labels.CommandLabels commands = labs.new CommandLabels(jCommands.get("exit").getAsString(),
                jCommands.get("pause").getAsString(),
                jCommands.get("main_menu").getAsString(),
                jCommands.get("camera_scene").getAsString(),
                jCommands.get("camera_player").getAsString(),
                jCommands.get("player_up").getAsString(),
                jCommands.get("player_down").getAsString(),
                jCommands.get("player_left").getAsString(),
                jCommands.get("player_right").getAsString(),
                jCommands.get("player_rotate_left").getAsString(),
                jCommands.get("player_rotate_right").getAsString(),
                jCommands.get("player_shoot").getAsString()                 
         );
         labs.setCommands(commands);
         
         JsonObject jMenu = jObject.get("menu").getAsJsonObject();
         Labels.MenuLabels menu = labs.new MenuLabels(
                jMenu.get("start").getAsString(),
                jMenu.get("commands").getAsString(),
                jMenu.get("top_10").getAsString(),
                jMenu.get("info").getAsString(),
                jMenu.get("help").getAsString(),
                jMenu.get("exit").getAsString(),
                jMenu.get("high_scores").getAsString(),
                jMenu.get("player1").getAsString(),
                jMenu.get("player2").getAsString()        
         );
         labs.setMenu(menu);
         
         JsonObject jInfo = jObject.get("info").getAsJsonObject();
         Labels.InfoLabels info = labs.new InfoLabels(
                 jInfo.get("name").getAsString(),
                 jInfo.get("author").getAsString(),
                 jInfo.get("mentor").getAsString(),
                 jInfo.get("version").getAsString(),
                 jInfo.get("description").getAsString()
         );
         labs.setInfo(info);

        return labs;
    }
}