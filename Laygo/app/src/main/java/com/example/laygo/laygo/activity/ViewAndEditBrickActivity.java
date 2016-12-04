package com.example.laygo.laygo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laygo.laygo.HomeActivity;
import com.example.laygo.laygo.LocationService;
import com.example.laygo.laygo.R;
import com.example.laygo.laygo.RemoteFetchExamples;
import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.dao.QuestionDAO;
import com.example.laygo.laygo.model.Brick;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;

/**
 * View and edit a brick
 */
public class ViewAndEditBrickActivity extends AppCompatActivity {
    public static final int REQ_TAKE_PHOTO = 0;
    // Graphic elements
    private ImageView locationButton;
    private ImageView cameraButton;
    private ImageView recordIcon;
    private ImageView playIcon;
    private Button saveButton;
    private Button cancelButton;
    private Button editButton;
    private Button searchButton;
    private EditText word;
    private EditText translation;
    private EditText examples;
    // Model elements
    private Brick b;
    private String photoPath;
    private String tempImage;
    private Location location;
    // Audio elements
    private static String mFileName;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private boolean startRecord = true;
    private boolean startPlay = true;
    private boolean hasRecorded = false;
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
        recordIcon = (ImageView) this.findViewById(R.id.recordIcon);
        playIcon = (ImageView) this.findViewById(R.id.playIcon);

        if (!getIntent().getBooleanExtra("editable", true)) {
            String truc = getIntent().getStringExtra("recording");
            if (truc.equals("")) playIcon.setVisibility(View.INVISIBLE);
        } else {
            playIcon.setVisibility(View.INVISIBLE);
        }

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/temp.3gp";

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
                tempImage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.jpg";
                File temp = new File(tempImage);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
                startActivityForResult(intent, REQ_TAKE_PHOTO);
            }
        });
        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay();
            }
        });
        recordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord();
            }
        });
        if (!editable) {
            setModeView(getIntent());
        }
    }

    // Record a sound and change the color of the record icon
    private void onRecord() {
        if (startRecord) {
            startRecording();
            recordIcon.setColorFilter(RED);
        } else {
            stopRecording();
            recordIcon.setColorFilter(BLACK);
        }
        startRecord = !startRecord;
    }

    // Play a sound and change the play to pause icon
    private void onPlay() {
        if (startPlay) {
            startPlaying();
            playIcon.setImageResource(R.drawable.ic_pause_black_48dp);
        } else {
            stopPlaying();
            playIcon.setImageResource(R.drawable.ic_play_arrow_black_48dp);
        }
        startPlay = !startPlay;
    }

    // Starts the player
    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            if (!getIntent().getBooleanExtra("editable", true)) {
                if (!getIntent().getStringExtra("audio").equals("")) {
                    if (hasRecorded) {
                        mPlayer.setDataSource(mFileName);
                    } else {
                        mPlayer.setDataSource(getIntent().getStringExtra("audio"));
                    }
                } else {
                    mPlayer.setDataSource(mFileName);
                }
            } else {
                mPlayer.setDataSource(mFileName);
            }
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("", "prepare() failed");
        }
    }

    // Stops the media player
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    // Starts the recorder
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("", "prepare() failed");
        }
        mRecorder.start();
    }

    // Stops the recorder
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        playIcon.setVisibility(View.VISIBLE);
        hasRecorded = true;
    }

    // Search examples in the dictionary API
    public void searchExamples() {
        new Thread() {
            public void run() {
                final String example = RemoteFetchExamples.getXML(getApplicationContext(), word.getText().toString());
                if (example == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "No examples found.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            examples.setText(examples.getText() + "\n" + example);
                        }
                    });
                }
            }
        }.start();
    }

    // Set the view as read-only
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
        recordIcon.setVisibility(View.INVISIBLE);
        recordIcon.setEnabled(false);

        final long id = i.getLongExtra("id", -1);
        final String wordString = i.getStringExtra("word");
        final String translationString = i.getStringExtra("translation");
        final String examplesString = i.getStringExtra("examples");
        final String imagePath = i.getStringExtra("photo");
        final String audioPath = i.getStringExtra("audio");
        final double latitude = i.getDoubleExtra("latitude", Double.MAX_VALUE);
        final double longitude = i.getDoubleExtra("longitude", Double.MAX_VALUE);

        b = new Brick();
        b.setExamples(examplesString);
        b.setTranslation(translationString);
        b.setWord(wordString);
        b.setId(id);
        if (latitude != Double.MAX_VALUE && longitude != Double.MAX_VALUE) {
            location = new Location("");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
        }

        File recordingFile = new File(mFileName);
        String recordingPath;
        if (recordingFile.exists()) {
            recordingPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + b.getId() + ".3gp";
            recordingFile.renameTo(new File(recordingPath));
        } else {
            recordingPath = "";
        }
        b.setRecording(recordingPath);


        if (audioPath.equals("")) {
            playIcon.setVisibility(View.INVISIBLE);
        }

        photoPath = imagePath;
        word.setText(wordString);
        word.setEnabled(false);
        examples.setText(examplesString);
        examples.setEnabled(false);
        translation.setText(translationString);
        translation.setEnabled(false);

        setPhotoView();

        editButton.setVisibility(View.VISIBLE);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                word.setEnabled(true);
                examples.setEnabled(true);
                translation.setEnabled(true);
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
                recordIcon.setVisibility(View.VISIBLE);
                recordIcon.setEnabled(true);
                if (!audioPath.equals("")) {
                    playIcon.setVisibility(View.VISIBLE);
                    playIcon.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (photoPath != null && !photoPath.equals("")) {
                File oldImage = new File(photoPath);
                oldImage.delete();
            }
            File image = new File(tempImage);
            if (image.exists()) {
                photoPath = tempImage;
                setPhotoView();
            }
        }
    }

    // Set the photo in the ImageView
    private void setPhotoView() {
        ImageView photoView = (ImageView) findViewById(R.id.photo);
        if (photoPath != null && !photoPath.equals("")) {
            File imageFile = new File(photoPath);
            if (imageFile.exists()) {
                photoView.setImageBitmap(getResizedImage(photoPath, 200));
            }
        }
    }

    // Get a resized bitmap version of a picture
    public static Bitmap getResizedImage(String path, int requiredSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, requiredSize, requiredSize);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    // Calculate sample size of bitmap
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // Retrieve location
    protected void setLocation() {
        Context context = getApplicationContext();
        location = new LocationService().getLocation(context);
        if (location != null) {
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

    // Complete the brick and save it
    private void completeBrick(final boolean create) {
        BrickDAO bdao = new BrickDAO(getApplicationContext());
        String word = ViewAndEditBrickActivity.this.word.getText()+"";
        String transl = ViewAndEditBrickActivity.this.translation.getText()+"";
        String examples = ViewAndEditBrickActivity.this.examples.getText()+"";

        if (word.length() < 1) throw new IllegalStateException("Empty word");
        if (create) {
            try {
                bdao.open();
                b = bdao.createBrick(word);
                bdao.close();
                QuestionDAO qdao = new QuestionDAO(getApplicationContext());
                qdao.open();
                qdao.createQuestion(b.getId());
                qdao.close();
            } catch (RuntimeException e) {
                bdao.close();
                Toast.makeText(ViewAndEditBrickActivity.this, "This word is already in your database", Toast.LENGTH_LONG).show();
            }
        }

        File recordingFile = new File(mFileName);
        String recordingPath;
        if (recordingFile.exists()) {
            recordingPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + b.getId() + ".3gp";
            recordingFile.renameTo(new File(recordingPath));
        } else {
            recordingPath = "";
        }

        if (photoPath != null) {
            File imageFile = new File(photoPath);
            if(imageFile.exists()){
                photoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + b.getId() + ".jpg";
                imageFile.renameTo(new File(photoPath));
            }
        }

        bdao.open();
        b.setImage(photoPath);
        b.setLocation(location);
        b.setWord(word);
        b.setTranslation(transl);
        b.setExamples(examples);
        b.setRecording(recordingPath);
        bdao.updateBrick(b);
        bdao.close();

        Toast.makeText(this, "Word saved", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
