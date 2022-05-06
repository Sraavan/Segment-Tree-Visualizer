package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static resources.TreeConfig.MAX_LEAVES;

// Unit tests for render tree node class
public class RenderTreeNodeTest {

    @Test
    void testConstructor() {
        RenderTreeNode node = new RenderTreeNode(1, 0, MAX_LEAVES - 1);

        assertEquals(node.getNodeInd(), 1);
        assertEquals(node.getLeftRange(), 0);
        assertEquals(node.getRightRange(), 15);
    }
}
