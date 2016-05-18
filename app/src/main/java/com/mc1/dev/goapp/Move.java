package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class Move
// contains the representation of a move placed on the board
// ----------------------------------------------------------------------
public class Move {
    private boolean isBlacksTurn;
    private int[] position;
    private String comment;
    private long timeLeftAfterMove;
    private int periodsLeftAfterMove;

    public Move(boolean isBlacksTurn, int[] position, String comment, long timeLeft, int periodsLeft) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = comment;
        this.timeLeftAfterMove = timeLeft;
        this.periodsLeftAfterMove = periodsLeft;
    }

    public Move(boolean isBlacksTurn, int[] position, long timeLeft, int periodsLeft) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = null;
        this.timeLeftAfterMove = timeLeft;
        this.periodsLeftAfterMove = periodsLeft;
    }

    public Move(boolean isBlacksTurn, int[] position, String comment) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = comment;
        this.timeLeftAfterMove = -1;
        this.periodsLeftAfterMove = -1;
    }

    public Move(boolean isBlacksTurn, int[] position) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = null;
        this.timeLeftAfterMove = -1;
        this.periodsLeftAfterMove = -1;
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

    public long getTimeLeftAfterMove() {
        return timeLeftAfterMove;
    }

    public int getPeriodsLeftAfterMove() {
        return periodsLeftAfterMove;
    }
}
