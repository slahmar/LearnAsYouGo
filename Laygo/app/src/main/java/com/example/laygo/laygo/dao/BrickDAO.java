package com.example.laygo.laygo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.laygo.laygo.db.LaygoSQLiteHelper;
import com.example.laygo.laygo.model.Brick;

import java.util.ArrayList;
import java.util.List;

public class BrickDAO extends GenericDAO{
    private String[] allColumns = { LaygoSQLiteHelper.COLUMN_ID,
                LaygoSQLiteHelper.COLUMN_WORD };

    public BrickDAO(Context context) {
        super(context);
        dbHelper = new LaygoSQLiteHelper(context);
    }

        /**
         * Find all the bricks in the database
         * @return a list of bricks empty if none is found
         */
        public List<Brick> findAll(){
            List<Brick> bricks = new ArrayList<Brick>();
            Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BRICK,
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
            Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BRICK, allColumns,
                    LaygoSQLiteHelper.COLUMN_WORD +" = \"" + word +"\"", null, null, null, null);

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
            //if(findByWord(word).size() == 0) {
                ContentValues values = new ContentValues();
                values.put(LaygoSQLiteHelper.COLUMN_WORD, word);
                long insertId = database.insert(LaygoSQLiteHelper.TABLE_BRICK, null,
                        values);
                Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BRICK,
                        allColumns, LaygoSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                        null, null, null);
                cursor.moveToFirst();
                Brick newBrick = cursorToBrick(cursor);
                cursor.close();
                return newBrick;
            //}
            //else{
            //    throw new RuntimeException("Duplicate word");
            //}
        }

        private Brick cursorToBrick(Cursor cursor) {
            Brick brick = new Brick();
            brick.setId(cursor.getLong(0));
            brick.setWord(cursor.getString(1));
            brick.setTranslation(cursor.getString(2));
            return brick;
        }

        public boolean updateBrick(Brick brick){
            ContentValues values = new ContentValues();
            values.put(LaygoSQLiteHelper.COLUMN_WORD, brick.getWord());
            // TODO : add all columns
            return (database.update(LaygoSQLiteHelper.TABLE_BRICK, values, LaygoSQLiteHelper.COLUMN_ID + "=" + brick.getId(), null) > 0);
        }

        public boolean deleteBrick(Brick brick){
            long id = brick.getId();
            return (database.delete(LaygoSQLiteHelper.TABLE_BRICK, LaygoSQLiteHelper.COLUMN_ID + "=" + id, null) > 0);
        }
}