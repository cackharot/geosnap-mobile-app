package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.bson.types.ObjectId;

import java.util.Date;

public class Dealer implements BaseModel {
    public ObjectId _id;
    public ObjectId district_id;
    public String name;
    public String contact_name;
    public String phone;
    public String email;
    public String address;
    public String center;
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
