package com.example.laygo.laygo;

import android.location.Location;

import java.io.File;
import java.util.Vector;


/**
 * Created by Petra on 12-10-2016.
 */

public class Brick {
    private String word;
    private String translation;
    private Vector<String> examples;

    private File image;
    private File audio;

    private Location location;

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Vector<String> getExamples() {
        return examples;
    }

    public void setExamples(Vector<String> examples) {
        this.examples = examples;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public File getAudio() {
        return audio;
    }

    public void setAudio(File audio) {
        this.audio = audio;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getWord() {

        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
