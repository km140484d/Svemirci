package settings.deserializers;

import com.google.gson.*;
import java.lang.reflect.Type;
import javafx.scene.input.KeyCode;
import settings.Commands;

public class CommandsDeserializer implements JsonDeserializer<Commands>{

    @Override
    public Commands deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jObject = (JsonObject) je;
        
       return new Commands(KeyCode.getKeyCode(jObject.get("exit").getAsString()), 
                    KeyCode.getKeyCode(jObject.get("pause").getAsString()), 
                    KeyCode.getKeyCode(jObject.get("main_menu").getAsString()),
                    KeyCode.getKeyCode(jObject.get("camera_scene").getAsString()),
                    KeyCode.getKeyCode(jObject.get("camer_player").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player1_up").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player1_down").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player1_left").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player1_right").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player1_rotate_left").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player1_rotate_right").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player1_shoot").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player2_up").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player2_down").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player2_left").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player2_right").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player2_rotate_left").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player2_rotate_right").getAsString()),
                    KeyCode.getKeyCode(jObject.get("player2_shoot").getAsString()));
    }
}