package com.example.laygo.laygo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LaygoSQLiteHQuestion extends SQLiteOpenHelper {
    public static final String TABLE_QUESTION = "question";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BRICK = "brickID";
    public static final String COLUMN_ASKED = "nAsked";
    public static final String COLUMN_CORRECT = "nCorrect";


    private static final String DATABASE_NAME = "question.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_BRICK = "create table "
            + TABLE_QUESTION + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_BRICK
            + " integer not null, "+ COLUMN_ASKED +" integer, " + COLUMN_CORRECT + " integer);";


    public LaygoSQLiteHQuestion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_BRICK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LaygoSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        onCreate(db);
    }
}
