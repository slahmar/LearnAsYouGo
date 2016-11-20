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
import android.widget.Toast;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.model.Question;
import com.example.laygo.laygo.model.Quiz;

import java.util.LinkedList;
import java.util.List;

import static com.example.laygo.laygo.R.styleable.View;

public class ChooseQuiz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz);
    }

    public void onClickTextQuiz(View view) {
        List<Question> tmp = new LinkedList<>();
        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        List<Brick> bricks = bdao.findAll();
        for (Brick b : bricks) {
            tmp.add(new Question(b));
        }
        if (tmp.size() < Quiz.MIN_TEXTS) {
            Toast.makeText(this, "You don't have enough words!", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(this, TextQuizActivity.class);
            startActivity(intent);
        }
    }

    public void onClickPictureQuiz(View view) {
        List<Question> tmp = new LinkedList<>();
        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        List<Brick> bricks = bdao.findAll();
        for (Brick b : bricks) {
            if (b.getImage() == null || b.getImage().equals("")) continue;
            tmp.add(new Question(b));
        }

        if (tmp.size() < Quiz.MIN_PICTURES) {
            Toast.makeText(this, "You don't have enough words!", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(this, GalleryQuiz.class);
            startActivity(intent);
        }
    }


}
