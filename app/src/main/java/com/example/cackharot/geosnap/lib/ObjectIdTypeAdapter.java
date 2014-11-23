package com.example.cackharot.geosnap.lib;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.bson.types.ObjectId;

import java.lang.reflect.Type;

public class ObjectIdTypeAdapter implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {
    public JsonElement serialize(ObjectId value, Type typeOfT, JsonSerializationContext context) {
        if (value == null)
            return new JsonPrimitive("");
        JsonPrimitive val = new JsonPrimitive(value.toString());
        JsonObject uuidObj = new JsonObject();
        uuidObj.add("$oid", val);
        return uuidObj;
    }

    public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String id = json.getAsJsonObject().get("$oid").getAsString();
        return new ObjectId(id);
    }
}
