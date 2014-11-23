package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.bson.types.ObjectId;

import java.util.Date;

public class User implements java.io.Serializable, BaseModel {
    public ObjectId _id;
    public String name;
    public String password;
    public String email;
    public String security_question;
    public String security_answer;
    public Date created_at;
    public Boolean status;

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", this._id.toString());
        values.put("name", this.name);
        values.put("password", this.password);
        values.put("email", this.email);
        values.put("question", this.security_question);
        values.put("answer", this.security_answer);
        values.put("created_at", this.created_at.getTime());
        values.put("status", this.status ? 1 : 0);
        return values;
    }

    public String[] getAllColumns() {
        return new String[]{"id", "name", "password", "email", "question", "answer", "created_at", "status"};
    }

    public void setEntity(Cursor cursor) {
        User entity = this;
        entity._id = new ObjectId(cursor.getString(0));
        entity.name = cursor.getString(1);
        entity.password = cursor.getString(2);
        entity.email = cursor.getString(3);
        entity.security_question = cursor.getString(4);
        entity.security_answer = cursor.getString(5);
        entity.created_at = new Date(cursor.getLong(6));
        entity.status = Boolean.parseBoolean(cursor.getString(7));
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
        return this.email;
    }
}
