package com.example.laygo.laygo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.laygo.laygo.HomeActivity;
import com.example.laygo.laygo.R;
import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.dao.QuestionDAO;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;
import com.example.laygo.laygo.model.Quiz;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Play a quiz
 */
public class QuizActivity extends AppCompatActivity {

    public static final String TEXT_TYPE = "Text";
    public static final String GALLERY_TYPE = "Gallery";
    public static final String STRING_SPLIT = "//";

    private String PREFS = "Settings";
    private List<Question> questions;
    private List<Question> allQuestions;
    private int currentQuestionID;
    private Question currentQuestion;
    private int score;
    private String givenAnswers, correctAnswers, quizType, askedQuestions;
    private ViewSwitcher switcher;

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
        askedQuestions = "";
        quizType = extras != null ? extras.getString("QUIZ_TYPE") : TEXT_TYPE;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentQuestionID", currentQuestionID);
        editor.putInt("score", score);
        editor.putString("givenAnswers", givenAnswers);
        editor.putString("correctAnswers", correctAnswers);
        editor.putString("quizType", quizType);
        editor.putString("askedQuestions", askedQuestions);
        editor.apply();
        getQuestions();
        setQuestions();
    }

    // Retrieve the questions and the bricks corresponding from the database
    private void getQuestions() {
        switcher = (ViewSwitcher) findViewById(R.id.switcher);

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
                        if (q == null || b == null) ;
                        else if (q.getBrickID() == b.getId() &&
                                b.getTranslation() != null && !b.getTranslation().equals(""))
                            q.setBrick(b);
                break;
            case GALLERY_TYPE:
                List<Question> deleteQs = new LinkedList<>();
                for (Question q : allQuestions)
                    for (Brick b : bricks)
                        if (q == null || b == null) ;
                        else if (q.getBrickID() == b.getId() &&
                                b.getImage() != null && b.getImage().length() > 1)
                            q.setBrick(b);
                        else if (q.getBrickID() == b.getId() &&
                                (b.getImage() == null || b.getImage().length() <= 1))
                            deleteQs.add(q);

                allQuestions.removeAll(deleteQs);
                switcher.showNext();
                break;
        }
        bdao.close();

        questions = new LinkedList<>();
        Collections.sort(allQuestions);
        for (int i = 0;
             i < Math.min(allQuestions.size(), quizType.equals(TEXT_TYPE) ? Quiz.MAX_TEXTS : Quiz.MAX_PICTURES);
             questions.add(allQuestions.get(i)), ++i)
            ;
    }

    // Set the design of the questions
    private void setQuestions() {
        List<RadioButton> rButtons = new LinkedList<>();
        Random r = new Random();
        List<Question> options = new LinkedList<>();
        Question tmp;
        int i, index;

        currentQuestion = questions.get(currentQuestionID++);
        options.add(currentQuestion);

        QuestionDAO dao = null;
        try {
            dao = new QuestionDAO(getApplicationContext());
            dao.open();

            for (i = 0; i < questions.size() - 1; ++i) {
                do {
                    index = r.nextInt(allQuestions.size());
                    tmp = allQuestions.get(index);
                } while (options.contains(tmp) || tmp.equals(currentQuestion));

                tmp.incAsked();
                options.add(tmp);
                if (tmp != null && tmp.getBrick() != null)
                    dao.updateQuestion(tmp);
            }
        } finally {
            if (dao != null) dao.close();
        }

        Collections.shuffle(options, new Random());
        rButtons.add((RadioButton) findViewById(R.id.radio0));
        rButtons.add((RadioButton) findViewById(R.id.radio1));
        rButtons.add((RadioButton) findViewById(R.id.radio2));

        i = 0;
        switch (quizType) {
            case TEXT_TYPE:
                ((TextView) switcher.findViewById(R.id.textViewQuizTitle))
                        .setText("What is the translation for " + currentQuestion + " ?");
                for (RadioButton rb : rButtons) {
                    rb.setText(options.get(i).getBrick().getTranslation());
                    ++i;
                }
                break;
            case GALLERY_TYPE:
                ((ImageView) switcher.findViewById(R.id.quizBrickImage))
                        .setImageBitmap(ViewAndEditBrickActivity.getResizedImage(currentQuestion.getBrick().getImage(), 200));
                for (RadioButton rb : rButtons) {
                    rb.setText(options.get(i).getBrick().getWord());
                    ++i;
                }
                break;
        }
    }

    // Start the result activity
    private void setResults() {
        Intent i = new Intent(getApplicationContext(), QuizResultActivity.class);
        i.putExtra("SCORE", score);
        i.putExtra("NBQUESTIONS", questions.size());
        i.putExtra("GIVEN_ANSWERS", givenAnswers);
        i.putExtra("CORRECT_ANSWERS", correctAnswers);
        i.putExtra("ASKED_QUESTIONS", askedQuestions);
        i.putExtra("QUIZ_TYPE", quizType);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        currentQuestionID = settings.getInt("currentQuestionID", 0);
        score = settings.getInt("score", 0);
        givenAnswers = settings.getString("givenAnswers", "");
        correctAnswers = settings.getString("correctAnswers", "");
        quizType = settings.getString("quizType", TEXT_TYPE);
        askedQuestions = settings.getString("askedQuestions", askedQuestions);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // don't do anything: the user shouldn't go back (otherwise
            // currentQuestion.incCorrect() may be called too many times)
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
        editor.putString("askedQuestions", askedQuestions);
        editor.apply();
    }

    // Go to next question or to the quiz result on click on the Next button
    public void onClickNextButton(View v) {
        RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup0);
        RadioButton selected = (RadioButton) findViewById(g.getCheckedRadioButtonId());
        if (selected == null) {
            Toast.makeText(this, "You have to select an option", Toast.LENGTH_LONG).show();
            return;
        }

        String correctAnswer = "";
        switch (quizType) {
            case TEXT_TYPE:
                correctAnswer = currentQuestion.getBrick().getTranslation();
                askedQuestions += currentQuestion.getBrick().getWord() + STRING_SPLIT;
                break;
            case GALLERY_TYPE:
                askedQuestions += currentQuestion.getBrick().getImage() + STRING_SPLIT;
                correctAnswer = currentQuestion.getBrick().getWord();
                break;
        }
        String givenAnswer = "" + selected.getText();
        givenAnswers += givenAnswer + STRING_SPLIT;
        correctAnswers += correctAnswer + STRING_SPLIT;
        if (givenAnswer.equals(correctAnswer)) {
            score++;
            currentQuestion.incCorrect();
        }
        QuestionDAO dao = null;
        try {
            dao = new QuestionDAO(getApplicationContext());
            dao.open();
            dao.updateQuestion(currentQuestion);
        } finally {
            if (dao != null) dao.close();
        }
        if (currentQuestionID == questions.size())
            setResults();
        else {
            g.clearCheck();
            setQuestions();
        }
    }

    // Go home when the home button is pressed
    public void backHome(View v) {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
    }
}

