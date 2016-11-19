package com.example.laygo.laygo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QuizResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        int score = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("SCORE");
        }
        if (score < 0) throw new IllegalArgumentException("Score negative");

        String result = score + " points!";
        TextView tv = (TextView) findViewById(R.id.scorePoints);
        tv.setText(result);
    }
}
