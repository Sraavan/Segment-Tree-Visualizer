package persistence;

import model.SegmentTree;
import model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.TreeConfig;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the JsonSegTreeWriter class
public class JsonSegTreeWriterTest {
    private SegmentTree segTree;

    @BeforeEach
    void runBefore(){
        ArrayList<TreeNode> arr = new ArrayList<>();
        for (int i=1; i <= TreeConfig.MAX_LEAVES;i++){
            arr.add(new TreeNode(i));
        }
        segTree = new SegmentTree(arr);
    }

    @Test
    void testWriterSegTreeAdd() {
        try{
            JsonSegTreeWriter writer = new JsonSegTreeWriter("./data/testWriterSegTreeAdd.json");
            writer.open();
            writer.write(segTree);
            writer.close();

            JsonSegTreeReader reader = new JsonSegTreeReader("./data/testWriterSegTreeAdd.json");
            segTree = reader.read();
            int expectedSum = TreeConfig.MAX_LEAVES * (TreeConfig.MAX_LEAVES+1)/2;
            assertEquals(segTree.rangeSumProduct(0,TreeConfig.MAX_LEAVES-1), expectedSum);

            assertEquals(segTree.getLeaf(0).getVal(), 1);
            assertEquals(segTree.getLeaf(0).getColor(), TreeNode.DEFAULT_COLOR);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterSegTreeProduct() {
        try{
            segTree.flipMergeFn();

            JsonSegTreeWriter writer = new JsonSegTreeWriter("./data/testWriterSegTreeProduct.json");
            writer.open();
            writer.write(segTree);
            writer.close();

            JsonSegTreeReader reader = new JsonSegTreeReader("./data/testWriterSegTreeProduct.json");
            segTree = reader.read();
            long product = 1;
            for (int i=1;i<=TreeConfig.MAX_LEAVES;i++){
                product = (product * i) % TreeConfig.MOD;
            }
            assertEquals(segTree.rangeSumProduct(0,TreeConfig.MAX_LEAVES-1), product);

            assertEquals(segTree.getLeaf(0).getVal(), 1);
            assertEquals(segTree.getLeaf(0).getColor(), TreeNode.DEFAULT_COLOR);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
