package resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Unit tests for Tree Config class
public class TreeConfigTest {

    @Test
    void testConstants() {
        TreeConfig tConfig = new TreeConfig();

        assertTrue((tConfig.MAX_LEAVES > 1) && Integer.bitCount(TreeConfig.MAX_LEAVES) == 1);
        assertTrue(tConfig.MAX_TREE_NODES >= 4 * tConfig.MAX_LEAVES);
        assertTrue(tConfig.NODE_LEN >= 5);
    }
}
