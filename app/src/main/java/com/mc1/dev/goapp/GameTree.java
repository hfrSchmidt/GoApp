package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class GameTree
// This class provides the functionality to store a number of moves
// in a tree-like data structure so that different variations within
// a game can be stored
// ----------------------------------------------------------------------
public class GameTree {

    private Node rootNode;
    private Node lastAddedNode;

    public GameTree(Node rootNodeInput) {
        if (rootNode != null) {
            this.rootNode = rootNodeInput;
        } else {
            this.rootNode = new Node(null, null);
        }
        this.lastAddedNode = this.rootNode;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Node getLastAddedNode() {
        return lastAddedNode;
    }

}
