package com.example.cackharot.geosnap.lib;

import java.util.Date;
import java.util.UUID;

public class ConfigurationHelper {
    public static final int CAMERA_REQUEST = 1888;
    public static String Host = "http://192.168.1.3:5500";
    public static String BaseUrl = Host + "/api";
    public static String SiteImageUrl = Host + "/static/images/sites/";
    public static String SiteImageUploadUrl = Host + "/upload_site_images";
    public static String LoginUrl = Host + "/validate_user";

    public static String Api_key = "TEtORDg5JiUjQCFOREZITEtE";


    public static final String TABLE_USER = "User";
    private static final String USER_CREATE_SCRIPT = "CREATE TABLE [User] (id VARCHAR NOT NULL, api_key VARCHAR NULL, name VARCHAR NOT NULL,"
            + "password VARCHAR NOT NULL, email TEXT NULL, question VARCHAR NULL, answer VARCHAR NULL,"
            + "created_at INTEGER NOT NULL, status INTEGER NOT NULL);";


    private static final String USER_DROP_SCRIPT = "DROP TABLE IF EXISTS User;";
    private static final String SITE_CREATE_SCRIPT = "CREATE TABLE [Site] (id VARCHAR NOT NULL, json_data TEXT NOT NULL, sync_status INTEGER NOT NULL);";
    private static final String SITE_DROP_SCRIPT = "DROP TABLE IF EXISTS Site;";
    public static final String DATABASE_NAME = "geoSnapDb.db";
    public static final int DB_VERSION = 8;
    public static final String[] DATABASE_CREATE_SCRIPT = new String[]{USER_CREATE_SCRIPT, SITE_CREATE_SCRIPT};
    public static final String[] DROP_TABLE_SCRIPT = new String[]{USER_DROP_SCRIPT, SITE_DROP_SCRIPT};
}
