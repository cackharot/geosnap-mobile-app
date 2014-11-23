package com.example.cackharot.geosnap.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.UUID;

public class GsonHelper {
    public static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
        builder.registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter());
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        return builder.create();
    }

    public static String unescapeSqlString(String string) {
        if (string == null)
            return null;

        StringBuilder sb = new StringBuilder(string);

        sb = sb.deleteCharAt(0);
        sb = sb.deleteCharAt(sb.length() - 1);

        return sb.toString().replaceAll("''", "'");
    }
}