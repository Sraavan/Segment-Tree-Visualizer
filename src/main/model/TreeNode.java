package model;

import org.json.JSONObject;

// Represents a Tree Node in a Segment Tree, having a value and color
public class TreeNode {
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String DEFAULT_COLOR = ANSI_WHITE_BACKGROUND + ANSI_BLACK;
    public static final String HIGHLIGHT_PATH_COLOR = ANSI_BLUE_BACKGROUND + ANSI_BLACK;
    public static final String HIGHLIGHT_COMPUTATION_COLOR = ANSI_RED_BACKGROUND + ANSI_BLACK;

    private int val;
    private String color;

    // EFFECTS: Constructs a tree node from a given node value and sets it's color to default color
    public TreeNode(int nodeVal) {
        this.val = nodeVal;
        setColor(DEFAULT_COLOR);
    }

    // EFFECTS: Constructs a new tree node with the same properties as the given tree node
    public TreeNode(TreeNode node) {
        this.val = node.val;
        this.color = node.color;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /*
     * Flags the node as being part of the current path
     * MODIFIES: this
     * EFFECTS: Sets the node's color to the Highlight path color
     */
    public void highlightPath() {
        this.setColor(HIGHLIGHT_PATH_COLOR);
    }

    /*
     * Flags the node as being used for computation
     * MODIFIES: this
     * EFFECTS: Sets the node's color to the Highlight Computation color
     */
    public void highlightCompute() {
        this.setColor(HIGHLIGHT_COMPUTATION_COLOR);
    }

    public int getVal() {
        return val;
    }

    public String getColor() {
        return color;
    }

    /*
     * MODIFIES: this
     * EFFECTS: Returns the current node's color and reset's it to default color
     */
    public String getAndResetColor() {
        String currentColor = color;

        setColor(DEFAULT_COLOR);
        return currentColor;
    }

    /*
     * Code reference: [https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo]
     * EFFECTS: Returns the current node as a JSON Object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("value", val);
        json.put("color", color);
        return json;
    }

}
