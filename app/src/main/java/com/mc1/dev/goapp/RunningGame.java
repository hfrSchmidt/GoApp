package com.mc1.dev.goapp;

import java.io.Serializable;
import java.util.ArrayList;

// ----------------------------------------------------------------------
// class RunningGame
// author Felix Wisser
//
// this class contains the internal representation of a running game
// excluding meta parameters like board size, komi, HC ...
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class RunningGame implements Serializable{
    private GameMetaInformation gmi;
    private MoveNode rootNode;
    private ArrayList<Integer> mainTreeIndices;      // contains indices to the current node in use, to get nodes from other branches, alter this array


    public RunningGame(GameMetaInformation gmi)
    {
        this.gmi = gmi;
        rootNode = new MoveNode(); //TODO construct node according to gmi -> whoose turn is it? -> game controller
        mainTreeIndices = new ArrayList<Integer>();
    }
    // ----------------------------------------------------------------------
    // function getCurrentNode()
    //
    // returns the last added Node from the whole tree. this is the node,
    // to which the next played is attached
    // ----------------------------------------------------------------------
    public MoveNode getCurrentNode() {

        MoveNode currentlyDeepest = rootNode;

        for (int i = 0; i < mainTreeIndices.size(); i++) {
            currentlyDeepest = currentlyDeepest.getChildren().get(mainTreeIndices.get(i));
        }

        return currentlyDeepest;
    }

    // ----------------------------------------------------------------------
    // function getSpecificNode()
    //
    // returns a moveNode according to the given array list, which contains
    // tree-indices like the treeIndices-member of this class
    //
    // is used to get other branches of the tree and moves, that are not at
    // the bottom layer of the move-tree
    // ----------------------------------------------------------------------
    public MoveNode getSpecificNode(ArrayList<Integer> indexArray) {

        MoveNode currentlyDeepest = rootNode;

        for (int i = 0; i < indexArray.size(); i++) {
            currentlyDeepest = currentlyDeepest.getChildren().get(indexArray.get(i));
        }

        return currentlyDeepest;
    }

    // ----------------------------------------------------------------------
    // function playMove()
    //
    // creates a new moveNode and attaches it to the main tree-branch
    //
    // to add a move to different branch of the tree, use recordMove() TODO implement
    // ----------------------------------------------------------------------
    public void playMove(int actionType, int[] position) {

        MoveNode currentNode = this.getCurrentNode();
        MoveNode thisMoveNode = new MoveNode(actionType, !currentNode.isBlacksMove(), position, currentNode); // negate color to signal the other play is at turn
        this.addIndexToMainTree(currentNode.addChild(thisMoveNode)); // add child node and index

        if (actionType == 2) { // if action is "resign" therefore ending the game
            // end this shit via game controller -> call popup window
        }
    }
    // ----------------------------------------------------------------------
    // function addIndexToMainTree()
    //
    // append given index to the main game tree
    // ----------------------------------------------------------------------
    public void addIndexToMainTree(int index) {
        mainTreeIndices.add(index);
    }

    // ----------------------------------------------------------------------
    // function takeLastMoveBack()
    //
    // deletes the last node in the main tree-branch
    //
    // to delete from a different branch of the tree, use deleteRecordedMove() TODO implement
    // ----------------------------------------------------------------------
    public void takeLastMoveBack() {

        MoveNode lastAddedMove = this.getCurrentNode();
        MoveNode parentNode = lastAddedMove.getParent();

        parentNode.removeChild(lastAddedMove);
        mainTreeIndices.remove(mainTreeIndices.size()-1); // size-1 is the index of the last element in the list

    }

    public GameMetaInformation getGameMetaInformation() {
        return gmi;
    }

    public ArrayList<Integer> getMainTreeIndices() {
        return mainTreeIndices;
    }

}
