package settings.deserializers;

import com.google.gson.*;
import java.lang.reflect.Type;
import settings.Labels;

public class LabelsDeserializer implements JsonDeserializer<Labels>{

    @Override
    public Labels deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jObject = (JsonObject) je;
        
        Labels labs = Labels.getInstance();
        labs.setStart(jObject.get("start").getAsString());
        labs.setStart(jObject.get("final_score").getAsString());
        labs.setStart(jObject.get("victory").getAsString());
        labs.setStart(jObject.get("defeat").getAsString());
        labs.setStart(jObject.get("life").getAsString());
        labs.setStart(jObject.get("time").getAsString());
        labs.setStart(jObject.get("score").getAsString());
        labs.setStart(jObject.get("high_score").getAsString());

        return labs;
    }
}