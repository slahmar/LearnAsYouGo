package com.example.laygo.laygo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddBrick extends AppCompatActivity {
    public static final int REQ_TAKE_PHOTO = 0;
    public static final int REQ_SET_LOCATION = 1;
    public static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 2;
    private ImageView imageView;
    private TextView word;
    private EditText translation;
    private EditText examples;
    private Brick b;
    private BrickDAO bdao;
    private String photoPath;
    private Bitmap photo;
    private Location location;

    private Uri mMediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean editable = getIntent().getBooleanExtra("editable", true);

        setContentView(R.layout.activity_add_brick);
        imageView = (ImageView)this.findViewById(R.id.camera);
        word = ((TextView)this.findViewById(R.id.editWord));
        translation = ((EditText)this.findViewById(R.id.editTranslation));
        examples = ((EditText)this.findViewById(R.id.editExamples));
        Button save = (Button)this.findViewById(R.id.saveButton);
        Button cancel = (Button)this.findViewById(R.id.cancelButton);

        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeBrick();
            }
        });

        final Context context = this.getApplicationContext();
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
                startActivity(i);
            }
        });

        ImageView locationButton = (ImageView) this.findViewById(R.id.setLocation);

        if(editable){
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocation();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQ_TAKE_PHOTO);
                }
            });
        }
        else{
            save.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
            locationButton.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            String wordString = getIntent().getStringExtra("word");
            String translationString = getIntent().getStringExtra("translation");
            String examplesString = getIntent().getStringExtra("examples");
            word.setText(wordString);
            word.setEnabled(false);
            translation.setEnabled(false);
            translation.setText(translationString);
            examples.setEnabled(false);
            examples.setText(examplesString);
        }
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
            if (cursor.moveToFirst()) {
                final ImageView imageView = (ImageView) findViewById(R.id.photo);
                photoPath = cursor.getString(1);
                File imageFile = new File(photoPath);
                if (imageFile.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(photoPath);
                    imageView.setImageBitmap(bm);
                }
            }
        }
    }


    protected void setLocation() {
        Context context = getApplicationContext();
        location = new LocationService().getLocation(context);
        if (location != null) {
            // set location of the brick
            Toast.makeText(this, "Location retrieved", Toast.LENGTH_LONG).show();
            ImageView locationButton = (ImageView) this.findViewById(R.id.setLocation);
            locationButton.setImageResource(R.drawable.location_set_icon);
            // display city name
            Geocoder gc = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addr = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addr.size() > 0)
                    Toast.makeText(this, addr.get(0).getLocality(), Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {}

        }
    }


    private void completeBrick() {
        bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        String word = this.word.getText().toString();
        if (word.length() < 1) throw new IllegalStateException("Empty word");
        try {
            b = bdao.createBrick(word);
        } catch (Exception e) {
            Log.d("Error", e.toString());
            Toast.makeText(this, "Word already added to the DB", Toast.LENGTH_LONG).show();
        }
        if (b == null) throw new IllegalStateException("Error creating the brick");

        String transl = translation.getText().toString();

        b.setImage(photoPath);
        b.setLocation(location);
        b.setWord(word);
        b.setTranslation(transl);
        b.setExamples("");
        bdao.updateBrick(b);

        Toast.makeText(this, bdao.findAll().get(bdao.findAll().size() - 1).toString(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }


}


class LocationService implements LocationListener  {
    private long minTime = 0; // minimum time interval between location updates, in milliseconds
    private float minDistance = 12; // 12 m (minimum distance between location updates)
    
    public Location getLocation(Context context) {
        Location location = null;
        LocationManager locationManager;

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        try   {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                return null;

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance, this);

            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            return location;

        } catch (Exception e) {

        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}