package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.example.cackharot.geosnap.lib.GsonHelper;
import com.google.gson.annotations.Expose;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class Site implements BaseModel {
    @Expose
    public ObjectId _id;
    public ObjectId district_id;
    public String name;
    public double consumption;
    public String address;
    public String rebars_considered;
    public Date created_at;
    public ArrayList<String> photos;
    public Boolean status;
    public Location location;
    public Boolean sync_status;
    public double square_feet;
    public ArrayList<Brand> used_brands;

    public Site() {
        this.location = new Location();
        this.used_brands = new ArrayList<Brand>();
    }

    public ContentValues getContentValues() {
        String jsonData = GsonHelper.getGson().toJson(this);
        jsonData = DatabaseUtils.sqlEscapeString(jsonData);
        ContentValues values = new ContentValues();
        values.put("id", _id.toString());
        values.put("json_data", jsonData);
        values.put("sync_status", sync_status);
        return values;
    }

    public String[] getAllColumns() {
        return new String[]{"id", "json_data", "sync_status"};
    }

    public void setEntity(Cursor cursor) {
        String jsonData = GsonHelper.unescapeSqlString(cursor.getString(1));
        Site entity = GsonHelper.getGson().fromJson(jsonData, Site.class);
        this._id = new ObjectId(cursor.getString(0));
        this.name = entity.name;
        this.consumption = entity.consumption;
        this.address = entity.address;
        this.rebars_considered = entity.rebars_considered;
        this.square_feet = entity.square_feet;
        this.created_at = entity.created_at;
        this.status = entity.status;
        this.photos = entity.photos;
        this.district_id = entity.district_id;
        this.location = entity.location;
        this.sync_status = Boolean.parseBoolean(cursor.getString(2));
    }

    @Override
    public ObjectId getId() {
        return _id;
    }

    @Override
    public void setId(ObjectId uuid) {
        this._id = uuid;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
