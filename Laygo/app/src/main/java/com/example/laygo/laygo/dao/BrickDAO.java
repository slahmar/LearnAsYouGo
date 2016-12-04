package com.example.laygo.laygo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;

import com.example.laygo.laygo.db.LaygoSQLiteHelper;
import com.example.laygo.laygo.model.Brick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for the bricks
 */
public class BrickDAO extends GenericDAO{
    private String[] allColumns = { LaygoSQLiteHelper.COLUMN_ID,
                LaygoSQLiteHelper.COLUMN_WORD, LaygoSQLiteHelper.COLUMN_TRANSLATION,
                LaygoSQLiteHelper.COLUMN_EXAMPLES, LaygoSQLiteHelper.COLUMN_PHOTO, LaygoSQLiteHelper.COLUMN_AUDIO,
                LaygoSQLiteHelper.COLUMN_LATITUDE, LaygoSQLiteHelper.COLUMN_LONGITUDE};
    public BrickDAO(Context context) {
        super(context);
        dbHelper = new LaygoSQLiteHelper(context);
    }

    // Find all the bricks in the database
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

    // Find a brick by its id
    public List<Brick> findById(long id) throws NullPointerException {
        List<Brick> bricks = new ArrayList<Brick>();
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BRICK, allColumns,
                LaygoSQLiteHelper.COLUMN_ID +" = \"" + id +"\"", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Brick brick = cursorToBrick(cursor);
            bricks.add(brick);
            cursor.moveToNext();
        }
        cursor.close();
        return bricks;
    }

    // Find a brick by its word
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

    // Create a brick
    public Brick createBrick(String word) throws NullPointerException, IllegalStateException{
        if(findByWord(word).size() == 0) {
            ContentValues values = new ContentValues(4);
            values.put(LaygoSQLiteHelper.COLUMN_WORD, word);
            values.put(LaygoSQLiteHelper.COLUMN_TRANSLATION, "");
            values.put(LaygoSQLiteHelper.COLUMN_EXAMPLES, "");
            values.put(LaygoSQLiteHelper.COLUMN_PHOTO, "");
            values.put(LaygoSQLiteHelper.COLUMN_AUDIO, "");
            values.put(LaygoSQLiteHelper.COLUMN_LATITUDE, Double.MAX_VALUE);
            values.put(LaygoSQLiteHelper.COLUMN_LONGITUDE, Double.MAX_VALUE);
            long insertId = database.insert(LaygoSQLiteHelper.TABLE_BRICK, null,
                    values);
            Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BRICK,
                    allColumns, LaygoSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Brick newBrick = cursorToBrick(cursor);
            newBrick.setId(insertId);
            cursor.close();
            return newBrick;
        }
        else{
            throw new RuntimeException("Duplicate word");
        }
    }

    // Returns a brick from a cursor
    private Brick cursorToBrick(Cursor cursor) {
        Brick brick = new Brick();
        brick.setId(cursor.getLong(0));
        brick.setWord(cursor.getString(1));
        brick.setTranslation(cursor.getString(2));
        brick.setExamples(cursor.getString(3));
        brick.setImage(cursor.getString(4));
        brick.setRecording(cursor.getString(5));
        Location loc = new Location("");
        loc.setLatitude(cursor.getDouble(6));
        loc.setLongitude(cursor.getDouble(7));
        brick.setLocation(loc);
        return brick;
    }

    // Update a brick
    public boolean updateBrick(Brick brick){
        ContentValues values = new ContentValues(4);
        values.put(LaygoSQLiteHelper.COLUMN_WORD, brick.getWord());
        values.put(LaygoSQLiteHelper.COLUMN_TRANSLATION, brick.getTranslation());
        values.put(LaygoSQLiteHelper.COLUMN_EXAMPLES, brick.getExamples());
        values.put(LaygoSQLiteHelper.COLUMN_PHOTO, brick.getImage());
        values.put(LaygoSQLiteHelper.COLUMN_AUDIO, brick.getRecording());
        Location location = brick.getLocation();
        if(location == null){
            values.put(LaygoSQLiteHelper.COLUMN_LATITUDE, Double.MAX_VALUE);
            values.put(LaygoSQLiteHelper.COLUMN_LONGITUDE, Double.MAX_VALUE);
        }
        else{
            values.put(LaygoSQLiteHelper.COLUMN_LATITUDE, brick.getLocation().getLatitude());
            values.put(LaygoSQLiteHelper.COLUMN_LONGITUDE, brick.getLocation().getLongitude());
        }
        return (database.update(LaygoSQLiteHelper.TABLE_BRICK, values, LaygoSQLiteHelper.COLUMN_ID + "=" + brick.getId(), null) > 0);
    }

    // Delete a brick and its image and recording
    public boolean deleteBrick(Brick brick){
        long id = brick.getId();
        String imagePath = brick.getImage();
        if(imagePath != null && !imagePath.equals("")){
            File image = new File(imagePath);
            if(image.exists()){
                image.delete();
            }
        }

        String audioRecording = brick.getRecording();
        if(audioRecording != null && !audioRecording.equals("")){
            File recording = new File(audioRecording);
            if(recording.exists()){
                recording.delete();
            }
        }

        return (database.delete(LaygoSQLiteHelper.TABLE_BRICK, LaygoSQLiteHelper.COLUMN_ID + "=" + id, null) > 0);
    }
}