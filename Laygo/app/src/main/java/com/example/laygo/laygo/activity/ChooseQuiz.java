package com.example.laygo.laygo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laygo.laygo.R;
import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;
import com.example.laygo.laygo.model.Quiz;

import java.util.LinkedList;
import java.util.List;

public class ChooseQuiz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz);
    }

    public void onClickTextQuiz(View view) {
        List<Question> tmp = new LinkedList<>();
        BrickDAO bdao = null;
        Intent intent = null;
        try {
            bdao = new BrickDAO(getApplicationContext());
            bdao.open();
            List<Brick> bricks = bdao.findAll();
            for (Brick b : bricks) {
                tmp.add(new Question(b));
            }
            if (tmp.size() < Quiz.MIN_TEXTS) {
                Toast.makeText(this, "You don't have enough words!", Toast.LENGTH_LONG).show();
            } else {
                intent = new Intent(this, QuizActivity.class);
            }
        } finally {if (bdao != null) bdao.close();}

        if (intent != null) {
            intent.putExtra("QUIZ_TYPE", "Text");
            startActivity(intent);
        }


    }

    public void onClickPictureQuiz(View view) {
        int counter = 0;

        BrickDAO bdao = null;
        Intent intent = null;

        try {
            bdao = new BrickDAO(getApplicationContext());
            bdao.open();
            List<Brick> bricks = bdao.findAll();
            for (Brick b : bricks) {
                if (b.getImage() == null || b.getImage().equals("")) continue;
                ++counter;
            }

            if (counter < Quiz.MIN_PICTURES) {
                Toast.makeText(this, "You don't have enough words!", Toast.LENGTH_LONG).show();
            } else {
                intent = new Intent(this, QuizActivity.class);
            }
        } finally {if (bdao != null) bdao.close();}

        if (intent != null) {
            intent.putExtra("QUIZ_TYPE", "Gallery");
            startActivity(intent);
        }
    }


}
