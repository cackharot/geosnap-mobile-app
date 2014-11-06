package com.example.cackharot.geosnap.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;
import java.util.UUID;

public class User implements java.io.Serializable {
    public UUID Id;
    public String Name;
    public String Password;
    public String Email;
    public String SecurityQuestion;
    public String SecurityAnswer;
    public Date CreatedDate;
    public Boolean Status;

    public static ContentValues getContentValues(User entity) {
        ContentValues values = new ContentValues();
        values.put("id", entity.Id.toString());
        values.put("name", entity.Name);
        values.put("password", entity.Password);
        values.put("email", entity.Email);
        values.put("question", entity.SecurityQuestion);
        values.put("answer", entity.SecurityAnswer);
        values.put("created_date", entity.CreatedDate.getTime());
        values.put("status", entity.Status ? 1 : 0);
        return values;
    }

    public static String[] getAllColumns() {
        return new String[]{"id", "name", "password", "email", "question", "answer", "created_date", "status"};
    }

    public static User getEntity(Cursor cursor) {
        User entity = new User();
        entity.Id = UUID.fromString(cursor.getString(0));
        entity.Name = cursor.getString(1);
        entity.Password = cursor.getString(2);
        entity.Email = cursor.getString(3);
        entity.SecurityQuestion = cursor.getString(4);
        entity.SecurityAnswer = cursor.getString(5);
        entity.CreatedDate = new Date(cursor.getLong(6));
        entity.Status = Boolean.parseBoolean(cursor.getString(7));
        return entity;
    }
}
