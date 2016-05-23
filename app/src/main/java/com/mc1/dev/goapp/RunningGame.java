package com.mc1.dev.goapp;

import java.io.File;
import java.io.Serializable;

// ----------------------------------------------------------------------
// class RunningGame
// this class contains the internal representation of a running game
// excluding meta parameters like board size, komi, HC ...
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class RunningGame implements Serializable{
    private int moveNumber;
    private GameMetaInformation gmi;
    private GameTree gt;

    public RunningGame(GameMetaInformation gmi, GameTree gtInput, int moveNumberInput) {
        this.gmi = gmi;
        this.moveNumber = moveNumberInput;
        this.gt = gtInput;
    }

    public RunningGame(GameMetaInformation gmi, GameTree gtInput) {
        this.gmi = gmi;
        this.moveNumber = 0;
        this.gt = gtInput;
    }

    public RunningGame(File sgfFile) {
        SGFParser sgfp = new SGFParser();

        RunningGame rg = sgfp.parse(sgfFile);
        this.gmi = rg.getGameMetaInformation();
        this.moveNumber = rg.getMoveNumber();
        this.gt = rg.getGameTree();
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

        this.moveNumber++;
        // TODO this only works for adding children to the root node. GameTree needs a getLastNode method.
        MoveNode thisMoveNode = new MoveNode(isBlacksTurn, position, comment);
        Node thisNode = new Node(gt.getLastAddedNode(), thisMoveNode);
        this.gt.getLastAddedNode().addChild(thisNode);
    }

    public void takeLastMoveBack() {

    }

    public GameMetaInformation getGameMetaInformation() {
        return gmi;
    }

    public int getMoveNumber() {
        return this.moveNumber;
    }

    public GameTree getGameTree() {
        return this.gt;
    }


}
