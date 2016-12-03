package com.example.laygo.laygo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.laygo.laygo.db.LaygoSQLiteHelper;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for the questions
 */
public class QuestionDAO extends GenericDAO{
    private String[] allColumns = { LaygoSQLiteHelper.COLUMN_ID, LaygoSQLiteHelper.COLUMN_BRICK,
            LaygoSQLiteHelper.COLUMN_ASKED, LaygoSQLiteHelper.COLUMN_CORRECT, };
    protected LaygoSQLiteHelper dbHelper;
    public QuestionDAO(Context context) {
        super(context);
        dbHelper = new LaygoSQLiteHelper(context);
    }

    // Find all the questions in the database
    public List<Question> findAll(){
        List<Question> qs = new ArrayList<>();
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_QUESTION,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question q = cursorToQuestion(cursor);
            qs.add(q);
            cursor.moveToNext();
        }
        cursor.close();
        return qs;
    }

    // Find a question by its id
    public List<Question> findById(long id) throws NullPointerException {
        List<Question> qs = new ArrayList<>();
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_QUESTION, allColumns,
                LaygoSQLiteHelper.COLUMN_ID +" = \"" + id +"\"", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question q = cursorToQuestion(cursor);
            qs.add(q);
            cursor.moveToNext();
        }
        cursor.close();
        return qs;
    }

    // Find a question by its corresponding brick
    public List<Question> findByBrick(Brick b) throws NullPointerException{
        List<Question> qs = new ArrayList<>();
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_QUESTION, allColumns,
                LaygoSQLiteHelper.COLUMN_BRICK +" = \"" + b.getId() +"\"", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question q = cursorToQuestion(cursor);
            qs.add(q);
            cursor.moveToNext();
        }
        cursor.close();
        return qs;
    }

    // Create a question
    public Question createQuestion(long brickId) throws NullPointerException, IllegalStateException{
        ContentValues values = new ContentValues(4);
        values.put(LaygoSQLiteHelper.COLUMN_BRICK, brickId);
        values.put(LaygoSQLiteHelper.COLUMN_ASKED, 0);
        values.put(LaygoSQLiteHelper.COLUMN_CORRECT, 0);
        long insertId = database.insert(LaygoSQLiteHelper.TABLE_QUESTION, null,
                values);
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_QUESTION,
                allColumns, LaygoSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Question q = cursorToQuestion(cursor);
        q.setID(insertId);
        cursor.close();
        return q;
    }

    // Returns a question from a cursor
    private Question cursorToQuestion(Cursor cursor) {
        Question q = new Question();
        q.setID(cursor.getInt(0));
        q.setBrickID(cursor.getLong(1));
        q.setAsked(cursor.getInt(2));
        q.setCorrect(cursor.getInt(3));
        return q;
    }

    // Update a question
    public boolean updateQuestion(Question q){
        ContentValues values = new ContentValues(3);
        values.put(LaygoSQLiteHelper.COLUMN_BRICK, q.getBrick().getId());
        values.put(LaygoSQLiteHelper.COLUMN_ASKED, q.getAsked());
        values.put(LaygoSQLiteHelper.COLUMN_CORRECT, q.getCorrect());
        return (database.update(LaygoSQLiteHelper.TABLE_QUESTION, values,
                LaygoSQLiteHelper.COLUMN_ID + "=" + q.getID(), null) > 0);
    }

    // Delete a question
    public boolean deleteQuestion(Question q){
        long id = q.getID();
        return (database.delete(LaygoSQLiteHelper.TABLE_QUESTION, LaygoSQLiteHelper.COLUMN_ID + "=" + id, null) > 0);
    }

    // Delete a question from its corresponding brick id
    public boolean deleteQuestion(long brickId){
        return (database.delete(LaygoSQLiteHelper.TABLE_QUESTION, LaygoSQLiteHelper.COLUMN_BRICK + "=" + brickId, null) > 0);
    }
}