package com.mc1.dev.goapp;

import java.util.ArrayList;

// ----------------------------------------------------------------------
// class Node
// This class provides the functionality to store moves in a tree
// data structure with a variable amount of children per node.
// Every tree must have exactly one root node. Furthermore apart from the
// root node every Node should have exactly one parent Node.
// ----------------------------------------------------------------------
public class Node {

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
