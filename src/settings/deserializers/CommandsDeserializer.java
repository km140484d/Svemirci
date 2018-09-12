package settings.deserializers;

import com.google.gson.*;
import java.lang.reflect.Type;
import javafx.scene.input.KeyCode;
import settings.Commands;

public class CommandsDeserializer implements JsonDeserializer<Commands>{

    @Override
    public Commands deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jObject = (JsonObject) je;
        
        Commands commands = new Commands(KeyCode.getKeyCode(jObject.get("exit").getAsString()),
                KeyCode.getKeyCode(jObject.get("full_screen").getAsString()),
                KeyCode.getKeyCode(jObject.get("pause").getAsString()),
                KeyCode.getKeyCode(jObject.get("main_menu").getAsString()),
                KeyCode.getKeyCode(jObject.get("camera_scene").getAsString()),
                KeyCode.getKeyCode(jObject.get("camer_player1").getAsString()),
                KeyCode.getKeyCode(jObject.get("camer_player2").getAsString())
        );
        
        jObject = jObject.get("player1").getAsJsonObject();
        commands.setPlayer1(getPlayerCommands(jObject.get("player1").getAsJsonObject(), commands));
        commands.setPlayer1(getPlayerCommands(jObject.get("player2").getAsJsonObject(), commands));        
       return commands;
    }
    
    public Commands.PlayerCommands getPlayerCommands(JsonObject jObject, Commands commands){
        return commands.new PlayerCommands(
                KeyCode.getKeyCode(jObject.get("up").getAsString()),
                KeyCode.getKeyCode(jObject.get("down").getAsString()),
                KeyCode.getKeyCode(jObject.get("left").getAsString()),
                KeyCode.getKeyCode(jObject.get("right").getAsString()),
                KeyCode.getKeyCode(jObject.get("rotate_left").getAsString()),
                KeyCode.getKeyCode(jObject.get("rotate_right").getAsString()),
                KeyCode.getKeyCode(jObject.get("shoot").getAsString())                
        );
    }
}