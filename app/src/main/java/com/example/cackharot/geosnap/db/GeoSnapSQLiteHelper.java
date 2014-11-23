package com.example.cackharot.geosnap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cackharot.geosnap.lib.ConfigurationHelper;

public class GeoSnapSQLiteHelper extends SQLiteOpenHelper {

    public GeoSnapSQLiteHelper(Context context) {
        super(context, ConfigurationHelper.DATABASE_NAME, null, ConfigurationHelper.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.w(UserRepository.class.getName(), "Creating database version " + ConfigurationHelper.DB_VERSION);
        for (String script : ConfigurationHelper.DATABASE_CREATE_SCRIPT) {
            sqLiteDatabase.execSQL(script);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(UserRepository.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        for (String script : ConfigurationHelper.DROP_TABLE_SCRIPT) {
            sqLiteDatabase.execSQL(script);
        }
        onCreate(sqLiteDatabase);
    }
}
