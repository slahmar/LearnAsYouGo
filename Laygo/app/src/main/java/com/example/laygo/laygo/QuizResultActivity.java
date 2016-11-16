package com.example.laygo.laygo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QuizResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int score = extras.getInt("SCORE");
        }
    }
}
