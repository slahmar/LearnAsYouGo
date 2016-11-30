package com.example.laygo.laygo.activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

<<<<<<< HEAD
import com.example.laygo.laygo.LocationService;
import com.example.laygo.laygo.R;
import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;
=======
import com.example.laygo.laygo.R;
>>>>>>> 80a3459f7acd8600368db7b99b8b5c037b536605
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final int ZOOM = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Context context = getApplicationContext();


        Location loc = new LocationService().getLocation(context);
        LatLng currentPosition;
        if (loc != null) {
            currentPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
            Geocoder gc = new Geocoder(context, Locale.getDefault());
            mMap.addMarker(new MarkerOptions().position(currentPosition).title("Current Position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, ZOOM));
            // display city name
            /*try {
                List<Address> addr = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addr.size() > 0)
                    Toast.makeText(this, addr.get(0).getLocality(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
            }*/

        }
        else {
            currentPosition = new LatLng(53.3461092, -6.2714981);
            mMap.addMarker(new MarkerOptions().position(currentPosition).title("Dublin"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, ZOOM));
        }

        BrickDAO bdao = new BrickDAO(getApplicationContext());
        bdao.open();
        for (Brick b : bdao.findAll()) {
            loc = b.getLocation();
            if (loc != null){
                currentPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentPosition).title(b.getWord()));

            }
        }
        bdao.close();
    }
}
