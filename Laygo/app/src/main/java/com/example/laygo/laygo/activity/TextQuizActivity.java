package com.example.laygo.laygo.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.laygo.laygo.R;
import com.example.laygo.laygo.activity.QuizResultActivity;
import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.dao.QuestionDAO;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;
import com.example.laygo.laygo.model.Quiz;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TextQuizActivity extends AppCompatActivity {
    private String PREFS = "Settings";

    private List<Question> questions;
    private List<Brick> bricks;
    private List<Question> allQuestions;
    private int currentQuestionID;
    private Question currentQuestion;
    private int score;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getQuestions();

        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        currentQuestionID = settings.getInt("currentQuestionID", 0);
        score = settings.getInt("score", 0);

        setQuestions();
    }

    private void getQuestions() {
        QuestionDAO dao = new QuestionDAO(getApplicationContext());
        dao.open();
        allQuestions = dao.findAll();
        dao.close();

        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        List<Brick> bricks = bdao.findAll();

        for (Question q : allQuestions)
            for (Brick b : bricks)
                if (q.getBrickID() == b.getId())
                    q.setBrick(b);

        bdao.close();

        questions = new LinkedList<>();
        Collections.sort(allQuestions);
        for (int i = 0;
             i < Math.min(allQuestions.size(), Quiz.MAX_TEXTS);
             questions.add(allQuestions.get(i)), ++i)
            ;

    }


    private void setQuestions() {
        List<RadioButton> rButtons;
        Button next;
        TextView tv;
        Random r;
        List<Question> options;
        Question tmp;
        int i, index;

        options = new LinkedList<>();
        rButtons = new LinkedList<>();
        r = new Random();

        currentQuestion = questions.get(currentQuestionID++);

        options.add(currentQuestion);
        //questions.remove(currentQuestion);
        for (i = 0; i < questions.size(); ++i) {
            do {
                index = r.nextInt(allQuestions.size());
                tmp = allQuestions.get(index);
            } while (options.contains(tmp) || tmp.equals(currentQuestion));

            tmp.incAsked();
            options.add(tmp);
        }

        Collections.shuffle(options, new Random());

        tv = (TextView) findViewById(R.id.textViewQuizTitle);
        rButtons.add((RadioButton) findViewById(R.id.radio0));
        rButtons.add((RadioButton) findViewById(R.id.radio1));
        rButtons.add((RadioButton) findViewById(R.id.radio2));
        next = (Button) findViewById(R.id.button1);

        i = 0;
        tv.setText("WORD: " + currentQuestion);
        for (RadioButton rb : rButtons) {
            rb.setText(options.get(i).getBrick().getTranslation());
            ++i;
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup0);
                RadioButton selected = (RadioButton) findViewById(g.getCheckedRadioButtonId());
                if (currentQuestion.getBrick().getTranslation().equals(selected.getText())) {
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

    private void setResults() {
        Intent i = new Intent(getApplicationContext(), QuizResultActivity.class);

        i.putExtra("SCORE", score);
        startActivity(i);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // don't do anything: the user shouldn't go back (otherwise
            // currentQuestion.incCorrect() may be called too much)
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentQuestionID", currentQuestionID);
        editor.putInt("score", score);
        editor.apply();
    }
}

