package com.example.cackharot.geosnap.lib;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    public JsonElement serialize(Date value, Type typeOfT, JsonSerializationContext context) {
        JsonPrimitive val = new JsonPrimitive(value.getTime());
        JsonObject uuidObj = new JsonObject();
        uuidObj.add("$date", val);
        return uuidObj;
    }

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Long id = json.getAsJsonObject().get("$date").getAsLong();
        return new Date(id);
    }
}
