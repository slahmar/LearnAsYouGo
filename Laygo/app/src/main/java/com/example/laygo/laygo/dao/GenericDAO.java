package com.example.laygo.laygo.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.laygo.laygo.db.LaygoSQLiteHelper;

/**
 * Generic data access object
 */
public class GenericDAO {
    protected SQLiteDatabase database;
    protected LaygoSQLiteHelper dbHelper;

    public GenericDAO(Context context){
        dbHelper = new LaygoSQLiteHelper(context);
    }

    // Get a writable database
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Close the access to the database
    public void close() {
        dbHelper.close();
    }
}
