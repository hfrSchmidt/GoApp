package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class Move
// contains the representation of a move placed on the board
// ----------------------------------------------------------------------
public class Move {
    private boolean isBlacksTurn;
    private int[] position;
    private String comment;

    public Move(boolean isBlacksTurn, int[] position, String comment) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = comment;
    }

    public Move(boolean isBlacksTurn, int[] position) {
        this.isBlacksTurn = isBlacksTurn;
        this.position = position;
        this.comment = null;
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
}
