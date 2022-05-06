package ui;

import model.RenderTreeNode;
import model.SegmentTree;
import model.TreeNode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Math.max;
import static resources.TreeConfig.*;

// Renders a given segment tree for GUI
public class TreeRenderer extends JPanel {

    // Tracks the current segment tree to be rendered
    private SegmentTree segTree;

    // Stores the rendering of the given segment tree
    private static String[] treeRender = new String[MAX_TREE_NODES];

    // GUI variables
    private JScrollPane scrollPane;
    private JLabel segTreeRenderLbl;

    /*
     * REQUIRES: Complete binary segment tree
     * MODIFIES: this
     * EFFECTS: Initializes the current segment tree state to the given segment tree and renders the tree panel
     */
    public TreeRenderer(SegmentTree st) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.segTree = st;

        JLabel label = new JLabel("Segment Tree");
        add(label);

        JPanel treeRender = this.render();
        scrollPane = new JScrollPane(treeRender);
        add(scrollPane);
    }

    /*
     * REQUIRES: Complete binary segment tree
     * MODIFIES: this
     * EFFECTS: Updates the current segment tree and re-renders it
     */
    public void updateAndRenderSegTree(SegmentTree st) {
        this.segTree = st;
        this.render();
    }

    /*
     * Renders a tree starting from the leaves and stores it; Following this, a BFS is run to combine the renders
     * MODIFIES: this
     * EFFECTS: treeRender stores the tree render for the subtree starting at the given node
     */
    public JPanel render() {
        renderTree(0, MAX_LEAVES - 1, TREE_ROOT_IND);

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setAlignmentX(CENTER_ALIGNMENT);

        if (segTreeRenderLbl == null) {
            segTreeRenderLbl = new JLabel("<html><pre>" + this.combineNodeRenders() + "</pre></html>");
            boxPanel.add(segTreeRenderLbl);
        } else {
            segTreeRenderLbl.setText("<html><pre>" + this.combineNodeRenders() + "</pre></html>");
        }
        return boxPanel;
    }

    /*
     * Combines the generated node renders for each level and returns the combined render
     * EFFECTS: BFS is run on treeRender to merge the renders at every level;
     */
    private String combineNodeRenders() {
        StringBuilder renderedString = new StringBuilder();

        Queue<RenderTreeNode> queue = new LinkedList<>();
        queue.add(new RenderTreeNode(1, 0, MAX_LEAVES - 1));

        // Run BFS to combine the renders
        while (!queue.isEmpty()) {
            int curQueueSize = queue.size();
            while (curQueueSize-- > 0) {
                RenderTreeNode curNode = queue.poll();

                renderedString.append(treeRender[curNode.getNodeInd()]);

                int leftRange = curNode.getLeftRange();
                int rightRange = curNode.getRightRange();

                // Stop BFS when a leaf is reached
                if (leftRange == rightRange) {
                    continue;
                }

                int mid = leftRange + (rightRange - leftRange) / 2;
                queue.add(new RenderTreeNode(2 * curNode.getNodeInd(), leftRange, mid));
                queue.add(new RenderTreeNode(2 * curNode.getNodeInd() + 1, mid + 1, rightRange));
            }
            renderedString.append("<br/>");
        }

        return renderedString.toString();
    }

    /*
     * Renders a subtree for the given tree node
     * REQUIRES: Valid range (l r) between 0 and MAX_LEAVES-1;
     *           Valid treeInd between 0 and MAX_TREE_NODES-1
     * MODIFIES: this
     * EFFECTS: Runs a post-order DFS to render the Subtrees and then renders the currentNode;
     *          updates treeRender to store the render for the current node;
     *          Tracks and returns the current Height of the subtree from the leaves
     */
    private int renderTree(int l, int r, int treeInd) {
        int mid = l + (r - l) / 2;

        int curHeight = 0;
        if (l != r) {
            // Height of left subtree = Height of right subtree
            renderTree(l, mid, 2 * treeInd);
            curHeight = renderTree(mid + 1, r, 2 * treeInd + 1) + 1;
        }

        renderNode(treeInd, curHeight);

        return curHeight;
    }

    /*
     * Renders a single node
     * REQUIRES: Valid treeInd between 0 and MAX_TREE_NODES-1
     *           curHeight should be 0 or positive
     * MODIFIES: this
     * EFFECTS: Renders the current node with padding based on the given height;
     *          updates treeRender to store the render for the current node;
     */
    private void renderNode(int treeInd, int curHeight) {
        StringBuilder curNode = new StringBuilder();

        // Handle leaves without adding any padding
        if (curHeight == 0) {
            TreeNode curSegTreeNode = segTree.getTreeNodeByInd(treeInd);
            treeRender[treeInd] = getFormattedNodeVal(curSegTreeNode) + pad(NODE_LEN, ' ');
            return;
        }

        TreeNode curSegTreeNode = segTree.getTreeNodeByInd(treeInd);

        // Get number of nodes / 2 for the previous height, for padding
        int cntPrevNodes = (1 << (curHeight - 1)) >> 1;
        int paddingLen = cntPrevNodes * NODE_LEN
                + max(cntPrevNodes - 1, 0) * NODE_LEN
                + NODE_LEN / 2;

        curNode.append(pad(paddingLen, ' ') + '/' + pad(paddingLen, '-'));
        curNode.append(getFormattedNodeVal(curSegTreeNode));
        curNode.append(pad(paddingLen, '-') + '\\' + pad(paddingLen, ' '));
        curNode.append(pad(NODE_LEN, ' '));

        treeRender[treeInd] = curNode.toString();
    }

    /*
     * Returns a string padding of paddingLen for the given char
     * REQUIRES: paddingLen should be positive
     * MODIFIES: this
     * EFFECTS: Repeats the given char for padding, for the specified number of times - paddingLen
     */
    private String pad(int paddingLen, char padChar) {
        StringBuilder padding = new StringBuilder();
        while (paddingLen-- > 0) {
            padding.append(padChar);
        }

        return padding.toString();
    }

    /*
     * Centers a node value in the Node render
     * EFFECTS: Places a node value in the center, in a string with a maximum length of NODE_LEN
     *          with whitespaces for padding;
     */
    private String getFormattedNodeVal(TreeNode segTreeNode) {
        String strNodeVal = Integer.toString(segTreeNode.getVal());
        int valLen = strNodeVal.length();

        int remPadding = NODE_LEN - valLen;
        int leftPadding = remPadding / 2;
        int rightPadding = remPadding - leftPadding;

        return pad(leftPadding, ' ')
                + strNodeVal + pad(rightPadding, ' ');
    }


}
