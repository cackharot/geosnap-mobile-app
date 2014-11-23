package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.cackharot.geosnap.lib.GsonHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Site implements BaseModel {
    public UUID _id;
    public UUID district_id;
    public String name;
    public double consumption;
    public String address;
    public String rebars_considered;
    public Date created_at;
    public ArrayList<String> photos;
    public Boolean status;
    public Location location;
    public Boolean sync_status;

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("_id", _id.toString());
        values.put("values", GsonHelper.getGson().toJson(this));
        values.put("sync_status", sync_status);
        return values;
    }

    public String[] getAllColumns() {
        return new String[]{"_id", "values", "sync_status"};
    }

    public void setEntity(Cursor cursor) {
        String jsonStr = cursor.getString(1);
        Site entity = GsonHelper.getGson().fromJson(jsonStr, Site.class);
        this._id = UUID.fromString(cursor.getString(0));
        this.name = entity.name;
        this.consumption = entity.consumption;
        this.address = entity.address;
        this.rebars_considered = entity.rebars_considered;
        this.created_at = entity.created_at;
        this.status = entity.status;
        this.photos = entity.photos;
        this.district_id = entity.district_id;
        this.location = entity.location;
        this.sync_status = Boolean.parseBoolean(cursor.getString(2));
    }

    @Override
    public UUID getId() {
        return _id;
    }

    @Override
    public void setId(UUID uuid) {
        this._id = uuid;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
