package com.example.laygo.laygo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.laygo.laygo.db.BrickSQLiteHelper;
import com.example.laygo.laygo.model.Brick;

import java.util.ArrayList;
import java.util.List;

public class BrickDAO {
        private SQLiteDatabase database;
        private BrickSQLiteHelper dbHelper;
        private String[] allColumns = { BrickSQLiteHelper.COLUMN_ID,
                BrickSQLiteHelper.COLUMN_WORD };

    public BrickDAO(Context context) {
        dbHelper = new BrickSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

        /**
         * Find all the bricks in the database
         * @return a list of bricks empty if none is found
         */
        public List<Brick> findAll(){
            List<Brick> bricks = new ArrayList<Brick>();
            Cursor cursor = database.query(BrickSQLiteHelper.TABLE_BRICK,
                    allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Brick brick = cursorToBrick(cursor);
                bricks.add(brick);
                cursor.moveToNext();
            }
            cursor.close();
            return bricks;
        }

        /**
         * Find the bricks that have the id "id"
         * @param id
         * @pre id not null
         * @throws NullPointerException if id is null
         * @return a list of bricks empty if none is found
         */
        public List<Brick> findById(Integer id) throws NullPointerException {
            return null;
        }

        /**
         * Find the bricks that have the word "word"
         * @param word
         * @pre word not null
         * @throws NullPointerException if word is null
         * @return a list of bricks empty if none is found
         */
        public List<Brick> findByWord(String word) throws NullPointerException{
            List<Brick> bricks = new ArrayList<Brick>();
            Cursor cursor = database.query(BrickSQLiteHelper.TABLE_BRICK, allColumns,
                    BrickSQLiteHelper.COLUMN_WORD +" = \"" + word +"\"", null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Brick brick = cursorToBrick(cursor);
                bricks.add(brick);
                cursor.moveToNext();
            }
            cursor.close();
            return bricks;
        }

        /**
         * Insert a brick in the database
         * @param word the word of the brick
         * @pre a brick with this word shouldn't already be in the database, word not null
         * @throws NullPointerException if word is null
         * @throws IllegalStateException if a brick with this word is already in the database
         * @return the brick created
         */
        public Brick createBrick(String word) throws NullPointerException, IllegalStateException{
            if(findByWord(word).size() == 0) {
                ContentValues values = new ContentValues();
                values.put(BrickSQLiteHelper.COLUMN_WORD, word);
                long insertId = database.insert(BrickSQLiteHelper.TABLE_BRICK, null,
                        values);
                Cursor cursor = database.query(BrickSQLiteHelper.TABLE_BRICK,
                        allColumns, BrickSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                        null, null, null);
                cursor.moveToFirst();
                Brick newBrick = cursorToBrick(cursor);
                cursor.close();
                return newBrick;
            }
            else{
                throw new RuntimeException("Duplicate word");
            }
        }

        private Brick cursorToBrick(Cursor cursor) {
            Brick brick = new Brick();
            brick.setId(cursor.getLong(0));
            brick.setWord(cursor.getString(1));
            return brick;
        }

        public boolean updateBrick(Brick brick){
            ContentValues values = new ContentValues();
            values.put(BrickSQLiteHelper.COLUMN_WORD, brick.getWord());
            // TODO : add all columns
            return (database.update(BrickSQLiteHelper.TABLE_BRICK, values, BrickSQLiteHelper.COLUMN_ID + "=" + brick.getId(), null) > 0);
        }

        public boolean deleteBrick(Brick brick){
            long id = brick.getId();
            return (database.delete(BrickSQLiteHelper.TABLE_BRICK, BrickSQLiteHelper.COLUMN_ID + "=" + id, null) > 0);
        }
}
