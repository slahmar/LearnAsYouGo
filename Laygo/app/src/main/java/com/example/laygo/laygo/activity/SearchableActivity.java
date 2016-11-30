package com.example.laygo.laygo.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.laygo.laygo.BrickListAdapter;
import com.example.laygo.laygo.R;
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

        listView = (ListView) this.findViewById(android.R.id.list);

        new Thread() {
            public void run() {
                BrickDAO b = new BrickDAO(SearchableActivity.this);
                b.open();
                List<Brick> bricks = b.findAll();
                final BrickListAdapter adapter = new BrickListAdapter(SearchableActivity.this, bricks);
                listView.setAdapter(adapter);
                b.close();

                final SearchView editSearch = (SearchView) SearchableActivity.this.findViewById(R.id.search);
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
        }.start();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Brick selectedBrick = (Brick) listView.getAdapter().getItem(position);
        Intent i = new Intent(getApplicationContext(), ViewAndEditBrickActivity.class);
        i.putExtra("editable", false);
        i.putExtra("word", selectedBrick.getWord());
        i.putExtra("photo", selectedBrick.getImage());
        i.putExtra("translation", selectedBrick.getTranslation());
        i.putExtra("examples", selectedBrick.getExamples());
        Location loc = selectedBrick.getLocation();
        if(loc != null ){
            i.putExtra("latitude", loc.getLatitude());
            i.putExtra("longitude", loc.getLongitude());
        }
        i.putExtra("id", selectedBrick.getId());
        startActivity(i);

    }


}
