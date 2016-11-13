package com.example.laygo.laygo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.laygo.laygo.db.LaygoSQLiteHelper;
import com.example.laygo.laygo.model.Block;
import com.example.laygo.laygo.model.Brick;

import java.util.ArrayList;
import java.util.List;

public class BlockDAO extends GenericDAO {
    private String[] allColumns = {LaygoSQLiteHelper.COLUMN_ID,
            LaygoSQLiteHelper.COLUMN_NAME};

    public BlockDAO(Context context) {
        super(context);
    }

    /**
     * Find all the blocks in the database
     *
     * @return a list of blocks empty if none is found
     */
    public List<Block> findAll() {
        List<Block> blocks = new ArrayList<Block>();
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BLOCK,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Block block = cursorToBlock(cursor);
            blocks.add(block);
            cursor.moveToNext();
        }
        cursor.close();
        return blocks;
    }

    /**
     * Find the blocks that have the id "id"
     *
     * @param id
     * @return a list of blocks empty if none is found
     * @throws NullPointerException if id is null
     * @pre id not null
     */
    public List<Block> findById(Integer id) throws NullPointerException {
        return null;
    }

    /**
     * Find the blocks that have the name "name"
     *
     * @param name
     * @return a list of blocks empty if none is found
     * @throws NullPointerException if word is null
     * @pre word not null
     */
    public List<Block> findByName(String name) throws NullPointerException {
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BLOCK, allColumns,
                LaygoSQLiteHelper.COLUMN_WORD + " = \"" + name + "\"", null, null, null, null);
        return cursorToListOfBlocks(cursor);
    }

    public List<Block> cursorToListOfBlocks(Cursor cursor) {
        List<Block> blocks = new ArrayList<Block>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Block block = cursorToBlock(cursor);
            blocks.add(block);
            cursor.moveToNext();
        }
        cursor.close();
        return blocks;
    }

    /**
     * Insert a block in the database
     *
     * @param name the name of the block
     * @return the block created
     * @throws NullPointerException  if name is null
     * @throws IllegalStateException if a block with this name is already in the database
     * @pre a block with this name shouldn't already be in the database, word not null
     */
    public Block createBlock(String name) throws NullPointerException, IllegalStateException {
        //if(findByWord(word).size() == 0) {
        ContentValues values = new ContentValues();
        values.put(LaygoSQLiteHelper.COLUMN_NAME, name);
        long insertId = database.insert(LaygoSQLiteHelper.TABLE_BLOCK, null,
                values);
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BLOCK,
                allColumns, LaygoSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Block newBlock = cursorToBlock(cursor);
        cursor.close();
        return newBlock;
        //}
        //else{
        //    throw new RuntimeException("Duplicate word");
        //}
    }

    private Block cursorToBlock(Cursor cursor) {
        Block block = new Block();
        block.setId(cursor.getLong(0));
        block.setName(cursor.getString(1));
        return block;
    }

    public boolean updateBlock(Block block) {
        ContentValues values = new ContentValues();
        values.put(LaygoSQLiteHelper.COLUMN_WORD, block.getName());
        return (database.update(LaygoSQLiteHelper.TABLE_BLOCK, values, LaygoSQLiteHelper.COLUMN_ID + "=" + block.getId(), null) > 0);
    }

    public boolean deleteBlock(Block block) {
        long id = block.getId();
        return (database.delete(LaygoSQLiteHelper.TABLE_BLOCK, LaygoSQLiteHelper.COLUMN_ID + "=" + id, null) > 0);
    }
}