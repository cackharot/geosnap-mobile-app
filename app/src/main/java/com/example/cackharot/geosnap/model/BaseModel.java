package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.bson.types.ObjectId;


public interface BaseModel {
    ObjectId getId();
    ContentValues getContentValues();
    String[] getAllColumns();
    void setEntity(Cursor cursor);
    void setId(ObjectId uuid);
}
