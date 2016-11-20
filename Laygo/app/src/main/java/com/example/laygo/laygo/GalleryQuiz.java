package com.example.laygo.laygo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GalleryQuiz extends AppCompatActivity {

    private List<Question> questions;
    private int currentQuestionID;
    private Question currentQuestion;
    private int score;
    private final static int QUESTION_OPTIONS = 3;
    private final static int MAX_NUM_QUESTIONS = Quiz.MIN_PICTURES;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getQuestions();
        currentQuestionID = score = 0;

        setQuestions();
    }

    private void getQuestions() {
        List<Question> tmp = null;
        tmp = new LinkedList<>();

        /// DELETE
        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        List<Brick> bricks = bdao.findAll();
        for (Brick b : bricks) {
            if (b.getImage() != null) // IMPORTANT
                tmp.add(new Question(b));
        }
        ///

        // get all questions from the DB: tmp = ..
        Collections.sort(tmp);
        for (int i = 0; i < MAX_NUM_QUESTIONS; questions.add(tmp.get(i)), i++) ;
    }


    private void setQuestions() {
        List<ImageButton> iButtons;
        Button next;
        TextView tv;
        Random r;
        List<Question> options;
        Question tmp;
        int i;
        final long currentCorrectBrickID;

        options = new LinkedList<>();
        iButtons = new LinkedList<>();
        r = new Random();

        currentQuestion = questions.get(currentQuestionID++);

        options.add(currentQuestion);
        for (i = 0; i < QUESTION_OPTIONS - 1; ++i) {
            tmp = questions.get(r.nextInt(questions.size()));
            tmp.incAsked();
            options.add(tmp);
        }
        Collections.shuffle(options, new Random());

        tv = (TextView) findViewById(R.id.galleryQuizQuestionText);
        iButtons.add((ImageButton) findViewById(R.id.imageButton0));
        iButtons.add((ImageButton) findViewById(R.id.imageButton1));
        iButtons.add((ImageButton) findViewById(R.id.imageButton2));



        i = 0;
        tv.setText("WORD: " + currentQuestion);
        for (ImageButton button : iButtons) {
            Brick brick = options.get(i++).getBrick();
            button.setImageBitmap(getImageFromPath(brick.getImage()));
            button.setContentDescription(brick.getTranslation());

            final ImageButton buttonOnClick = button;
            buttonOnClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (buttonOnClick.getContentDescription().equals(currentQuestion.getBrick().getTranslation())) {
                        score++;
                        currentQuestion.incCorrect();
                    }
                    if (currentQuestionID == questions.size() - 1)
                        setResults();
                    else
                        setQuestions();
                }
            });
        }
    }

    private void setResults() {
        Intent i = new Intent(getApplicationContext(), QuizResultActivity.class);

        i.putExtra("SCORE", score);
        startActivity(i);

    }

    private Bitmap getImageFromPath(String path) {
        BitmapFactory.Options ops;
        Bitmap bitmap;
        File file;
        try {
            file = new File(path);
            ops = new BitmapFactory.Options();
            ops.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, ops);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
