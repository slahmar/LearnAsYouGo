package com.example.laygo.laygo.model;

/**
 * This class represents a question about a certain brick
 */
public abstract class Question {
    /**
     * The word about which the question is
     */
    private Brick brick;
    /**
     * Number of times the question has been asked
     */
    private int asked = 0;
    /**
     * Number of times the question was correctly answered
     */
    private int correct = 0;
}
