package com.mc1.dev.goapp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

// ----------------------------------------------------------------------
// class RunningGame
// this class contains the internal representation of a running game
// excluding meta parameters like board size, komi, HC ...
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class RunningGame implements Serializable{
    private int moveNumber;
    private GameMetaInformation gmi;

    private ArrayList<Move> board;

    public RunningGame(GameMetaInformation gmi) {
        this.gmi = gmi;
        this.moveNumber = 1;
        this.board = new ArrayList<>(0);
    }

    public RunningGame(File sgfFile) {
        // create new sgfParser Object and call the respective
        // methods to initialise the members
    }

    public void writeToSGF() {

    }

    public void playMove(int[] position, String comment) {
        boolean isBlacksTurn = true;
        if (moveNumber % 2 == 0) {
            isBlacksTurn = false;
        }
        if (moveNumber % 2 == 1 && gmi.getHandicap() != 0) {
            isBlacksTurn = false;
        }

        board.add(new Move(isBlacksTurn, position, comment));
    }

    public void takeLastMoveBack() {
        if (!board.isEmpty()) {
            board.remove(board.size() - 1);
        }
    }

    public GameMetaInformation getGameMetaInformation() {
        return gmi;
    }


}
