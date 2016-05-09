package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class Move
// contains the representation of a move placed on the board
// ----------------------------------------------------------------------
public class Move {
    private boolean color;
    private int[] position;
    private String comment;

    public Move(boolean color, int[] position, String comment) {
        this.color = color;
        this.position = position;
        this.comment = comment;
    }

    public Move(boolean color, int[] position) {
        this.color = color;
        this.position = position;
        this.comment = null;
    }


    public boolean getColor() {
        return color;
    }

    public int[] getPosition() {
        return position;
    }

    public String getComment() {
        return comment;
    }
}
