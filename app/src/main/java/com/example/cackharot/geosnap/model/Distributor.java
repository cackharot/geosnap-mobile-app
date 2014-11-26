package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.bson.types.ObjectId;

import java.util.List;

public class Distributor implements BaseModel {
    public ObjectId _id;
    public String name;
    public String contact_name;
    public String mobile;
    public String territory;
    public List<District> districts;

    @Override
    public ObjectId getId() {
        return _id;
    }

    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public String[] getAllColumns() {
        return new String[0];
    }

    @Override
    public void setEntity(Cursor cursor) {

    }

    @Override
    public void setId(ObjectId uuid) {
        _id = uuid;
    }
}
