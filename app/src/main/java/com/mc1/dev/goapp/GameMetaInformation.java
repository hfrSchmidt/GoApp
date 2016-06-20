package com.mc1.dev.goapp;

import java.io.Serializable;

// ----------------------------------------------------------------------
// class GameMetaInformation
// gmi is a data container to store information of a single game
// stored are only option parameters like komi and time mode
// no information on current board state is stored
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class GameMetaInformation implements Serializable {
    private float komi;
    private int handicap;
    private int boardSize;
    private String timeMode;
    private String whiteName;
    private String blackName;
    private String whiteRank;
    private String blackRank;
    private String result;

    // in accordance with MoveNode's actionType
    public enum actionType {
        MOVE,
        PASS,
        RESIGN
    }

    public GameMetaInformation() {
        //TODO possible invalid values for float and int?
        this.timeMode = null;
        this.whiteName = null;
        this.blackName = null;
        this.whiteRank = null;
        this.blackRank = null;
        this.result = "Void";
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String res) {
        this.result = res;
    }

    public float getKomi() {
        return komi;
    }

    public void setKomi(float komi) {
        this.komi = komi;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public String getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(String timeMode) {
        this.timeMode = timeMode;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public void setWhiteRank(String whiteRank) {
        this.whiteRank = whiteRank;
    }

    public String getWhiteRank() {
        return this.whiteRank;
    }

    public void setBlackRank(String blackRank) {
        this.blackRank = blackRank;
    }

    public String getBlackRank() {
        return this.blackRank;
    }
}
