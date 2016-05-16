package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class Move
// contains the representation of a move placed on the board
// ----------------------------------------------------------------------
public class Move {
    private boolean isBlacksTurn;
    private int[] position;
    private String comment;
    private Float timeLeftAfterMove;
    private int moveNumber;

    public Move(boolean isBlacksTurn, int[] position, int moveNumber, String comment, Float timeLeft) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.moveNumber = moveNumber;
        this.comment = comment;
        this.timeLeftAfterMove = timeLeft;
    }

    public Move(boolean isBlacksTurn, int[] position, int moveNumber, String comment) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.moveNumber = moveNumber;
        this.comment = comment;
        this.timeLeftAfterMove = null;
    }

    public Move(boolean isBlacksTurn, int[] position, int moveNumber) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.moveNumber = moveNumber;
        this.comment = null;
        this.timeLeftAfterMove = null;
    }


    public boolean getColor() {
        return isBlacksTurn;
    }

    public int[] getPosition() {
        return position;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public String getComment() {
        return comment;
    }

    public Float getTimeLeftAfterMove() {
        return timeLeftAfterMove;
    }
}
