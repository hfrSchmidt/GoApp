package com.mc1.dev.goapp;

import java.io.Serializable;
import java.util.ArrayList;

// ----------------------------------------------------------------------
// class MoveNode
//
// contains the representation of a move placed on the board
// consists of move data as well as members for defining a tree structure
// to store multiple branches in one game
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class MoveNode implements Serializable {
    private ArrayList<MoveNode> children;
    private MoveNode parent;

    private int actionType; // defines what type of action is used: 0 = set, 1 = pass, 2 = resign
    private boolean isBlacksMove;
    private int[] position;
    private String comment;

    // the constructor to create root nodes
    public MoveNode() {
        this.actionType = 1;
        this.isBlacksMove = false; // root is considered a move of white, so black begins
        this.children = new ArrayList<>();
    }

    // the constructor for normal use
    public MoveNode(int actionType, boolean isBlacksMove, int[] position, MoveNode parent) {
        this.actionType = actionType;
        this.isBlacksMove = isBlacksMove;
        this.position = position;
        this.comment = null;
        this.children = new ArrayList<>();
        this.parent = parent;
    }

    // add child returns the index of the newly inserted child node
    public int addChild(MoveNode childInput) {
        this.children.add(childInput);
        return this.children.indexOf(childInput);
    }

    // removes the specified child from the children. Returns true if child existed and
    // false if it did not
    public boolean removeChild(MoveNode childToRemove) {
        return this.children.remove(childToRemove);
    }

    public ArrayList<MoveNode> getChildren() {
        return children;
    }

    public boolean isBlacksMove() {
        return isBlacksMove;
    }

    public int[] getPosition() {
        return position;
    }

    public String getComment() {
        return comment;
    }

    public MoveNode getParent() {
        return parent;
    }

}
