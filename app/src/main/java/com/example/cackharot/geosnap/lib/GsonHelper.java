package com.example.cackharot.geosnap.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.UUID;

public class GsonHelper {
    public static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        return builder.create();
    }
}