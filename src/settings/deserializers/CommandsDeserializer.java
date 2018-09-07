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
                KeyCode.getKeyCode(jObject.get("pause").getAsString()),
                KeyCode.getKeyCode(jObject.get("main_menu").getAsString()),
                KeyCode.getKeyCode(jObject.get("camera_scene").getAsString()),
                KeyCode.getKeyCode(jObject.get("camer_player").getAsString())           
        );
        
       return commands;
    }
}