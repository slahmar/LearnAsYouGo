package com.example.laygo.laygo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;

import java.util.List;

public class MainActivity extends Activity {
    private BrickDAO datasource;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datasource = new BrickDAO(this);
        datasource.open();

        Brick brick = datasource.createBrick("game");
        Log.d("DEBUG", brick.toString());
        brick.setTranslation("jeu");
        assert(datasource.updateBrick(brick));

        List<Brick> values = datasource.findAll();
        for(Brick b : values){
            Log.d("DEBUG", b.toString());
        }
        setContentView(R.layout.activity_main);
    }
}

