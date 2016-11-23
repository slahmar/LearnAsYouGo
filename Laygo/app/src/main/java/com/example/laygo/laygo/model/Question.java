package com.example.laygo.laygo.model;

import java.util.Objects;

/**
 * This class represents a question about a certain brick
 */
public class Question implements Comparable {

    private Brick brick;

    private int asked = 0;
    private long brickID = -1;
    private int correct = 0;
    private long ID = 0;

    public Question(Brick b) {brick = b;}
    public Question() {this(null);}
    /**
     * Number of times the question has been asked
     */
    public int getAsked() {return asked;}
    /**
     * Number of times the question was correctly answered
     */
    public int getCorrect() {return correct;}
    /**
     * The word about which the question is
     */
    public Brick getBrick() {return brick;}

    public long getID() {return ID;}
    public long getBrickID() {return brickID;}

    public void setAsked(int v) {asked = v;}
    public void setCorrect(int v) {correct = v;}
    public void setBrick(Brick b) {brick = b;}
    public void setBrickID(long i) {brickID = i;}
    public void setID(long id) {this.ID = id;}

    public void incAsked() {asked++;}
    public void incCorrect() {correct++;}

    /**
     * Precedence: this has precedence if it has been answered correctly fewer times
     * @param q the other question to be compared this to
     * @return
     */
    public int compareTo(Object q) {
        Question q2 = (Question)q;

        return (getAsked() - getCorrect()) - (q2.getAsked() - q2.getCorrect());
    }

    public String toString() {
        return brick.getWord();
    }

    public boolean equals(Object o) {
        Question q = (Question)o;
        return q.getAsked() == this.getAsked() && q.getCorrect() == this.getCorrect() &&
                q.getBrickID() == this.getBrickID();
    }
}
