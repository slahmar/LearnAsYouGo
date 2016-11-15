package com.example.laygo.laygo;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;
import com.example.laygo.laygo.BrickArrayAdapter;

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
        final BrickArrayAdapter adapter = new BrickArrayAdapter(this, bricks);
        setListAdapter(adapter);

        final EditText editSearch = (EditText) this.findViewById(R.id.search);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*final SearchView editSearch = (SearchView) this.findViewById(R.id.search);
 
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
        });*/


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

        // Go to see the brick
        Intent i = new Intent(getApplicationContext(), AddBrick.class);
        i.putExtra("editable", false);
        startActivity(i);

    }


}
