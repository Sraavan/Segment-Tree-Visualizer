package persistence;

import model.SegmentTree;
import org.junit.jupiter.api.Test;
import resources.TreeConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Unit tests for the JsonSegTreeReader class
public class JsonSegTreeReaderTest {

    // Code reference: [https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo]
    @Test
    void testReaderNonExistentFile() {
        JsonSegTreeReader reader = new JsonSegTreeReader("./data/random.json");
        try {
            SegmentTree st = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    void testReaderSegTreeAddFn() {
        JsonSegTreeReader reader = new JsonSegTreeReader("./data/segTreeAddn.json");
        try {
            SegmentTree st = reader.read();
            assertEquals(st.rangeSumProduct(1, 2), 5);
            assertEquals(st.getLeaf(0).getColor(), "\u001b[47m\u001b[30m");
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderSegTreeProductFn() {
        JsonSegTreeReader reader = new JsonSegTreeReader("./data/segTreeMult.json");
        try {
            SegmentTree st = reader.read();
            assertEquals(st.rangeSumProduct(1, 2), 6);
            assertEquals(st.getLeaf(0).getColor(), "\u001b[47m\u001b[30m");
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
