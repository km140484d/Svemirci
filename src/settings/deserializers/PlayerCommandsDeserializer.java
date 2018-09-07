package settings.deserializers;

import com.google.gson.*;
import java.lang.reflect.Type;
import javafx.scene.input.*;
import settings.*;

public class PlayerCommandsDeserializer implements JsonDeserializer<PlayerCommands>{

    @Override
    public PlayerCommands deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jObject = (JsonObject)je;
        return new PlayerCommands(
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
