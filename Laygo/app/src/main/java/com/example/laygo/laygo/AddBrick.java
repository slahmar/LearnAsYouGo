package com.example.laygo.laygo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laygo.laygo.model.Brick;

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

    private Uri mMediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brick);
        ButterKnife.bind(this);
        this.imageView = (ImageView)this.findViewById(R.id.showPhoto);

        ImageView photoButton = (ImageView) this.findViewById(R.id.takePhoto);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Brick b = null;
                //mMediaUri = getOutputFileUri(b);

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                startActivityForResult(intent, REQ_TAKE_PHOTO);
            }
        });

        ImageView locationButton = (ImageView) this.findViewById(R.id.setLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
            }
        });
    }


    private Uri getOutputFileUri(Brick b) {
        String word = b != null ? b.getWord() : "";
        String tStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fName = "IMG_" + word + "_" + tStamp + ".jpg";
        if (isExtStorageAvailable()) {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM", fName);
            Uri imgUri = Uri.fromFile(file);

            return imgUri;

        }
        return null;
    }

    private boolean isExtStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState());
        //Log.e("Ext storage available? ", String.valueOf(result));

    }

    @Override
    protected void  onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
        else {
            Toast.makeText(this, "Error (" + String.valueOf(resCode) + ")", Toast.LENGTH_LONG).show();
        }
    }


    protected void setLocation() {
        Context context = getApplicationContext();
        Location location = new LocationService().getLocation(context);
        if (location != null) {
            // set location of the brick
            Toast.makeText(this, "Location retrieved", Toast.LENGTH_LONG).show();
            ImageView locationButton = (ImageView) this.findViewById(R.id.setLocation);
            ImageView locationAvailableButton = (ImageView) this.findViewById(R.id.locationAvailable);
            locationButton.setVisibility(View.INVISIBLE);
            locationAvailableButton.setVisibility(View.VISIBLE);

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

