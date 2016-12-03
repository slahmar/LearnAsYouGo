package com.example.laygo.laygo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.laygo.laygo.R;

/**
 * Help user to understand how the app works
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView help = (TextView) findViewById(R.id.helpText);
        help.setText(getText(R.string.help));
    }

}
