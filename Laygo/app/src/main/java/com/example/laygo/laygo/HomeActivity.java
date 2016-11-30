package com.example.laygo.laygo;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;

import com.example.laygo.laygo.activity.ChooseQuiz;
import com.example.laygo.laygo.activity.MapsActivity;
import com.example.laygo.laygo.activity.SearchableActivity;
import com.example.laygo.laygo.activity.ViewAndEditBrickActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); //Set the layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar); //We created a app with a toolbar-free theme
        setSupportActionBar(myToolbar); //So we need to add one and say that it can have buttons

        String truc[] = {"Quiz", "Brick List", "Maps", "About"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, truc);
        ListView drawerMenu = (ListView) findViewById(R.id.drawerMenu); //We add some items in the drawer menu
        drawerMenu.setAdapter(adapter);
        drawerMenu.setOnItemClickListener(new DrawerItemClickListener());

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, myToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchableActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAddBrick (View view) {
        Intent intent = new Intent(this, ViewAndEditBrickActivity.class);
        startActivity(intent);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem (int position){
        Intent intent = null;
        switch(position){
            case 0:
                intent = new Intent(this, ChooseQuiz.class);
                break;

            case 1:
                intent = new Intent(this, SearchableActivity.class);
                break;
            case 2:
                intent = new Intent(this, MapsActivity.class);
                break;
            default: break;
        }
        if (intent != null)
            startActivity(intent);
    }

}

