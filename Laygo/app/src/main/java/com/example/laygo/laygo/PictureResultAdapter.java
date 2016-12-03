package com.example.laygo.laygo;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laygo.laygo.activity.ViewAndEditBrickActivity;

// Adapter used to display the results for the picture quiz
public class PictureResultAdapter extends ArrayAdapter<String> {

    private String[] photos;
    private String[] answers;
    private String[] correctAnswers;
    Context context;

    public PictureResultAdapter(Context context, String[] photos, String[] answers, String[] correctAnswers) {
        super(context, R.layout.picture_result, answers);
        this.photos = photos;
        this.answers = answers;
        this.correctAnswers = correctAnswers;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View rowView = mInflater.inflate(R.layout.picture_result, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.result);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageBrick);
        String answer = answers[position];
        String correctAnswer = correctAnswers[position];
        String result;
        if (answer.equals(correctAnswer)) {
            result = "<font color='#31B404'>You answered correctly : " + answer + "</font>";
        } else {
            result = "<font color='#DF0101'>You answered : " + answer + ", the right answer was : " + correctAnswer + "</font>";
        }
        textView.setText(Html.fromHtml(result));
        imageView.setImageBitmap(ViewAndEditBrickActivity.getResizedImage(photos[position], 200));

        return rowView;
    }
}