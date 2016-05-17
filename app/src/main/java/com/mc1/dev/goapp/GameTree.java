package com.mc1.dev.goapp;

import java.util.ArrayList;

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

    public static class Node {

        private ArrayList<Node> children;
        private Node parent;
        private Move move;

        public Node(Node parentNodeInput, Move moveInput, ArrayList<Node> childrenInput) {
            this.parent = parentNodeInput;
            this.move = moveInput;
            this.children = childrenInput;
        }

        public Node(Node parentNodeInput, Move moveInput) {
            this.parent = parentNodeInput;
            this.move = moveInput;
            this.children = new ArrayList<>(0);
        }

        // add child returns the index of the newly inserted child node
        public int addChild(Node childInput) {
            this.children.add(childInput);
            return this.children.indexOf(childInput);
        }

        // removes the specified child from the children. Returns true if child existed and
        // false if it did not
        public boolean removeChild(Node childToRemove) {
            return this.children.remove(childToRemove);
        }
    }

}
