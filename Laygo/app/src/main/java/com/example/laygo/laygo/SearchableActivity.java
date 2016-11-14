package com.example.laygo.laygo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class SearchableActivity extends ListActivity{

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        BrickDAO b = new BrickDAO(this);
        b.open();
        List<Brick> bricks = b.findAll();
        String [] values = new String[bricks.size()];
        for(int i = 0 ; i < bricks.size(); i ++) {
            values[i] = bricks.get(i).getWord();
        }
        BrickArrayAdapter adapter = new BrickArrayAdapter(this, values);
        setListAdapter(adapter);

        EditText editSearch = (EditText) findViewById(R.id.search);
 
        // Capture Text in EditText
        editSearch.addTextChangedListener(new TextWatcher() {
 
            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
 
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
                return;
            }
 
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                return;
            }
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
