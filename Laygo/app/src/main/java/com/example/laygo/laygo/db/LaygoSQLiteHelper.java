package com.example.laygo.laygo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LaygoSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_BRICK = "brick";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_TRANSLATION = "translation";

    public static final String TABLE_BLOCK = "block";
    public static final String COLUMN_NAME = "name";

    private static final String DATABASE_NAME = "brick.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_BRICK = "create table "
            + TABLE_BRICK + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_WORD
            + " text not null, "+ COLUMN_TRANSLATION +" );";

    private static final String CREATE_TABLE_BLOCK = "create table "
            + TABLE_BLOCK + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null);";

    public LaygoSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_BRICK);
        database.execSQL(CREATE_TABLE_BLOCK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LaygoSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRICK);
        onCreate(db);
    }
}