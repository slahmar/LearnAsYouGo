package com.example.laygo.laygo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.laygo.laygo.LocationService;
import com.example.laygo.laygo.HomeActivity;
import com.example.laygo.laygo.R;
import com.example.laygo.laygo.RemoteFetchExamples;
import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ViewAndEditBrickActivity extends AppCompatActivity {
    public static final int REQ_TAKE_PHOTO = 0;
    // Graphic elements
    private ImageView locationButton;
    private ImageView cameraButton;
    private Button saveButton;
    private Button cancelButton;
    private Button editButton;
    private Button searchButton;
    private EditText word;
    private EditText translation;
    private EditText examples;
    private ViewSwitcher wordSwitcher;
    private ViewSwitcher translationSwitcher;
    private ViewSwitcher examplesSwitcher;
    // Model elements
    private Brick b;
    private String photoPath;
    private Location location;

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        boolean editable = getIntent().getBooleanExtra("editable", true);

        setContentView(R.layout.activity_view_edit_brick);
        final Context context = this.getApplicationContext();

        searchButton = ((Button) this.findViewById(R.id.searchButton));
        cameraButton = (ImageView) this.findViewById(R.id.camera);
        word = ((EditText) this.findViewById(R.id.editWord));
        translation = ((EditText) this.findViewById(R.id.editTranslation));
        examples = ((EditText) this.findViewById(R.id.editExamples));
        saveButton = (Button) this.findViewById(R.id.saveButton);
        cancelButton = (Button) this.findViewById(R.id.cancelButton);
        editButton = (Button) findViewById(R.id.editButton);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeBrick(true);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchExamples();
            }
        });
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
                startActivity(i);
            }
        });
        locationButton = (ImageView) this.findViewById(R.id.setLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_TAKE_PHOTO);
            }
        });
        if (!editable) {
            setModeView(getIntent());
        }
    }

    public void searchExamples(){
        new Thread(){
            public void run(){
                final String example = RemoteFetchExamples.getXML(getApplicationContext(), word.getText().toString());
                if(example == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(),
                                    "No examples found",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            examples.setText(examples.getText()+"\n"+example);
                        }
                    });
                }
            }
        }.start();
    }

    public void setModeView(Intent i) {
        saveButton.setVisibility(View.INVISIBLE);
        saveButton.setEnabled(false);
        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setEnabled(false);
        cameraButton.setEnabled(false);
        cameraButton.setVisibility(View.INVISIBLE);
        locationButton.setEnabled(false);
        locationButton.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
        searchButton.setEnabled(false);

        final long id = i.getLongExtra("id", -1);
        final String wordString = i.getStringExtra("word");
        final String translationString = i.getStringExtra("translation");
        final String examplesString = i.getStringExtra("examples");
        final String path = i.getStringExtra("photo");
        final double latitude = i.getDoubleExtra("latitude", Double.MAX_VALUE);
        final double longitude = i.getDoubleExtra("longitude", Double.MAX_VALUE);

        b = new Brick();
        b.setExamples(examplesString);
        b.setTranslation(translationString);
        b.setWord(wordString);
        b.setId(id);
        if(latitude!= Double.MAX_VALUE && longitude!=Double.MAX_VALUE){
            location = new Location("");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            locationButton.setImageResource(R.drawable.location_set_icon);
        }

        photoPath = path;

        wordSwitcher = (ViewSwitcher) findViewById(R.id.wordSwitcher);
        wordSwitcher.showNext();
        TextView wordView = (TextView) wordSwitcher.findViewById(R.id.viewWord);
        wordView.setText(wordString);

        translationSwitcher = (ViewSwitcher) findViewById(R.id.translationSwitcher);
        translationSwitcher.showNext();
        TextView translationView = (TextView) translationSwitcher.findViewById(R.id.viewTranslation);
        translationView.setText(translationString);

        examplesSwitcher = (ViewSwitcher) findViewById(R.id.examplesSwitcher);
        examplesSwitcher.showNext();
        TextView examplesView = (TextView) examplesSwitcher.findViewById(R.id.viewExamples);
        examplesView.setText(examplesString);

        setPhotoView();


        editButton.setVisibility(View.VISIBLE);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                wordSwitcher.showPrevious();
                word.setText(wordString);
                examplesSwitcher.showPrevious();
                examples.setText(examplesString);
                translationSwitcher.showPrevious();
                translation.setText(translationString);
                saveButton.setVisibility(View.VISIBLE);
                saveButton.setEnabled(true);
                searchButton.setEnabled(true);
                searchButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setEnabled(true);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setModeView(getIntent());
                    }
                });
                editButton.setVisibility(View.INVISIBLE);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeBrick(false);
                    }
                });
                cameraButton.setVisibility(View.VISIBLE);
                cameraButton.setEnabled(true);
                locationButton.setVisibility(View.VISIBLE);
                locationButton.setEnabled(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE
            };
            final Cursor cursor = getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            // Put it in the image view
            if (cursor != null && cursor.moveToFirst()) {
                photoPath = cursor.getString(1);
                setPhotoView();
                cursor.close();
            }
        }
    }

    private void setPhotoView() {
        ImageView photoView = (ImageView) findViewById(R.id.photo);
        if(photoPath!=null && photoPath!=""){
            File imageFile = new File(photoPath);
            if (imageFile.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(photoPath);
                photoView.setImageBitmap(bm);
            }
        }

    }


    protected void setLocation() {
        Context context = getApplicationContext();
        location = new LocationService().getLocation(context);
        if (location != null) {
            // set location of the brick
            Toast.makeText(this, "Location retrieved", Toast.LENGTH_LONG).show();
            locationButton.setImageResource(R.drawable.location_set_icon);
            // display city name
            Geocoder gc = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addr = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addr.size() > 0)
                    Toast.makeText(this, addr.get(0).getLocality(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
            }

        } else {
            Toast.makeText(this, "Location not retrieved", Toast.LENGTH_LONG).show();
        }
    }


    private void completeBrick(boolean create) {
        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        String word = this.word.getText().toString();
        String transl = translation.getText().toString();
        String examples = this.examples.getText().toString();

        if (word.length() < 1) throw new IllegalStateException("Empty word");
        if (create) {
            try {
                b = bdao.createBrick(word);
            } catch (RuntimeException e) {
                Log.e("Error", e.toString());
                Toast.makeText(this, "This word is already in your database", Toast.LENGTH_LONG).show();
            }
            if (b == null) throw new IllegalStateException("Error creating the brick");
        }
        b.setImage(photoPath);
        b.setLocation(location);
        b.setWord(word);
        b.setTranslation(transl);
        b.setExamples(examples);
        bdao.updateBrick(b);
        bdao.close();
        Toast.makeText(this, "Word saved", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}


