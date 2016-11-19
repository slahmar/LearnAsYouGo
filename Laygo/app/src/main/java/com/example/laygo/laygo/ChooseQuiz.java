package com.example.laygo.laygo;

import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.example.laygo.laygo.R.styleable.View;

public class ChooseQuiz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz);
    }

    public void onClickTextQuiz(View view) {
        Intent intent = new Intent(this, TextQuizActivity.class);
        startActivity(intent);
    }

    public void onClickPictureQuiz(View view) {
        Intent intent = new Intent(this, GalleryQuiz.class);
        startActivity(intent);
    }


}
