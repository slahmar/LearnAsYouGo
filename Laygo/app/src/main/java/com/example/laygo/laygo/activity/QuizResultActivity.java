package com.example.laygo.laygo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.laygo.laygo.HomeActivity;
import com.example.laygo.laygo.R;
import java.util.LinkedList;
import java.util.List;

/**
 * Displays the result at the end of a quiz
 */
public class QuizResultActivity extends AppCompatActivity {
    private String PREFS = "Settings";
    private String givenAnswers, correctAnswers, askedQuestionsIDs, askedQuestions;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        int score = 0;
        int nbQuestions = 1;
        String[] given, corrects, ids, asked;
        List<String> results = new LinkedList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("SCORE");
            nbQuestions = extras.getInt("NBQUESTIONS");
            givenAnswers = extras.getString("GIVEN_ANSWERS");
            correctAnswers = extras.getString("CORRECT_ANSWERS");
            askedQuestionsIDs = extras.getString("ASKED_IDS");
            askedQuestions = extras.getString("ASKED_QUESTIONS");
        }
        if (score < 0) throw new IllegalArgumentException("Score negative");

        String result = score + "/" + nbQuestions;
        TextView tv = (TextView) findViewById(R.id.scorePoints);
        tv.setText(result);
        listView = (ListView)findViewById(R.id.resultListView);

        given = givenAnswers.split("//");
        corrects = correctAnswers.split("//");
        ids = askedQuestionsIDs.split("//");
        asked = askedQuestions.split("//");

        for (int i = 0; i < given.length; ++i) {
            String resultText = ""+(i+1);
            if (given[i].equals(corrects[i])) {
                resultText+="The word was : " + asked[i] + " and you answered correctly : " + given[i];
            } else {
                resultText+="The word was : " + asked[i] + ", you answered : " + given[i] + " and the right answer was : "+corrects[i];
            }
            results.add(resultText);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
