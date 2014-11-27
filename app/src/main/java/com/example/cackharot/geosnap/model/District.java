package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class District implements BaseModel {
    public ObjectId _id;
    public ObjectId distributor_id;
    public String name;
    public List<String> centers;
    public List<Dealer> dealers;
    public Date created_at;
    public Boolean status;

    @Override
    public String toString() {
        return this.name;
    }

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
