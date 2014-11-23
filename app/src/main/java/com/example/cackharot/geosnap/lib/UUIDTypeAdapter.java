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
import java.util.UUID;

public class UUIDTypeAdapter implements JsonSerializer<UUID>, JsonDeserializer<UUID> {
    public JsonElement serialize(UUID value, Type typeOfT, JsonSerializationContext context) {
        JsonPrimitive val = new JsonPrimitive(value.toString());
        JsonObject uuidObj = new JsonObject();
        uuidObj.add("$oid", val);
        return uuidObj;
    }

    public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String id = json.getAsJsonObject().get("$oid").getAsString();
        String uuid = id.replaceFirst(
                "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)",
                "$1-$2-$3-$4-$5");
        return UUID.fromString(uuid);
    }
}

