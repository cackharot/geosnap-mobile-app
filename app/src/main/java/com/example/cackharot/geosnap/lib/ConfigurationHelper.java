package com.example.cackharot.geosnap.lib;

import java.util.Date;
import java.util.UUID;

public class ConfigurationHelper {
    public static final String TABLE_USER = "User";
    private static String USER_CREATE_SCRIPT = "CREATE TABLE User (id TEXT NOT NULL, name TEXT NOT NULL,"
            + "password TEXT NOT NULL, email TEXT NULL, question TEXT NULL, answer TEXT NULL,"
            + "created_date INTEGER NOT NULL, status INTEGER NOT NULL);\n";
    private static final String USER_DROP_SCRIPT = "DROP TABLE IF EXISTS User;\n";
    private static final String USER_INSERT_SCRIPT = "INSERT INTO User('" + UUID.randomUUID().toString() + "',"
            + "'john','pass@123','john@example.com', 'What is you first pet name?', 'Tiger', " + new Date().getTime() + " ,1);";

    public static final String DATABASE_NAME = "geoSnapDb.db";
    public static final int DB_VERSION = 1;
    public static final String DATABASE_CREATE_SCRIPT = USER_CREATE_SCRIPT + USER_INSERT_SCRIPT;
    public static final String DROP_TABLE_SCRIPT = USER_DROP_SCRIPT;
}
