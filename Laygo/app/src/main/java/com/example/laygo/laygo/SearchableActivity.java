package com.example.laygo.laygo;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;

import java.util.List;
import java.util.Locale;

public class SearchableActivity extends ListActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Get ListView object from xml
        listView = (ListView) this.findViewById(android.R.id.list);

        BrickDAO b = new BrickDAO(this);
        b.open();
        List<Brick> bricks = b.findAll();
        final BrickListAdapter adapter = new BrickListAdapter(this, bricks);
        listView.setAdapter(adapter);

        final SearchView editSearch = (SearchView) this.findViewById(R.id.search);
        // Capture Text in EditText
        editSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = editSearch.getQuery().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = editSearch.getQuery().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
                return true;
            }
        });


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Brick selectedBrick = (Brick) getListAdapter().getItem(position);
        // TODO : This is not working (error)
        Intent i = new Intent(getApplicationContext(), AddBrick.class);
        i.putExtra("editable", false);
        i.putExtra("word", selectedBrick.getWord());
        i.putExtra("translation", selectedBrick.getTranslation());
        i.putExtra("examples", selectedBrick.getExamples());
        startActivity(i);

    }


}
