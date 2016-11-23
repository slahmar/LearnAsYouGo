package com.example.laygo.laygo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;

import com.example.laygo.laygo.db.LaygoSQLiteHQuestion;
import com.example.laygo.laygo.db.LaygoSQLiteHelper;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDAO extends GenericDAO{
    private String[] allColumns = { LaygoSQLiteHQuestion.COLUMN_ID, LaygoSQLiteHQuestion.COLUMN_BRICK,
                                    LaygoSQLiteHQuestion.COLUMN_ASKED, LaygoSQLiteHQuestion.COLUMN_CORRECT, };
    protected LaygoSQLiteHQuestion dbHelper;
    public QuestionDAO(Context context) {
        super(context);
        dbHelper = new LaygoSQLiteHQuestion(context);
    }

    /**
     * Find all the questions in the database
     * @return a list of questions; empty if none is found
     */
    public List<Question> findAll(){
        List<Question> qs = new ArrayList<>();
        Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BRICK,
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

    /**
     * Find the questions that have the id "id"
     * @param id
     * @pre id not null
     * @throws NullPointerException if id is null
     * @return a list of bricks empty if none is found
     */
    public List<Question> findById(long id) throws NullPointerException {
        List<Question> qs = new ArrayList<>();
        Cursor cursor = database.query(LaygoSQLiteHQuestion.TABLE_QUESTION, allColumns,
                LaygoSQLiteHQuestion.COLUMN_ID +" = \"" + id +"\"", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question q = cursorToQuestion(cursor);
            qs.add(q);
            cursor.moveToNext();
        }
        cursor.close();
        return qs;
    }

    /**
     * Find the bricks that have the word "word"
     * @param b
     * @pre b not null
     * @throws NullPointerException if word is null
     * @return a list of bricks empty if none is found
     */
    public List<Question> findByBrick(Brick b) throws NullPointerException{
        List<Question> qs = new ArrayList<>();
        Cursor cursor = database.query(LaygoSQLiteHQuestion.TABLE_QUESTION, allColumns,
                LaygoSQLiteHQuestion.COLUMN_BRICK +" = \"" + b.getId() +"\"", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question q = cursorToQuestion(cursor);
            qs.add(q);
            cursor.moveToNext();
        }
        cursor.close();
        return qs;
    }

    /**
     * Insert a brick in the database
     * @param b the word of the brick
     * @pre a question with this brick shouldn't already be in the database, b not null
     * @throws NullPointerException if word is null
     * @throws IllegalStateException if a brick with this word is already in the database
     * @return the brick created
     */
    public Question createQuestion(Brick b) throws NullPointerException, IllegalStateException{
        if(findByBrick(b).size() == 0) {
            ContentValues values = new ContentValues(4);
            values.put(LaygoSQLiteHQuestion.COLUMN_BRICK, b.getId());
            values.put(LaygoSQLiteHQuestion.COLUMN_ASKED, 0);
            values.put(LaygoSQLiteHQuestion.COLUMN_CORRECT, 0);
            long insertId = database.insert(LaygoSQLiteHQuestion.TABLE_QUESTION, null,
                    values);
            Cursor cursor = database.query(LaygoSQLiteHelper.TABLE_BRICK,
                    allColumns, LaygoSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Question q = cursorToQuestion(cursor);
            q.setID(insertId);
            cursor.close();
            return q;
        }
        else{
            throw new RuntimeException("Duplicate question");
        }
    }

    private Question cursorToQuestion(Cursor cursor) {
        Question q = new Question();
        q.setID(cursor.getInt(0));
        q.setBrickID(cursor.getInt(1));
        q.setAsked(cursor.getInt(2));
        q.setCorrect(cursor.getInt(3));

        return q;
    }

    public boolean updateQuestion(Question q){
        ContentValues values = new ContentValues(4);
        values.put(LaygoSQLiteHQuestion.COLUMN_BRICK, q.getBrick().getId());
        values.put(LaygoSQLiteHQuestion.COLUMN_ASKED, 0);
        values.put(LaygoSQLiteHQuestion.COLUMN_CORRECT, 0);
        return (database.update(LaygoSQLiteHQuestion.TABLE_QUESTION, values,
                LaygoSQLiteHQuestion.COLUMN_ID + "=" + q.getID(), null) > 0);
    }

    public boolean deleteQuestion(Question q){
        long id = q.getID();
        return (database.delete(LaygoSQLiteHQuestion.TABLE_QUESTION, LaygoSQLiteHQuestion.COLUMN_ID + "=" + id, null) > 0);
    }
}