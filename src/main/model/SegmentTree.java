package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static resources.TreeConfig.*;

/*
 * Represents a SegmentTree with customizable merge function
 * NOTE: All values are Modded with MOD specified in treeConfig to handle overflow
 */
public class SegmentTree {
    private static final TreeNode[] tree = new TreeNode[MAX_TREE_NODES]; // Holds computation result of subtree
    private String curMergeFn = DEFAULT_MERGE_FN; // Tracks the current merge function being used
    private ArrayList<TreeNode> leaves; // Tracks the current state of the leaves of the tree

    /*
     * REQUIRES: arr.length() == MAX_LEAVES;
     * MODIFIES: this
     * EFFECTS: Builds a segment tree from the given array with the default merge function
     */
    public SegmentTree(ArrayList<TreeNode> arr) {
        leaves = arr;
        for (int i = 0; i < MAX_TREE_NODES; i++) {
            tree[i] = new TreeNode(0);
        }
        build(arr, 0, MAX_LEAVES - 1, TREE_ROOT_IND, false);

        String nodeVals = "";
        for (TreeNode t : arr) {
            nodeVals += t.getVal() + " ";
        }
        EventLog.getInstance().logEvent(new Event("Generated new segment tree with " + arr.size()
                + " values - " + nodeVals));
    }

    /*
     * Updates the leaf node with the given value and then recomputes the parent values until root
     * REQUIRES: Valid index between 0 and MAX_LEAVES-1
     * MODIFIES: this
     * EFFECTS: tree is updated with the given value recursively from the leaf with the specified index
     */
    public void updateVal(int valInd, int val) {
        update(0, MAX_LEAVES - 1, TREE_ROOT_IND, valInd, val);
        EventLog.getInstance().logEvent(new Event("Updated segment tree at node index - " + valInd
                + " to value - " + val));
    }

    /*
     * Computes the sum/product for the given range
     * REQUIRES: Valid increasing range between 0 and MAX_LEAVES-1
     * EFFECTS: Computes the sum/product recursively and returns the exact sum/product based on the
     *          merge function in the range l to r
     */
    public int rangeSumProduct(int l, int r) {
        return computeRangeSumProduct(0, MAX_LEAVES - 1, TREE_ROOT_IND, l, r);
    }

    /*
     * REQUIRES: Valid index between 0 and MAX_TREE_NODES-1
     * EFFECTS: Returns the tree node at the specified index
     */
    public TreeNode getTreeNodeByInd(int treeInd) {
        return tree[treeInd];
    }

    /*
     * EFFECTS: Repeats the current merge function
     */
    public String getCurMergeFn() {
        return curMergeFn;
    }

    /*
     * Updates the function being used to merge 2 child nodes
     * MODIFIES: this
     * EFFECTS: Toggles the merge function between Addition and Multiplication;
     *          Tree is recomputed with the updated merge function
     */
    public String flipMergeFn() {
        String prevMergeFn = curMergeFn;
        curMergeFn = (curMergeFn.equals("Addition") ? "Product" : "Addition");
        build(new ArrayList<>(), 0, MAX_LEAVES - 1, TREE_ROOT_IND, true);
        EventLog.getInstance().logEvent(new Event("Updated merge function from '" + prevMergeFn
                + "' to '" + curMergeFn + "'"));
        return curMergeFn;
    }

    /*
     * Code reference: [https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo]
     * EFFECTS: Returns this Segment Tree as a JSON Object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("leaves", getLeavesJson());
        json.put("mergeFunc", getCurMergeFn());
        return json;
    }

    /*
     * REQUIRES: Valid index between 0 and MAX_LEAVES-1
     * EFFECTS: Returns the leaf node at the specified index
     */
    public TreeNode getLeaf(int ind) {
        return leaves.get(ind);
    }

    /*
     * Builds/rebuilds the tree from the leaves
     * REQUIRES: Valid range (l r) between 0 and MAX_LEAVES-1;
     *           treeInd between 0 and MAX_TREE_NODES-1
     * MODIFIES: this
     * EFFECTS: If rebuild is disabled then the leaves are updated from the given array;
     *          Computes all node values by merging their children with the specified merge function
     */
    private void build(ArrayList<TreeNode> arr, int l, int r, int treeInd, boolean rebuild) {
        if (l == r) {
            if (!rebuild) {
                tree[treeInd] = new TreeNode(arr.get(l)); //Overwrite current leaf with an exact copy of the new leaf
            }
            return;
        }
        int mid = l + (r - l) / 2;
        build(arr, l, mid, 2 * treeInd, rebuild);
        build(arr, mid + 1, r, 2 * treeInd + 1, rebuild);
        tree[treeInd].setVal(merge(tree[2 * treeInd], tree[2 * treeInd + 1]));
    }

    /*
     * Updates the leaf node with the given value and then recomputes the parent values until root
     * REQUIRES: Valid range (l r) between 0 and MAX_LEAVES-1;
     *           treeInd between 0 and MAX_TREE_NODES-1;
     *           updateInd between 0 and MAX_LEAVES-1;
     * MODIFIES: this
     * EFFECTS: tree is updated with the given value recursively from the leaf with the specified index
     */
    private void update(int l, int r, int treeInd, int updateInd, int val) {
        tree[treeInd].highlightPath();

        if (l == r) {
            tree[treeInd].highlightCompute();
            tree[treeInd].setVal(val);
            leaves.get(updateInd).setVal(val); // TODO: Encapsulate this nicer with setter for val
            return;
        }
        int mid = l + (r - l) / 2;
        if (updateInd <= mid) {
            update(l, mid, 2 * treeInd, updateInd, val);
        } else {
            update(mid + 1, r, 2 * treeInd + 1, updateInd, val);
        }
        tree[treeInd].setVal(merge(tree[2 * treeInd], tree[2 * treeInd + 1]));
    }

    /*
     * Computes the sum/product for the given range
     * REQUIRES: Valid range (l r) between 0 and MAX_LEAVES-1;
     *           treeInd between 0 and MAX_TREE_NODES-1;
     *           Valid increasing range(targetL targetR) between 0 and MAX_LEAVES-1
     * EFFECTS: Computes the sum/product recursively and returns the exact sum/product based on the
     *          merge function in the range l to r
     */
    private int computeRangeSumProduct(int l, int r, int treeInd, int targetL, int targetR) {
        if (targetL > targetR) {
            if (curMergeFn.equals("Addition")) {
                return 0;
            } else {
                return 1;
            }
        }

        tree[treeInd].highlightPath();

        if (targetL == l && targetR == r) {
            tree[treeInd].highlightCompute();
            return tree[treeInd].getVal();
        }

        int mid = l + (r - l) / 2;

        return merge(computeRangeSumProduct(l, mid, 2 * treeInd, targetL, min(mid, targetR)),
                computeRangeSumProduct(mid + 1, r, 2 * treeInd + 1, max(mid + 1, targetL), targetR));
    }

    /*
     * EFFECTS: Merges 2 values based on the current merge function;
     *          Merge process includes modding with a large prime to handle overflow
     */
    private int merge(int left, int right) {
        if (curMergeFn.equals("Addition")) {
            return (int) ((0L + left + right) % MOD);
        } else {
            return (int) ((1L * left * right) % MOD);
        }
    }

    /*
     * EFFECTS: Merges 2 nodes based on the current merge function;
     *          Merge process includes modding with a large prime to handle overflow
     */
    private int merge(TreeNode left, TreeNode right) {
        return merge(left.getVal(), right.getVal());
    }

    /*
     * EFFECTS: Constructs and returns the json array with all leaf nodes for this tree
     */
    private JSONArray getLeavesJson() {
        JSONArray jsonArray = new JSONArray();

        for (TreeNode leaf : leaves) {
            jsonArray.put(leaf.toJson());
        }

        return jsonArray;
    }
}
