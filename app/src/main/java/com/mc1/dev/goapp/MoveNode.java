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
    private GameMetaInformation.actionType actionType;
    private boolean isBlacksMove;
    private boolean isPrisoner;
    private int[] position;
    private String comment;
    private long currentTime;
    private byte currentOtPeriods;

    // the constructor to create root nodes
    public MoveNode(boolean isHCGame) {
        this.actionType = GameMetaInformation.actionType.PASS;
        // in HC games root is considered a black node --> white begins
        // in non Handicap games root is a white node
        this.isBlacksMove = isHCGame;
        this.isPrisoner = false;
        this.children = new ArrayList<>();
        if (isBlacksMove) {
            this.currentTime = TimeController.getInstance().getBlackTimeLeft();
            this.currentOtPeriods = TimeController.getInstance().getBlackPeriodsLeft();
        } else {
            this.currentTime = TimeController.getInstance().getWhiteTimeLeft();
            this.currentOtPeriods = TimeController.getInstance().getWhitePeriodsLeft();
        }
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

    public GameMetaInformation.actionType getActionType() {
        return actionType;
    }

    public ArrayList<MoveNode> getChildren() {
        return children;
    }

    public boolean isBlacksMove() {
        return isBlacksMove;
    }

    public void setPrisoner() {
        isPrisoner = true;
    }

    public void unsetPrisoner() {
        isPrisoner = false;
    }

    public boolean isPrisoner() {
        return isPrisoner;
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
