package com.example.laygo.laygo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.laygo.laygo.R;
import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.dao.QuestionDAO;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;
import com.example.laygo.laygo.model.Quiz;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GalleryQuiz extends AppCompatActivity {

    private List<Question> questions;
    private List<Brick> bricks;
    private List<Question> allQuestions;
    private int currentQuestionID;
    private Question currentQuestion;
    private int score;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_quiz);

        getQuestions();
        currentQuestionID = score = 0;

        setQuestions();
    }

    private void getQuestions() {
        List<Question> deleteQs = new LinkedList<>();

        QuestionDAO dao = new QuestionDAO(getApplicationContext());
        dao.open();
        allQuestions = Collections.synchronizedList(dao.findAll());
        dao.close();

        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        List<Brick> bricks = Collections.synchronizedList(bdao.findAll());

        for (Question q : allQuestions)
            for (Brick b : bricks)
                if (q.getBrickID() == b.getId() && b.getImage() != null && b.getImage().length() > 1)
                    q.setBrick(b);
                else if (q.getBrickID() == b.getId() && (b.getImage() == null || b.getImage().length() <= 1))
                    deleteQs.add(q);

        allQuestions.removeAll(deleteQs);

        bdao.close();

        questions = new LinkedList<>();
        Collections.sort(allQuestions);
        for (int i = 0;
             i < Math.min(allQuestions.size(), Quiz.MAX_PICTURES);
             questions.add(allQuestions.get(i)), ++i)
            ;


    }


    private void setQuestions() {
        List<ImageButton> iButtons;
        TextView tv;
        Random r;
        List<Question> options;
        Question tmp;
        int i;
        int correctImageId = 0;

        options = new LinkedList<>();
        iButtons = new LinkedList<>();
        r = new Random();

        currentQuestion = questions.get(currentQuestionID++);

        options.add(currentQuestion);
        //questions.remove(currentQuestion);
        for (i = 0; i < questions.size(); ++i) {
            do {
                int index = r.nextInt(allQuestions.size());
                tmp = allQuestions.get(index);
            } while (options.contains(tmp) || tmp.equals(currentQuestion));

            tmp.incAsked();
            options.add(tmp);
        }

        Collections.shuffle(options, new Random());

        tv = (TextView) findViewById(R.id.galleryQuizQuestionText0);
        iButtons.add((ImageButton) findViewById(R.id.imageButton0));
        iButtons.add((ImageButton) findViewById(R.id.imageButton1));
        iButtons.add((ImageButton) findViewById(R.id.imageButton2));

        i = 0;
        String s = "WORD: " + currentQuestion;
        tv.setText(s);
        for (ImageButton ib : iButtons) {
            if (options.get(i).equals(currentQuestion)) correctImageId = i;
            ib.setImageBitmap(getImageFromPath(options.get(i).getBrick().getImage()));
            ++i;
        }

        for (ImageButton ib : iButtons) {
            final ImageButton button = ib;
            final int cImgId = correctImageId;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean correct = false;
                    switch (button.getId()) {
                        case R.id.imageButton0: if (cImgId == 0) correct = true; break;
                        case R.id.imageButton1: if (cImgId == 1) correct = true; break;
                        case R.id.imageButton2: if (cImgId == 2) correct = true; break;
                    }
                    if (correct) {
                        ++score;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // don't do anything: the user shouldn't go back (otherwise
            // currentQuestion.incCorrect() may be called too much)
            return true;
        }

        return super.onKeyDown(keyCode, event);
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