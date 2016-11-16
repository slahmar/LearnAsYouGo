package com.example.laygo.laygo;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.laygo.laygo.model.Question;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentQuestionID;
    private Question currentQuestion;
    private int score;
    private final static int QUESTION_OPTIONS = 3;
    private final static int MAX_NUM_QUESTIONS = 10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getQuestions();
        currentQuestionID = score = 0;

        setQuestions();
    }

    private void getQuestions() {
        List<Question> tmp = null;
        // get all questions from the DB: tmp = ..
        Collections.sort(tmp);
        for (int i = 0; i < MAX_NUM_QUESTIONS; questions.add(tmp.get(i)), i++) ;
    }


    private void setQuestions() {
        List<RadioButton> rButtons;
        Button next;
        TextView tv;
        Random r;
        List<Question> options;
        Question tmp;

        options = new LinkedList<>();
        rButtons = new LinkedList<>();
        r = new Random();

        currentQuestion = questions.get(currentQuestionID++);

        options.add(currentQuestion);
        for (int i = 0; i < QUESTION_OPTIONS - 1; ++i) {
            tmp = questions.get(r.nextInt(questions.size()));
            tmp.incAsked();
            options.add(tmp);
        }
        Collections.shuffle(options, new Random());

        tv = (TextView) findViewById(R.id.textViewQuizTitle);
        rButtons.add((RadioButton) findViewById(R.id.radio0));
        rButtons.add((RadioButton) findViewById(R.id.radio1));
        rButtons.add((RadioButton) findViewById(R.id.radio2));
        next = (Button) findViewById(R.id.button1);

        // set images

        tv.setText("WORD: " + currentQuestion);
        for (RadioButton rb : rButtons)
            rb.setText(options.get(0).getBrick().getTranslation());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup1);
                RadioButton selected = (RadioButton) findViewById(g.getCheckedRadioButtonId());
                if (currentQuestion.getBrick().getTranslation().equals(selected.getText())) {
                    score++;
                    currentQuestion.incCorrect();
                }
                // if (currentQuestionID == questions.size() - 1) start result activity
                setQuestions();
            }
        });
    }
}
