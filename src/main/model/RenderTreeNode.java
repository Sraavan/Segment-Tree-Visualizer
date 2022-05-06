package model;

// Represents a single node in a segment tree
public class RenderTreeNode {
    private int nodeInd; // Tracks the index of the node in a segment tree
    private int leftRange; // Tracks the left range the node represents in the tree
    private int rightRange; // Tracks the right range the node represents in the tree

    /*
     * REQUIRES: leftRange and rightRange should satisfy the inequality: 0 <= leftRange <= rightRange < MAX_LEAVES-1
     * MODIFIES: this
     * EFFECTS: Initializes a node at the given node Index
     */
    public RenderTreeNode(int nodeInd, int leftRange, int rightRange) {
        this.nodeInd = nodeInd;
        this.leftRange = leftRange;
        this.rightRange = rightRange;
    }

    public int getNodeInd() {
        return nodeInd;
    }

    public int getLeftRange() {
        return leftRange;
    }

    public int getRightRange() {
        return rightRange;
    }
}
