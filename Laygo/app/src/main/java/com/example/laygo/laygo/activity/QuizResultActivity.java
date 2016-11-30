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

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        score = settings.getInt("score", 0);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("SCORE");
        }
        if (score < 0) throw new IllegalArgumentException("Score negative");

        String result = score + " point" + (score == 1 ? "!" : "s!");
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

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("score", score);
        editor.apply();
    }
}
