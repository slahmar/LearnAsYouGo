package com.example.laygo.laygo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.laygo.laygo.db.BrickSQLiteHelper;
import com.example.laygo.laygo.model.Brick;

import java.util.List;

public class BrickDAO {
        private SQLiteDatabase database;
        private BrickSQLiteHelper dbHelper;
        private String[] allColumns = { BrickSQLiteHelper.COLUMN_ID,
                BrickSQLiteHelper.COLUMN_WORD };

        // TODO : Implement all methods

        /**
         * Find all the bricks in the database
         * @return a list of bricks empty if none is found
         */
        public List<Brick> findAll(){
            return null;
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
            return null;
        }

        /**
         * Insert a brick in the database
         * @param brick the brick to insert
         * @pre brick shouldn't already be in the database, brick not null
         * @throws NullPointerException if brick is null
         * @throws IllegalStateException if brick is already in the database
         * @return true if the insertion has succeeded, else false
         */
        public boolean insertBrick(Brick brick) throws NullPointerException, IllegalStateException{
            return false;
        }

        public boolean updateBrick(Brick brick){
            return false;
        }

        public boolean deleteBrick(Brick brick){
            return false;
        }
}
