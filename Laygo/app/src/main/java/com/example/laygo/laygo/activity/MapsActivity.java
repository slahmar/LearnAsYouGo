package com.example.laygo.laygo.activity;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.laygo.laygo.LocationService;
import com.example.laygo.laygo.R;
import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.model.Brick;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

// Displays a Google Maps activity with the locations of the user's bricks
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final int ZOOM = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    // Add a marker for each of the bricks and one at the current location or in Dublin.
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
        } else {
            currentPosition = new LatLng(53.3461092, -6.2714981);
            mMap.addMarker(new MarkerOptions().position(currentPosition).title("Dublin"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, ZOOM));
        }

        BrickDAO bdao = null;
        try {
            bdao = new BrickDAO(getApplicationContext());
            bdao.open();
            for (Brick b : bdao.findAll()) {
                loc = b.getLocation();
                if (loc != null && loc.getLatitude() != Double.MAX_VALUE && loc.getLongitude() != Double.MAX_VALUE) {
                    currentPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currentPosition).title(b.getWord()));

                }
            }
        } finally {
            if (bdao != null) bdao.close();
        }
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

}
