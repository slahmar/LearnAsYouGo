package com.example.laygo.laygo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class LocationService implements LocationListener {
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
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                onGPS.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(onGPS);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime, minDistance, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        } catch (Exception e) {
            Log.e("LOCATION", e.getMessage());
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
