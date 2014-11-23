package com.example.cackharot.geosnap.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cackharot.geosnap.model.BaseModel;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.UUID;

public class BaseRepository<T extends BaseModel> implements Closeable {

    private final GeoSnapSQLiteHelper dbHelper;
    private final Class<T> tClass;

    public BaseRepository(Context context, Class<T> tClass) {
        this.tClass = tClass;
        dbHelper = new GeoSnapSQLiteHelper(context);
    }

    public UUID Create(T entity) {
        if (entity.getId() == null)
            entity.setId(UUID.randomUUID());
        ContentValues values = entity.getContentValues();
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(tClass.getSimpleName(), null, values);
        this.close();
        return entity.getId();
    }

    public T Get(UUID id) {
        T entity = getTInstance();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(tClass.getSimpleName(),
                entity.getAllColumns(),
                "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null)
            return null;
        cursor.moveToFirst();
        entity.setEntity(cursor);
        cursor.close();
        this.close();
        return entity;
    }

    private T getTInstance() {
        try {
            return tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<T> GetAll() {
        T entity = getTInstance();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(tClass.getSimpleName(), entity.getAllColumns(), null, null, null, null, null);
        if (cursor == null)
            return null;
        cursor.moveToFirst();
        ArrayList<T> entities = new ArrayList<T>();
        while (!cursor.isAfterLast()) {
            entity = getTInstance();
            entity.setEntity(cursor);
            entities.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        return entities;
    }

    public Integer getCount() {
        T entity = getTInstance();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(tClass.getSimpleName(),
                entity.getAllColumns(), null, null, null, null, null);
        if (cursor == null)
            return 0;
        Integer count = cursor.getCount();
        cursor.close();
        this.close();
        return count;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    private SQLiteDatabase getWritableDatabase() {
        return dbHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
    }
}
