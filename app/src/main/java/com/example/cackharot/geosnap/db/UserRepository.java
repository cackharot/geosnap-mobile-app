package com.example.cackharot.geosnap.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cackharot.geosnap.lib.ConfigurationHelper;
import com.example.cackharot.geosnap.model.User;

import java.util.ArrayList;
import java.util.UUID;

public class UserRepository {

    private final GeoSnapSQLiteHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new GeoSnapSQLiteHelper(context);
    }

    public UUID Create(User entity) {
        entity.Id = UUID.randomUUID();
        ContentValues values = User.getContentValues(entity);
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(ConfigurationHelper.TABLE_USER, null, values);
        this.close();
        return entity.Id;
    }

    public User GetUser(UUID id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(ConfigurationHelper.TABLE_USER,
                User.getAllColumns(),
                "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null)
            return null;
        cursor.moveToFirst();
        User entity = User.getEntity(cursor);
        cursor.close();
        this.close();
        return entity;
    }

    public boolean ValidateUser(String name, String password) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(ConfigurationHelper.TABLE_USER,
                User.getAllColumns(),
                " name=? AND password=? ",
                new String[]{String.valueOf(name), String.valueOf(password)}, null, null, null);
        if (cursor == null)
            return false;
        Integer count = cursor.getCount();
        cursor.close();
        this.close();
        return count == 1;
    }

    public ArrayList<User> GetAllUsers() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(ConfigurationHelper.TABLE_USER, User.getAllColumns(), null, null, null, null, null);
        if (cursor == null)
            return null;
        cursor.moveToFirst();
        ArrayList<User> users = new ArrayList<User>();
        while (!cursor.isAfterLast()) {
            User entity = User.getEntity(cursor);
            users.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    private void close() {
        dbHelper.close();
    }

    private SQLiteDatabase getWritableDatabase() {
        return dbHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
    }

    public Integer getUserCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(ConfigurationHelper.TABLE_USER,
                User.getAllColumns(), null, null, null, null, null);
        if (cursor == null)
            return 0;
        Integer count = cursor.getCount();
        cursor.close();
        this.close();
        return count;
    }
}
