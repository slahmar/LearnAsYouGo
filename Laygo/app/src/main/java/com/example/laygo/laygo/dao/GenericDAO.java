package com.example.laygo.laygo.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.laygo.laygo.db.LaygoSQLiteHelper;
import com.example.laygo.laygo.model.Block;

/**
 * Created by salom_000 on 09/11/2016.
 */

public class GenericDAO {
    protected SQLiteDatabase database;
    protected LaygoSQLiteHelper dbHelper;

    public GenericDAO(Context context){
        dbHelper = new LaygoSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}