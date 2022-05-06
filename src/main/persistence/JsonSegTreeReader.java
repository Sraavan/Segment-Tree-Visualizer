package persistence;

import model.SegmentTree;
import model.TreeNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
// Code reference: [https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo]
public class JsonSegTreeReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonSegTreeReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public SegmentTree read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSegTree(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses all leaf nodes and merge function from JSON object and constructs a segment tree and returns it
    public SegmentTree parseSegTree(JSONObject jsonObject) {
        JSONArray leafNodesJsonArray = jsonObject.getJSONArray("leaves");
        String mergeFunc = jsonObject.getString("mergeFunc");

        ArrayList<TreeNode> leaves = new ArrayList<>();
        for (Object json : leafNodesJsonArray) {
            JSONObject leafNode = (JSONObject) json;
            leaves.add(parseTreeNode(leafNode));
        }

        SegmentTree st = new SegmentTree(leaves);
        if (!st.getCurMergeFn().equals(mergeFunc)) {
            st.flipMergeFn();
        }
        return st;
    }

    // EFFECTS: parses tree node from JSON object and returns it
    public TreeNode parseTreeNode(JSONObject jsonObject) {
        int value = jsonObject.getInt("value");
        String color = jsonObject.getString("color");

        TreeNode t = new TreeNode(value);
        t.setColor(color);

        return t;
    }

}
