package com.example.laygo.laygo.model;

import android.location.Location;

/**
 * A brick is a word with its translation under different formats (audio, picture, text) and example sentences
 */
public class Brick {
    private long id;
    private String word;
    private String translation;
    private String examples;
    private String imagePath;
    private Location location;
    private String recording;

    // Get the translation
    public String getTranslation() {
        return translation;
    }

    // Set the translation
    public void setTranslation(String translation) {
        this.translation = translation;
    }

    // Get the example sentences
    public String getExamples() {
        return examples;
    }

    // Set the example sentences
    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getImage() {
        return imagePath;
    }

    public void setImage(String image) {
        this.imagePath = image;
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

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public String getRecording() { return recording;}

    public void setRecording(String path) { recording = path; }

    @Override
    public String toString(){
        return "Brick : "+word + " = "+translation+", examples = "+examples+", path = "+imagePath;
    }
}