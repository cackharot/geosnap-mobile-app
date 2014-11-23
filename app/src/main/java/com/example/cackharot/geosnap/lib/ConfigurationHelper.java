package com.example.cackharot.geosnap.lib;

import java.util.Date;
import java.util.UUID;

public class ConfigurationHelper {
    public static String BaseUrl = "http://192.168.1.3:5500/api";
    public static String Api_key = "TEtORDg5JiUjQCFOREZITEtE";

    public static final String TABLE_USER = "User";

    private static String USER_CREATE_SCRIPT = "CREATE TABLE User (id TEXT NOT NULL, name TEXT NOT NULL,"
            + "password TEXT NOT NULL, email TEXT NULL, question TEXT NULL, answer TEXT NULL,"
            + "created_at INTEGER NOT NULL, status INTEGER NOT NULL);\n";
    private static final String USER_DROP_SCRIPT = "DROP TABLE IF EXISTS User;\n";


    private static String SITE_CREATE_SCRIPT = "CREATE TABLE Site (id TEXT NOT NULL, values TEXT NOT NULL, sync_status INTEGER NOT NULL);\n";
    private static final String SITE_DROP_SCRIPT = "DROP TABLE IF EXISTS Site;\n";
    public static final String DATABASE_NAME = "geoSnapDb.db";
    public static final int DB_VERSION = 3;
    public static final String DATABASE_CREATE_SCRIPT = USER_CREATE_SCRIPT + SITE_CREATE_SCRIPT;
    public static final String DROP_TABLE_SCRIPT = USER_DROP_SCRIPT + SITE_DROP_SCRIPT;
}
