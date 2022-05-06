package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the TreeNode class
public class TreeNodeTest {

    @Test
    void testHighlightPath(){
        TreeNode node = new TreeNode(1);
        assertEquals(node.getColor(), TreeNode.DEFAULT_COLOR);

        node.highlightPath();

        assertEquals(node.getColor(), TreeNode.HIGHLIGHT_PATH_COLOR);
    }

    @Test
    void testHighlightCompute(){
        TreeNode node = new TreeNode(1);
        assertEquals(node.getColor(), TreeNode.DEFAULT_COLOR);

        node.highlightCompute();

        assertEquals(node.getColor(), TreeNode.HIGHLIGHT_COMPUTATION_COLOR);
    }

    @Test
    void testGetAndResetColor(){
        TreeNode node = new TreeNode(1);
        node.highlightPath();
        assertEquals(node.getColor(), TreeNode.HIGHLIGHT_PATH_COLOR);

        node.getAndResetColor();
        assertEquals(node.getColor(), TreeNode.DEFAULT_COLOR);
    }

    @Test
    void testCopyConstructor(){
        TreeNode node1 = new TreeNode(1);
        node1.setColor(TreeNode.ANSI_BLACK);

        TreeNode node2 = new TreeNode(node1);
        assertEquals(node2.getVal(), 1);
        assertEquals(node2.getColor(), TreeNode.ANSI_BLACK);
    }
}
