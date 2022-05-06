package ui;

import model.Event;
import model.EventLog;
import model.SegmentTree;
import model.TreeNode;
import persistence.JsonSegTreeReader;
import persistence.JsonSegTreeWriter;
import renderer.ConsoleTreeRenderer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static resources.TreeConfig.MAX_LEAVES;

// Segment Tree Console Application
public class SegTreeConsoleApp {
    private static final String SEGTREE_JSON_STORE = "./data/segTreeFoundation.json";
    private SegmentTree segTree;
    private ConsoleTreeRenderer treeRenderer;
    private Scanner userInput;
    private JsonSegTreeWriter jsonWriter;
    private JsonSegTreeReader jsonReader;

    // EFFECTS: Runs the segment tree console application
    public SegTreeConsoleApp() {
        userInput = new Scanner(System.in);
        userInput.useDelimiter("\n");

        jsonWriter = new JsonSegTreeWriter(SEGTREE_JSON_STORE);
        jsonReader = new JsonSegTreeReader(SEGTREE_JSON_STORE);

        runSegTreeVisualizer();
    }

    /*
     * MODIFIES: this
     * EFFECTS: Display's logo, context-based menu;
     *          Display updated segment tree after every user operation
     */
    private void runSegTreeVisualizer() {
        displayLogo();

        while (true) {
            if (segTree == null) {
                if (!displayTreeSetupMenu()) {
                    break;
                }
            } else {
                if (!displayTreeFunctionsMenu()) {
                    break;
                }
            }

            displaySegTree();
        }

        for (Event next : EventLog.getInstance()) {
            System.out.println(next.toString() + "\n\n");
        }

        System.out.println("\nClosing down!");
    }

    /*
     * MODIFIES: this
     * EFFECTS: Display's segment tree setup menu
     */
    private boolean displayTreeSetupMenu() {
        System.out.println("\nSelect an option to initialize the Segment Tree: ");
        System.out.println("\t a -> Auto-Initialize with values from 1 to " + MAX_LEAVES);
        System.out.println("\t b -> Initialize with custom values");
        System.out.println("\t c -> Load the most recently saved Segment tree");

        if (segTree != null) {
            System.out.println("\t d -> Go back to previous menu");
        }

        System.out.println("\t q -> Quit");

        String curOption = userInput.next().toLowerCase();
        if (curOption.equals("a")) {
            initializeDefaultSegTree();
        } else if (curOption.equals("b")) {
            displayCustomInitializationMenu();
        } else if (curOption.equals("c")) {
            loadRecentTreeState();
        } else if (curOption.equals("q")) {
            return false;
        }

        return true;
    }


    /*
     * MODIFIES: this
     * EFFECTS: Displays functions that can be performed on the segment tree;
     */
    private boolean displayTreeFunctionsMenu() {
        System.out.println("\nSelect any function to perform on the Segment Tree: ");
        System.out.println("\t a -> Find sum/product of a given range in the initial array");
        System.out.println("\t b -> Update any value in the initial array");
        System.out.println("\t c -> Use a different merge function to compute the tree");
        System.out.println("\t d -> Save the current segment tree (Overrides the previously saved tree state)");
        System.out.println("\t e -> Initialize a new segment tree");
        System.out.println("\t q -> Quit");

        String curOption = userInput.next().toLowerCase();

        if (curOption.equals("a")) {
            displayRangeFunctionMenu();
        } else if (curOption.equals("b")) {
            displayUpdateValueMenu();
        } else if (curOption.equals("c")) {
            displayUpdateMergeFnMenu();
        } else if (curOption.equals("d")) {
            saveCurrentTreeState();
        } else if (curOption.equals("e")) {
            displayTreeSetupMenu();
        } else if (curOption.equals("q")) {
            return false;
        }

        return true;
    }


    /*
     * Allows the user to input multiple values with which the tree can be initialized
     * MODIFIES: this
     * EFFECTS: Gets user input and initializes the tree
     */
    private void displayCustomInitializationMenu() {
        System.out.println("\nEnter until " + MAX_LEAVES + " distinct values separated by a space: ");
        System.out.println("(If fewer values are entered, the remaining values will be initialized to 0)");

        String[] values = userInput.next().split(" ");
        ArrayList<TreeNode> intValues = new ArrayList<>();
        for (String i : values) {
            try {
                int curVal = Integer.parseInt(i);
                intValues.add(new TreeNode(curVal));
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid values entered. Please try entering again: ");
                displayCustomInitializationMenu();
            }
        }

        int padding = MAX_LEAVES - intValues.size();
        while (padding-- > 0) {
            intValues.add(new TreeNode(0));
        }

        segTree = new SegmentTree(intValues);
    }

    /*
     * EFFECTS: Allows user to query range sum/product and returns the result
     */
    private void displayRangeFunctionMenu() {
        System.out.println("\nEnter a range between 1 and " + MAX_LEAVES + " (Format: l r) separated by space,"
                + " to query the sum/product for: ");

        String[] values = userInput.next().split(" ");
        try {
            int left = Integer.parseInt(values[0]);
            int right = Integer.parseInt(values[1]);

            left--;
            right--;

            if (!(left >= 0 && right < MAX_LEAVES && left <= right)) {
                throw new Exception();
            }

            int rangeMergeVal = segTree.rangeSumProduct(left, right);
            System.out.println("\nSum/Product of the specified range = " + rangeMergeVal);

        } catch (Exception e) {
            System.out.println("\nInvalid values entered. Please try entering again");
            displayRangeFunctionMenu();
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: Displays the menu and gets user input to update a tree for a given array index;
     */
    private void displayUpdateValueMenu() {
        System.out.println("\nEnter the position(1-based index) of the leaf node "
                + "in the tree you would like to update: ");
        int updatePos = userInput.nextInt();
        updatePos--;

        System.out.println(("\nEnter the value you would like to update it to: "));
        int updateVal = userInput.nextInt();

        segTree.updateVal(updatePos, updateVal);
        System.out.println("\n Updated value!");
    }

    /*
     * MODIFIES: this
     * EFFECTS: Toggles the merge function used in the tree based on the user input
     */
    private void displayUpdateMergeFnMenu() {
        String curMergeFn = segTree.getCurMergeFn();
        System.out.println("\nWould you like to switch the current merge function? (Enter y/n) ");
        System.out.println("Current Merge Fn: " + curMergeFn);
        System.out.println("New Merge Fn: " + (curMergeFn.equals("Addition") ? "Multiplication" : "Addition"));

        String isSwitch = userInput.next().toLowerCase();
        if (isSwitch.equals("y")) {
            segTree.flipMergeFn();
            System.out.println("\n Updated merge Function!");
        }
    }


    private void saveCurrentTreeState() {
        try {
            jsonWriter.open();
            jsonWriter.write(segTree);
            jsonWriter.close();
            System.out.println("\n Saved current Tree State!");
        } catch (FileNotFoundException e) {
            System.out.println("\n Unable to write to file: " + SEGTREE_JSON_STORE + " :(");
        }
    }

    private void loadRecentTreeState() {
        try {
            segTree = jsonReader.read();
            System.out.println("\n Loaded saved tree successfully!");
        } catch (IOException e) {
            System.out.println("\n Unable to read from file: " + SEGTREE_JSON_STORE + " :(");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: Initializes a segment tree with default values from 1 to MAX_LEAVES
     */
    private void initializeDefaultSegTree() {
        ArrayList<TreeNode> arr = new ArrayList<>();
        for (int i = 0; i < MAX_LEAVES; i++) {
            arr.add(new TreeNode(i + 1));
        }
        segTree = new SegmentTree(arr);
    }

    /*
     * EFFECTS: Renders the current state of the segment tree
     */
    private void displaySegTree() {
        if (segTree == null) {
            return;
        }

        treeRenderer = new ConsoleTreeRenderer(segTree);
        System.out.println("\n\n\nSegment Tree (Current Merge fn: " + segTree.getCurMergeFn() + " )\n");

        System.out.println(treeRenderer.render());

        System.out.println("Legend: ");
        System.out.println("Blue -> Highlights path");
        System.out.println("Red  -> Highlights nodes used for computing results/performing updates");
    }

    /*
     * EFFECTS: Displays an ASCII art logo
     */
    private void displayLogo() {
        // Generated ASCII logo from the following website:
        // [https://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20]
        String logo = "\n"
                + " _____                                 _     _____               _   _ _         "
                + "        _ _              \n"
                + "/  ___|                               | |   |_   _|             | | | (_)          "
                + "     | (_)             \n"
                + "\\ `--.  ___  __ _ _ __ ___   ___ _ __ | |_    | |_ __ ___  ___  | | | |_ ___ _   _ "
                + " __ _| |_ _______ _ __ \n"
                + " `--. \\/ _ \\/ _` | '_ ` _ \\ / _ \\ '_ \\| __|   | | '__/ _ \\/ _ \\ | | | | / __|"
                + " | | |/ _` | | |_  / _ \\ '__|\n"
                + "/\\__/ /  __/ (_| | | | | | |  __/ | | | |_    | | | |  __/  __/ \\ \\_/ / \\__ \\ |_|"
                + " | (_| | | |/ /  __/ |   \n"
                + "\\____/ \\___|\\__, |_| |_| |_|\\___|_| |_|\\__|   \\_/_|  \\___|\\___|  \\___/|_|___/"
                + "\\__,_|\\__,_|_|_/___\\___|_|   \n"
                + "             __/ |                                                                    "
                + "                    \n"
                + "            |___/                                                                       "
                + "                  \n"
                + "                                                                                         "
                + "                 \n";
        System.out.println(logo);
    }
}
