package com.example.laygo.laygo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.laygo.laygo.HomeActivity;
import com.example.laygo.laygo.R;

public class QuizResultActivity extends AppCompatActivity {
    private String PREFS = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        int score = 0;
        int nbQuestions = 1;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("SCORE");
            nbQuestions = extras.getInt("NBQUESTIONS");
        }
        if (score < 0) throw new IllegalArgumentException("Score negative");

        String result = score + "/" + nbQuestions;
        TextView tv = (TextView) findViewById(R.id.scorePoints);
        tv.setText(result);
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
