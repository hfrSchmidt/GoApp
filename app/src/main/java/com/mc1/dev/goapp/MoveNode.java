package com.mc1.dev.goapp;

import java.util.ArrayList;

// ----------------------------------------------------------------------
// class MoveNode
// contains the representation of a move placed on the board
// ----------------------------------------------------------------------
public class MoveNode {
    private ArrayList<MoveNode> children;
    private MoveNode parent;

    private boolean isBlacksTurn;
    private int[] position;
    private String comment;

    public MoveNode(boolean isBlacksTurn, int[] position, String comment, ArrayList<MoveNode> childrenInput) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = comment;
        this.children = childrenInput;
    }

    public MoveNode(boolean isBlacksTurn, int[] position, String comment) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = comment;
        this.children = new ArrayList<>(0);
        this.parent = null;
    }

    public MoveNode(boolean isBlacksTurn, int[] position) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = null;
        this.children = new ArrayList<>(0);
        this.parent = null;
    }


    public boolean getColor() {
        return isBlacksTurn;
    }

    public int[] getPosition() {
        return position;
    }

    public String getComment() {
        return comment;
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
}
