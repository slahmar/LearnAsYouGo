package com.example.laygo.laygo.model;

import android.location.Location;

import java.io.File;
import java.util.Vector;



/**
 * A brick is a word with its translation under different formats (audio, picture, text) and example sentences
 */
public class Brick {
    private long id;
    /**
     * The word in the learned language
     */
    private String word;
    /**
     * The word in the user's mother tongue
     */
    private String translation;
    /**
     * A set of example sentences using the word
     */
    private Vector<String> examples;
    /**
     * An image of the object
     */
    private File image;
    /**
     * An audio recording
     */
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

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }

    @Override
    public String toString(){
        return "Brick : "+word + " = "+translation;
    }
}
