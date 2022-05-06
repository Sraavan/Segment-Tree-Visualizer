package ui;

import model.Event;
import model.EventLog;
import model.SegmentTree;
import model.TreeNode;
import persistence.JsonSegTreeReader;
import persistence.JsonSegTreeWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static resources.TreeConfig.MAX_LEAVES;

// Segment Tree UI application
public class SegTreeApp extends JPanel implements ActionListener, ItemListener {

    // Tracks the current segment tree to be rendered
    private SegmentTree segTree;

    private TreeRenderer guiTreeRenderer;

    private static final String SEGTREE_JSON_STORE = "./data/segTreeFoundation.json";
    private JsonSegTreeWriter jsonWriter;
    private JsonSegTreeReader jsonReader;

    // GUI Variables
    private JScrollPane scrollPane;
    private JPanel actionCards;
    private JLabel segTreeRenderLbl;
    private JLabel sumOutputTextLbl;
    private JLabel sumOutputLbl;
    private JTextField generateValuesTF;
    private JTextField updateValuePosTF;
    private JTextField updateValueValTF;
    private JTextField sumLeftRangeTF;
    private JTextField sumRightRangeTF;

    private static final String ADD_CARD_STR = "Generate new Seg Tree";
    private static final String UPDATE_CARD_STR = "Update a value in the current tree";
    private static final String SUM_CARD_STR = "Sum of values in range";
    public static final String SAVE_MENU_STR = "Save";
    public static final String LOAD_MENU_STR = "Load";

    /*
     * EFFECTS: Sets up the app frame and lays out panels
     */
    public SegTreeApp() {
        super(new BorderLayout());

        jsonWriter = new JsonSegTreeWriter(SEGTREE_JSON_STORE);
        jsonReader = new JsonSegTreeReader(SEGTREE_JSON_STORE);

        initializeDefaultSegTree();
        guiTreeRenderer = new TreeRenderer(this.segTree);

        this.setupContentPane();

        // Display UI once all the contents are setup
        this.createAndShowGUI();
    }

    /*
     * EFFECTS: Create the GUI and show it.  For thread safety,
     *          this method should be invoked from the
     *          event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Segment Tree Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setJMenuBar(getMenus());
        frame.getContentPane().add(this);
        frame.getContentPane().setSize(40, 100);

        frame.setSize(1000, 400);
        frame.setVisible(true);

        //Src: [https://stackoverflow.com/a/5824133]
        //Overrides close btn on jFrame to write logged events
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.toString() + "\n\n");
                }
                ((JFrame)(e.getComponent())).dispose();
            }
        });
    }

    /*
     * EFFECTS: Creates the menu for the app
     */
    private JMenuBar getMenus() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        JMenuItem saveMenuItem = new JMenuItem(SAVE_MENU_STR);
        saveMenuItem.setActionCommand(SAVE_MENU_STR);
        saveMenuItem.addActionListener(this);
        menu.add(saveMenuItem);

        JMenuItem loadMenuItem = new JMenuItem(LOAD_MENU_STR);
        loadMenuItem.setActionCommand(LOAD_MENU_STR);
        loadMenuItem.addActionListener(this);
        menu.add(loadMenuItem);

        return menuBar;
    }

    /*
     * MODIFIES: this
     * EFFECTS: Adds the panels making up the content pane
     */
    private void setupContentPane() {
        // Add tree to the center
        add(guiTreeRenderer, BorderLayout.CENTER);
        add(getSegTreeActionsPanel(), BorderLayout.SOUTH);
    }

    /*
     * EFFECTS: Setups the action panel for tree functions
     */
    private JPanel getSegTreeActionsPanel() {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        actionCards = new JPanel(new CardLayout());

        JPanel comboBoxPanel = new JPanel(); //use FlowLayout
        String[] comboBoxItems = {ADD_CARD_STR, UPDATE_CARD_STR, SUM_CARD_STR};
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPanel.add(cb);

        // Add new values
        actionCards.add(getAddValuesPanel(), ADD_CARD_STR);
        // Update values
        actionCards.add(getUpdateValuesPanel(), UPDATE_CARD_STR);
        // Sum values
        actionCards.add(getSumValuesPanel(), SUM_CARD_STR);

        boxPanel.add(comboBoxPanel);
        boxPanel.add(actionCards);

        return boxPanel;
    }

    /*
     * EFFECTS: Returns the card panel for initializing a new seg tree
     */
    private JPanel getAddValuesPanel() {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        JLabel addText = new JLabel("<html>Enter until " + MAX_LEAVES + " distinct values separated by a space: "
                + "</br>(If fewer values are entered, the remaining values will be initialized to 0)</br></html>");

        generateValuesTF = new JTextField(20);
        Dimension d1 = generateValuesTF.getPreferredSize();
        generateValuesTF.setMaximumSize(d1);

        JButton updateBtn = new JButton("Generate");
        updateBtn.setActionCommand(ADD_CARD_STR);
        updateBtn.addActionListener(this);

        addText.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateValuesTF.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        boxPanel.add(addText);
        boxPanel.add(generateValuesTF);
        boxPanel.add(updateBtn);
        return boxPanel;
    }

    /*
     * EFFECTS: Returns the card panel for updating a seg tree
     */
    private JPanel getUpdateValuesPanel() {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        JLabel valueText = new JLabel("Enter the position(1-based index) of the leaf node "
                + "in the tree you would like to update:");
        updateValuePosTF = new JTextField(20);
        Dimension d1 = updateValuePosTF.getPreferredSize();
        updateValuePosTF.setMaximumSize(d1);

        JLabel updateText = new JLabel("Enter the value you would like to update it to: ");
        updateValueValTF = new JTextField(20);
        Dimension d2 = updateValueValTF.getPreferredSize();
        updateValueValTF.setMaximumSize(d2);

        JButton updateBtn = new JButton("Update values");
        updateBtn.setActionCommand(UPDATE_CARD_STR);
        updateBtn.addActionListener(this);

        boxPanel.add(valueText);
        boxPanel.add(updateValuePosTF);
        boxPanel.add(updateText);
        boxPanel.add(updateValueValTF);
        boxPanel.add(updateBtn);

        return boxPanel;
    }

    /*
     * EFFECTS: Returns the card panel for summing up values in a given range in the current seg tree
     */
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private JPanel getSumValuesPanel() {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        JLabel sumText = new JLabel("Enter a range between 1 and " + MAX_LEAVES
                + " to query the sum/product for");

        JLabel leftText = new JLabel("Left range");
        sumLeftRangeTF = new JTextField(20);
        Dimension d1 = sumLeftRangeTF.getPreferredSize();
        sumLeftRangeTF.setMaximumSize(d1);

        JLabel rightText = new JLabel("Right range");
        sumRightRangeTF = new JTextField(20);
        Dimension d2 = sumRightRangeTF.getPreferredSize();
        sumRightRangeTF.setMaximumSize(d2);

        JButton updateBtn = new JButton("Calculate");
        updateBtn.setActionCommand(SUM_CARD_STR);
        updateBtn.addActionListener(this);

        sumOutputTextLbl = new JLabel("Sum of the values in the given range: ");
        sumOutputLbl = new JLabel();
        sumOutputTextLbl.setVisible(false);
        sumOutputLbl.setVisible(false);

        for (JComponent i : new JComponent[]{sumText, leftText, sumLeftRangeTF, rightText, sumRightRangeTF,
                updateBtn, sumOutputTextLbl, sumOutputLbl}) {
            i.setAlignmentX(Component.LEFT_ALIGNMENT);
            boxPanel.add(i);
        }

        return boxPanel;
    }

    /*
     * MODIFIES: this
     * EFFECTS: Performs the assigned button action on the current seg tree
     */
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (ADD_CARD_STR.equals(command)) {
            //Add button clicked
            String[] values = generateValuesTF.getText().split(" ");
            ArrayList<TreeNode> intValues = new ArrayList<>();
            for (String i : values) {
                try {
                    int curVal = Integer.parseInt(i);
                    intValues.add(new TreeNode(curVal));
                } catch (NumberFormatException err) {
                    System.out.println("\nInvalid values entered. Please try entering again: ");
                }
            }

            int padding = MAX_LEAVES - intValues.size();
            while (padding-- > 0) {
                intValues.add(new TreeNode(0));
            }

            segTree = new SegmentTree(intValues);
            guiTreeRenderer.updateAndRenderSegTree(segTree);
        } else if (UPDATE_CARD_STR.equals(command)) {
            int updatePos = Integer.parseInt(updateValuePosTF.getText());
            updatePos--;
            int updateVal = Integer.parseInt(updateValueValTF.getText());

            segTree.updateVal(updatePos, updateVal);
            guiTreeRenderer.updateAndRenderSegTree(segTree);
        } else if (SUM_CARD_STR.equals(command)) {
            int l = Integer.parseInt(sumLeftRangeTF.getText());
            int r = Integer.parseInt(sumRightRangeTF.getText());
            l--;
            r--;
            int sum = segTree.rangeSumProduct(l, r);
            sumOutputLbl.setText(Integer.toString(sum));
            sumOutputTextLbl.setVisible(true);
            sumOutputLbl.setVisible(true);
        } else if (SAVE_MENU_STR.equals(command)) {
            try {
                jsonWriter.open();
                jsonWriter.write(segTree);
                jsonWriter.close();
            } catch (FileNotFoundException err) {
                System.out.println("\n Unable to write to file: " + SEGTREE_JSON_STORE + " :(");
            }
        } else if (LOAD_MENU_STR.equals(command)) {
            try {
                segTree = jsonReader.read();
                guiTreeRenderer.updateAndRenderSegTree(segTree);
            } catch (IOException err) {
                System.out.println("\n Unable to read from file: " + SEGTREE_JSON_STORE + " :(");
            }
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: Updates the tree action panel based on user selection
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout) (actionCards.getLayout());
        cl.show(actionCards, (String) e.getItem());
    }

    /*
     * MODIFIES: this
     * EFFECTS: Initializes a default seg tree with values from 1 - MAX_LEAVES
     */
    private void initializeDefaultSegTree() {
        ArrayList<TreeNode> arr = new ArrayList<>();
        for (int i = 0; i < MAX_LEAVES; i++) {
            arr.add(new TreeNode(i + 1));
        }
        this.segTree = new SegmentTree(arr);
    }

}