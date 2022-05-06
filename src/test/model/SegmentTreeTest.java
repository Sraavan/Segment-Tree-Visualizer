package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.TreeConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static resources.TreeConfig.*;

// Unit tests for Segment Tree class
class SegmentTreeTest {

    private SegmentTree segTree;

    @BeforeEach
    void runBefore() {
        ArrayList<TreeNode> arr = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            arr.add(new TreeNode(i));
        }

        segTree = new SegmentTree(arr);
    }

    @Test
    void testConstructorBuild() {
        int sum = MAX_LEAVES * (MAX_LEAVES+1) / 2;
        assertEquals(segTree.getCurMergeFn(), DEFAULT_MERGE_FN);
        assertEquals(segTree.getTreeNodeByInd(TREE_ROOT_IND).getVal(), sum);
    }

    @Test
    void testUpdate() {
        segTree.updateVal(0, 2);
        int updatedSum = (16 * 17 / 2) - 1 + 2;
        assertEquals(segTree.getTreeNodeByInd(TREE_ROOT_IND).getVal(), updatedSum);

        segTree.updateVal(15, 20);
        updatedSum = updatedSum - 16 + 20;
        assertEquals(segTree.getTreeNodeByInd(TREE_ROOT_IND).getVal(), updatedSum);
    }

    @Test
    void testRangeSum() {
        int sum1 = segTree.rangeSumProduct(0, 0);
        assertEquals(sum1, 1);

        int sum2 = segTree.rangeSumProduct(0, 15);
        assertEquals(sum2, 16 * 17 / 2);
    }

    @Test
    void testRangeProduct() {
        segTree.flipMergeFn();
        assertEquals(segTree.rangeSumProduct(2, 2), 3);

        long product = 1;
        for (int i = 2; i <= 16; i++) {
            product = (product * i) % MOD;
        }

        assertEquals(segTree.rangeSumProduct(0, 15), product);
    }

    @Test
    void testFlipMergeFn() {
        int allSum = 16 * 17 / 2;
        long allProduct = 1;
        for (int i = 2; i <= 16; i++) {
            allProduct = (allProduct * i) % MOD;
        }

        assertEquals(segTree.getCurMergeFn(), "Addition");
        assertEquals(segTree.getTreeNodeByInd(TREE_ROOT_IND).getVal(), allSum);

        segTree.flipMergeFn();
        assertEquals(segTree.getCurMergeFn(), "Product");
        assertEquals(segTree.getTreeNodeByInd(TREE_ROOT_IND).getVal(), allProduct);

        segTree.flipMergeFn();
        assertEquals(segTree.getCurMergeFn(), "Addition");
        assertEquals(segTree.getTreeNodeByInd(TREE_ROOT_IND).getVal(), allSum);
    }

}
