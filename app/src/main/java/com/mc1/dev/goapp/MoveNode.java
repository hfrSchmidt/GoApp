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

    // defines what type of action is used: 0 = set, 1 = pass, 2 = resign
    // just like static variables in GMI
    private GameMetaInformation.actionType actionType;
    private boolean isBlacksMove;
    private int[] position;
    private String comment;
    private long currentTime;
    private byte currentOtPeriods;

    // the constructor to create root nodes
    public MoveNode() {
        this.actionType = GameMetaInformation.actionType.PASS;
        // TODO black does not always begin: In HC games white begins!!
        // Game controller needs function to return whether the game is a HC game
        this.isBlacksMove = false; // root is considered a move of white, so black begins
        this.children = new ArrayList<>();
        if (isBlacksMove) {
            this.currentTime = TimeController.getInstance().getBlackTimeLeft();
            this.currentOtPeriods = TimeController.getInstance().getBlackPeriodsLeft();
        } else {
            this.currentTime = TimeController.getInstance().getWhiteTimeLeft();
            this.currentOtPeriods = TimeController.getInstance().getWhitePeriodsLeft();
        }
        // game controller needs a function to return the board size
        // --> position of pass move.
    }

    // the constructor for normal use
    public MoveNode(GameMetaInformation.actionType actionType, boolean isBlacksMove, int[] position, MoveNode parent) {
        this.actionType = actionType;
        this.isBlacksMove = isBlacksMove;
        this.position = position;
        this.comment = null;
        this.children = new ArrayList<>();
        this.parent = parent;
        if (isBlacksMove) {
            this.currentTime = TimeController.getInstance().getBlackTimeLeft();
            this.currentOtPeriods = TimeController.getInstance().getBlackPeriodsLeft();
        } else {
            this.currentTime = TimeController.getInstance().getWhiteTimeLeft();
            this.currentOtPeriods = TimeController.getInstance().getWhitePeriodsLeft();
        }
    }

    public MoveNode(GameMetaInformation.actionType actionType, boolean isBlacksMove, int[] position, MoveNode parent, long time, byte otPeriods) {
        this.actionType = actionType;
        this.isBlacksMove = isBlacksMove;
        this.position = position;
        this.comment = null;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.currentTime = time;
        this.currentOtPeriods = otPeriods;
    }

    public MoveNode(GameMetaInformation.actionType actionType, boolean isBlacksMove, int[] position, MoveNode parent, long time, byte otPeriods, String co) {
        this.actionType = actionType;
        this.isBlacksMove = isBlacksMove;
        this.position = position;
        this.comment = co;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.currentTime = time;
        this.currentOtPeriods = otPeriods;
    }

    public MoveNode(GameMetaInformation.actionType actionType, boolean isBlacksMove, int[] position, MoveNode parent, String comment) {
        this.actionType = actionType;
        this.isBlacksMove = isBlacksMove;
        this.position = position;
        this.comment = comment;
        this.children = new ArrayList<>();
        this.parent = parent;
        if (isBlacksMove) {
            this.currentTime = TimeController.getInstance().getBlackTimeLeft();
            this.currentOtPeriods = TimeController.getInstance().getBlackPeriodsLeft();
        } else {
            this.currentTime = TimeController.getInstance().getWhiteTimeLeft();
            this.currentOtPeriods = TimeController.getInstance().getWhitePeriodsLeft();
        }
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

    public long getTime() {
        return currentTime;
    }

    public byte getOtPeriods() {
        return currentOtPeriods;
    }

}
