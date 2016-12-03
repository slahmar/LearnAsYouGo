package com.example.laygo.laygo.model;

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
    public int getAsked() {return asked;}
    public int getCorrect() {return correct;}
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

    // The current question has precedence if it has been answered correctly fewer times
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
