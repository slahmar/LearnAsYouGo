package com.example.laygo.laygo.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laygo.laygo.R;
import com.example.laygo.laygo.activity.QuizResultActivity;
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
import java.util.Objects;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private String PREFS = "Settings";
    private static final String TEXT_TYPE = "Text";
    private static final String GALLERY_TYPE = "Gallery";


    private List<Question> questions;
    private List<Brick> bricks;
    private List<Question> allQuestions;
    private int currentQuestionID;
    private Question currentQuestion;
    private int score;
    private String givenAnswers, correctAnswers, quizType,
            askedQuestionsIDs, askedQuestions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Bundle extras = getIntent().getExtras();

        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        currentQuestionID = 0;
        score = 0;
        givenAnswers = "";
        correctAnswers = "";
        askedQuestionsIDs = "";
        askedQuestions = "";
        quizType = extras != null ? extras.getString("QUIZ_TYPE") : TEXT_TYPE; // default
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentQuestionID", currentQuestionID);
        editor.putInt("score", score);
        editor.putString("givenAnswers", givenAnswers);
        editor.putString("correctAnswers", correctAnswers);
        editor.putString("quizType", quizType);
        editor.putString("askedQuestionsIDs", askedQuestionsIDs);
        editor.putString("askedQuestions", askedQuestions);
        editor.apply();

        getQuestions();

        switch (quizType) {
            case TEXT_TYPE: (findViewById(R.id.quizBrickImage)).setVisibility(View.INVISIBLE); break;
            case GALLERY_TYPE: (findViewById(R.id.textViewQuizTitle)).setVisibility(View.INVISIBLE); break;
        }

        setQuestions();

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        currentQuestionID = settings.getInt("currentQuestionID", 0);
        score = settings.getInt("score", 0);
        givenAnswers = settings.getString("givenAnswers", "");
        correctAnswers = settings.getString("correctAnswers", "");
        quizType = settings.getString("quizType", TEXT_TYPE);
        askedQuestionsIDs = settings.getString("askedQuestionsIDs", askedQuestionsIDs);
        askedQuestions = settings.getString("askedQuestions", askedQuestions);
    }

    private void getQuestions() {
        QuestionDAO dao = new QuestionDAO(getApplicationContext());
        dao.open();
        allQuestions = dao.findAll();
        dao.close();

        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        List<Brick> bricks = bdao.findAll();

        switch (quizType) {
            case TEXT_TYPE:
                for (Question q : allQuestions)
                    for (Brick b : bricks)
                        if (q.getBrickID() == b.getId())
                            q.setBrick(b);
                break;
            case GALLERY_TYPE:
                List<Question> deleteQs = new LinkedList<>();
                for (Question q : allQuestions)
                    for (Brick b : bricks)
                        if (q.getBrickID() == b.getId() && b.getImage() != null && b.getImage().length() > 1)
                            q.setBrick(b);
                        else if (q.getBrickID() == b.getId() && (b.getImage() == null || b.getImage().length() <= 1))
                            deleteQs.add(q);

                allQuestions.removeAll(deleteQs);
                break;
        }
        bdao.close();

        questions = new LinkedList<>();
        Collections.sort(allQuestions);
        for (int i = 0;
             i < Math.min(allQuestions.size(), quizType.equals("Text")? Quiz.MAX_TEXTS:Quiz.MAX_PICTURES);
             questions.add(allQuestions.get(i)), ++i);

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

        for (i = 0; i < questions.size() - 1; ++i) {
            do {
                index = r.nextInt(allQuestions.size());
                tmp = allQuestions.get(index);
            } while (options.contains(tmp) || tmp.equals(currentQuestion));

            tmp.incAsked();
            options.add(tmp);
        }

        Collections.shuffle(options, new Random());

        rButtons.add((RadioButton) findViewById(R.id.radio0));
        rButtons.add((RadioButton) findViewById(R.id.radio1));
        rButtons.add((RadioButton) findViewById(R.id.radio2));
        next = (Button) findViewById(R.id.button1);

        i = 0;
        switch (quizType) {
            case TEXT_TYPE:
                ((TextView)findViewById(R.id.textViewQuizTitle))
                        .setText("What is the translation for " + currentQuestion + " ?");
                for (RadioButton rb : rButtons) {
                    rb.setText(options.get(i).getBrick().getTranslation());
                    ++i;
                }
                break;
            case GALLERY_TYPE:
                ((ImageView)findViewById(R.id.quizBrickImage))
                        .setImageBitmap(getImageFromPath(currentQuestion.getBrick().getImage()));
                for (RadioButton rb : rButtons) {
                    rb.setText(options.get(i).getBrick().getWord());
                    ++i;
                }
                break;
        }

        final Activity t = this;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup0);
                RadioButton selected = (RadioButton) findViewById(g.getCheckedRadioButtonId());
                if (selected == null) {
                    Toast.makeText(t, "You have to select an option", Toast.LENGTH_LONG).show();
                    return;
                }

                String correctAnswer = "";
                switch (quizType) {
                    case TEXT_TYPE:
                        correctAnswer = currentQuestion.getBrick().getTranslation();
                        break;
                    case GALLERY_TYPE:
                        correctAnswer = currentQuestion.getBrick().getWord();
                        break;
                }
                String givenAnswer = "" + selected.getText();

                givenAnswers += givenAnswer + "//";
                correctAnswers += correctAnswer + "//";
                askedQuestions += currentQuestion.getBrick().getWord() + "//";
                askedQuestionsIDs += currentQuestion.getID() + "//";
                if (givenAnswer.equals(correctAnswer)) {
                    score++;
                    currentQuestion.incCorrect();
                }
                if (currentQuestionID == questions.size())
                    setResults();
                else{
                    g.clearCheck();
                    setQuestions();
                }

            }
        });
    }

    private void setResults() {
        Intent i = new Intent(getApplicationContext(), QuizResultActivity.class);
        i.putExtra("SCORE", score);
        i.putExtra("NBQUESTIONS", questions.size());
        i.putExtra("GIVEN_ANSWERS", givenAnswers);
        i.putExtra("CORRECT_ANSWERS", correctAnswers);
        i.putExtra("QUIZ_TYPE", quizType);
        i.putExtra("ASKED_IDS", askedQuestionsIDs);
        i.putExtra("ASKED_QUESTIONS", askedQuestions);
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
        editor.putString("givenAnswers", givenAnswers);
        editor.putString("correctAnswers", correctAnswers);
        editor.putString("quizType", quizType);
        editor.putString("askedQuestionsIDs", askedQuestionsIDs);
        editor.putString("askedQuestions", askedQuestions);
        editor.apply();
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

