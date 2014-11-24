package com.example.cackharot.geosnap.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cackharot.geosnap.model.BaseModel;

import org.bson.types.ObjectId;

import java.io.Closeable;
import java.util.ArrayList;

public class BaseRepository<T extends BaseModel> implements Closeable {

    private final GeoSnapSQLiteHelper dbHelper;
    private final Class<T> tClass;

    public BaseRepository(Context context, Class<T> tClass) {
        this.tClass = tClass;
        dbHelper = new GeoSnapSQLiteHelper(context);
    }

    public ObjectId Create(T entity) {
        if (entity.getId() == null) {
            entity.setId(ObjectId.get());
            ContentValues values = entity.getContentValues();
            SQLiteDatabase database = this.getWritableDatabase();
            database.insert(tClass.getSimpleName(), null, values);
            this.close();
        } else {
            Update(entity);
        }
        return entity.getId();
    }

    public void Update(T entity) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = entity.getContentValues();
        database.update(tClass.getSimpleName(), values,
                "id = ?",
                new String[]{String.valueOf(entity.getId())});
        this.close();
    }

    public T Get(ObjectId id) {
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
        dbHelper.close();
    }

    private SQLiteDatabase getWritableDatabase() {
        return dbHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
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
}
