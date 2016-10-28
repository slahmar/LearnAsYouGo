package com.example.laygo.laygo.model;

import java.util.Vector;

/**
 * A block is a set of related words
 */
public class Block {
    /**
     * The name of the block
     */
    private String name;
    /**
     * The set of words contained by the block
     */
    private Vector<Brick> block = new Vector<Brick>();

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public Vector<Brick> getBlock(){
        return block;
    }

    public void addBrick(Brick brick){
        this.block.add(brick);
    }

    public void removeBrick(Brick brick){
        this.block.remove(brick);
    }
}
