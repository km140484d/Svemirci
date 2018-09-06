package settings.deserializers;

import com.google.gson.*;
import java.lang.reflect.Type;
import settings.Labels;

public class LabelsDeserializer implements JsonDeserializer<Labels>{

    @Override
    public Labels deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jObject = (JsonObject) je;
        
       return new Labels(jObject.get("start").getAsString(), 
                    jObject.get("victory").getAsString(),
                    jObject.get("defeat").getAsString(),
                    jObject.get("time").getAsString(),
                    jObject.get("score").getAsString(),
                    jObject.get("high_score").getAsString());
    }
}