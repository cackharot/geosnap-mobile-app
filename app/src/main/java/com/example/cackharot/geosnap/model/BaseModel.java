package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public interface BaseModel {
    UUID getId();
    ContentValues getContentValues();
    String[] getAllColumns();
    void setEntity(Cursor cursor);
    void setId(UUID uuid);
}
