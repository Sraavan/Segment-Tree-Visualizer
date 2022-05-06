package resources;

// Represents the constants used in a Segment Tree computation
public class TreeConfig {
    // Max array size to start the segment tree. Currently, has to be a power of 2 for a complete binary tree
    public static final int MAX_LEAVES = 16;

    // Represents the max tree nodes for the given number of leaves
    public static final int MAX_TREE_NODES = 4 * MAX_LEAVES + 5;

    // Represents the Tree root index. Changing this will break the tree!
    public static final int TREE_ROOT_IND = 1;

    // Represents the string length of each node
    public static final int NODE_LEN = 5;

    // Represents the default merge function being used
    public static final String DEFAULT_MERGE_FN = "Addition";

    // Represents the MOD to be used to prevent overflow
    public static final int MOD = (int) 1e9 + 7;
}
