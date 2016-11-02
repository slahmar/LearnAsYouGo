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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laygo.laygo.model.Brick;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public static final int REQ_TAKE_PHOTO = 0;
    public static final int REQ_SET_LOCATION = 1;
    public static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 2;
    private ImageView imageView;

    private Uri mMediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}


