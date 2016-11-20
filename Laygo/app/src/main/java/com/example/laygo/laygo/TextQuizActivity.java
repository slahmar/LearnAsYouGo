package com.example.laygo.laygo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;
import com.example.laygo.laygo.model.Quiz;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TextQuizActivity extends AppCompatActivity {

    private List<Question> questions;
    private List<Brick> bricks;
    private List<Question> allQuestions;
    private int currentQuestionID;
    private Question currentQuestion;
    private int score;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getQuestions();
        currentQuestionID = score = 0;

        setQuestions();
    }

    private void getQuestions() {
        allQuestions = new LinkedList<>();

        /// DELETE
        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        bricks = bdao.findAll();
        for (Brick b : bricks) {
            allQuestions.add(new Question(b));
        }
        ///

        // get all questions from the DB: tmp = ..
        questions = new LinkedList<>();
        Collections.sort(allQuestions);
        for (int i = 0; i < Math.min(allQuestions.size(), Quiz.MAX_TEXTS); questions.add(allQuestions.get(i)), i++) ;
    }


    private void setQuestions() {
        List<RadioButton> rButtons;
        Button next;
        TextView tv;
        Random r;
        List<Question> options;
        Question tmp;
        int i;

        options = new LinkedList<>();
        rButtons = new LinkedList<>();
        r = new Random();

        currentQuestion = questions.get(currentQuestionID++);

        options.add(currentQuestion);
        allQuestions.remove(currentQuestion);
        for (i = 0; i < questions.size() - 1; ++i) {
            int index = r.nextInt(allQuestions.size());
            tmp = allQuestions.get(index);
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
            rb.setText(options.get(i++).getBrick().getTranslation());
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup0);
                RadioButton selected = (RadioButton) findViewById(g.getCheckedRadioButtonId());
                String text = selected.getText() + "";
                Log.e("Selected RadioButton: ", text);
                if (currentQuestion.getBrick().getTranslation().equals(text)) {
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

}

