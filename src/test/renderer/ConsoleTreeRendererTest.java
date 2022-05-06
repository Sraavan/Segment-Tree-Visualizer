package renderer;

import model.SegmentTree;
import model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static resources.TreeConfig.MAX_LEAVES;
import static resources.TreeConfig.NODE_LEN;

// Unit tests for ConsoleTreeRenderer class
public class ConsoleTreeRendererTest {
    private ConsoleTreeRenderer renderer;

    @BeforeEach
    void runBefore() {
        ArrayList<TreeNode> arr = new ArrayList<>();
        for (int i = 1; i <= MAX_LEAVES; i++) {
            arr.add(new TreeNode(i));
        }
        SegmentTree segTree = new SegmentTree(arr);

        renderer = new ConsoleTreeRenderer(segTree);
    }

    @Test
    void testRenderLeafNodes() {
        String renderedString = renderer.render();
        String[] levels = renderedString.split("\n");

        for (String level : levels) {
            String nonColoredLevel = removeColor(level);
            assertEquals(nonColoredLevel.length(), 2 * 16 * NODE_LEN);
        }

        String paddedLeaves2 = "  1         2  ";
        String nonColoredLeaves = removeColor(levels[levels.length - 1]);
        assertEquals(nonColoredLeaves.substring(0, 3 * NODE_LEN), paddedLeaves2);

    }

    @Test
    void testRenderRootNode() {
        String renderedString = renderer.render();
        String[] levels = renderedString.split("\n");

        String level2 = "                 /----------------- 36  -----------------\\                "
                + "                       /----------------- 100 -----------------\\                      ";

        String nonColoredLevel1 = removeColor(levels[1]);
        assertEquals(nonColoredLevel1, level2);

    }

    /*
     * Code Reference: [https://stackoverflow.com/a/14652763]
     * EFFECTS: Removes the ASCII color codes from a given string
     */
    private String removeColor(String s){
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
