package settings.deserializers;

import com.google.gson.*;
import java.lang.reflect.Type;
import settings.*;

public class ConfigurationDeserializer implements JsonDeserializer<Configuration>{

    @Override
    public Configuration deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jObject = (JsonObject) je;
        return new Configuration(
                jObject.get("name").getAsString(),
                jObject.get("width").getAsDouble(),
                jObject.get("height").getAsDouble(),
                jObject.get("columns").getAsInt(),
                jObject.get("rows").getAsInt()
        );
    }
    
}
