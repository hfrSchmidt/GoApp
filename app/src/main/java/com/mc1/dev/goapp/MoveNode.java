package com.mc1.dev.goapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String LOG_TAG = MoveNode.class.getSimpleName();

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
        this.parent = null;
        if (TimeController.getInstance().isConfigured()) {
            if (isBlacksMove) {
                this.currentTime = TimeController.getInstance().getBlackTimeLeft();
                this.currentOtPeriods = TimeController.getInstance().getBlackPeriodsLeft();
            } else {
                this.currentTime = TimeController.getInstance().getWhiteTimeLeft();
                this.currentOtPeriods = TimeController.getInstance().getWhitePeriodsLeft();
            }
        } else {
            this.currentTime = GameMetaInformation.INVALID_LONG;
            this.currentOtPeriods = GameMetaInformation.INVALID_BYTE;
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
        if (TimeController.getInstance().isConfigured()) {
            if (isBlacksMove) {
                this.currentTime = TimeController.getInstance().getBlackTimeLeft();
                this.currentOtPeriods = TimeController.getInstance().getBlackPeriodsLeft();
            } else {
                this.currentTime = TimeController.getInstance().getWhiteTimeLeft();
                this.currentOtPeriods = TimeController.getInstance().getWhitePeriodsLeft();
            }
        } else {
            this.currentTime = GameMetaInformation.INVALID_LONG;
            this.currentOtPeriods = GameMetaInformation.INVALID_BYTE;
        }
    }

    MoveNode(JSONObject jsonObj) {
        try {
            this.actionType = (GameMetaInformation.actionType) jsonObj.get("actionType");
            this.isBlacksMove = (boolean) jsonObj.get("isBlacksMove");
            this.position = (int[]) jsonObj.get("position");
            this.comment = (String) jsonObj.get("comment");
            this.parent = null;
            this.children = new ArrayList<>();
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    // copy constructor
    public MoveNode(MoveNode mn) {
        this.actionType = mn.getActionType();
        this.isBlacksMove = mn.isBlacksMove();
        this.position = mn.getPosition();
        this.comment = mn.getComment();
        this.children = mn.getChildren();
        this.parent = mn.getParent();
        this.currentTime = mn.getTime();
        this.currentOtPeriods = mn.getOtPeriods();
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
        if (TimeController.getInstance().isConfigured()) {
            if (isBlacksMove) {
                this.currentTime = TimeController.getInstance().getBlackTimeLeft();
                this.currentOtPeriods = TimeController.getInstance().getBlackPeriodsLeft();
            } else {
                this.currentTime = TimeController.getInstance().getWhiteTimeLeft();
                this.currentOtPeriods = TimeController.getInstance().getWhitePeriodsLeft();
            }
        } else {
            this.currentTime = GameMetaInformation.INVALID_LONG;
            this.currentOtPeriods = GameMetaInformation.INVALID_BYTE;
        }
    }

    // converts this move node to a json-string
    // excluding the parent and children, otherwise the whole game is converted every time the
    // method is called
    String toJSON() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("actionType", actionType)
                    .put("isBlacksMove", isBlacksMove)
                    .put("isPrisoner", isPrisoner)
                    .put("position", position)
                    .put("comment", comment)
                    .put("time", currentTime)
                    .put("otPeriods", currentOtPeriods);
        } catch (JSONException e) {
            // TODO do something with the exception e.g. retry
            e.printStackTrace();
        }
        Log.d(LOG_TAG, jsonObj.toString());
        return jsonObj.toString();
    }

    // add child returns the index of the newly inserted child node
    int addChild(MoveNode childInput) {
        childInput.setParent(this);
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

    public boolean hasChildren() {
        return !children.isEmpty();
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

    public void setPosition(int[] position) {
        this.position = position;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MoveNode getParent() {
        return parent;
    }

    private void setParent(MoveNode parent) {
        this.parent = parent;
    }

    public long getTime() {
        return currentTime;
    }

    public void setTime(long time) {
        this.currentTime = time;
    }

    public byte getOtPeriods() {
        return currentOtPeriods;
    }

    public void setOtPeriods(byte otPeriods) {
        this.currentOtPeriods = otPeriods;
    }
}
