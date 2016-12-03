package com.example.laygo.laygo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.laygo.laygo.HomeActivity;
import com.example.laygo.laygo.PictureResultAdapter;
import com.example.laygo.laygo.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Displays the result at the end of a quiz
 */
public class QuizResultActivity extends AppCompatActivity {
    private String givenAnswers, correctAnswers,askedQuestions;
    private ListView listView;
    private String quizType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        int score = 0;
        int nbQuestions = 1;
        String[] given, corrects, ids, asked;
        List<Spanned> results = new LinkedList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("SCORE");
            nbQuestions = extras.getInt("NBQUESTIONS");
            givenAnswers = extras.getString("GIVEN_ANSWERS");
            correctAnswers = extras.getString("CORRECT_ANSWERS");
            askedQuestions = extras.getString("ASKED_QUESTIONS");
            quizType = extras.getString("QUIZ_TYPE");
        }
        if (score < 0) throw new IllegalArgumentException("Score negative");

        String result = score + "/" + nbQuestions;
        TextView tv = (TextView) findViewById(R.id.scorePoints);
        tv.setText(result);
        listView = (ListView)findViewById(R.id.resultListView);

        given = givenAnswers.split("//");
        corrects = correctAnswers.split("//");
        asked = askedQuestions.split("//");

        ArrayAdapter adapter;
        if(quizType.equals(QuizActivity.TEXT_TYPE)){
            for (int i = 0; i < given.length; ++i) {
                String resultText = (i + 1) + ". ";
                if (given[i].equals(corrects[i])) {
                    resultText+="<font color='#31B404'>The word was : " + asked[i] + " and you answered correctly : " + given[i]+"</font>";
                } else {
                    resultText+="<font color='#DF0101'>The word was : " + asked[i] + ", you answered : " + given[i] + " and the right answer was : "+corrects[i]+"</font>";
                }
                results.add(Html.fromHtml(resultText));
            }
            adapter = new ArrayAdapter<Spanned>(this, android.R.layout.simple_list_item_1, results);
        }
        else{
            adapter = new PictureResultAdapter(this, asked, given, corrects);
        }

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
