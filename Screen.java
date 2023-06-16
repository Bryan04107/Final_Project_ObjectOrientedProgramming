import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Screen extends JFrame {
    private final CardLayout cardLayout;
    private final JTextField sizeInput, blockInput, startInputX, startInputY, endInputX, endInputY, templateNum;
    protected NodeType[][] node;
    protected NodeType startNode, endNode;
    protected boolean toggleCost, toggleMode;
    private static int maxSize = 30, blockProbability = 30, startPosX= 4, startPosY = 4, endPosX = 25, endPosY = 25, step, finalStep;
    private static long timeTaken;
    private final JLabel label1 = new JLabel(" Time Taken: " + timeTaken + "ms.");
    private final JLabel label2 = new JLabel(" Tiles Checked: " + step + ".");
    private final JLabel label3 = new JLabel(" Tiles to Goal: " + finalStep + ".");

    public static int getMaxSize() {
        return maxSize;
    }
    public static void setMaxSize(int maxSize) {
        Screen.maxSize = maxSize;
    }
    public static int getStep() {
        return step;
    }
    public static void setStep(int step) {
        Screen.step = step;
    }
    public static int getFinalStep() {
        return finalStep;
    }
    public static void setFinalStep(int finalStep) {
        Screen.finalStep = finalStep;
    }
    public static int getTimeTaken() {
        return (int) timeTaken;
    }
    public static void setTimeTaken(int timeTaken) {
        Screen.timeTaken = timeTaken;
    }

    public Screen() {
        // Screen Properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // Constructors
        JPanel gridPanel = new JPanel(new GridLayout(10, 10));
        gridPanel.setPreferredSize(new Dimension(750, 750));
        Grid grid = new Grid(gridPanel, this);
        GridRecord statRecord = new GridRecord(this);

        // Scene 1
        JPanel scene1 = new JPanel();
        scene1.setLayout(new GridBagLayout());

        // Title
        JLabel titleLabel = new JLabel("Algorithm Visualizer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Set font size and style

        // Start Button
        JButton startButton = new JButton("Start!");
        startButton.setFont(new Font("Arial", Font.BOLD, 24)); // Set a larger font size
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(getContentPane(), "2");
            }
        });

        // Layout For Scene 1
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.anchor = GridBagConstraints.CENTER;
        gbc1.insets = new Insets(0, 0, 20, 0);
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        scene1.add(titleLabel, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy = 1;
        scene1.add(startButton, gbc1);


        // Scene 2
        JPanel scene2 = new JPanel(new BorderLayout());

        // Inputs Panel
        JPanel inputsPanel = new JPanel(new GridBagLayout());
        inputsPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
        inputsPanel.setBackground(Color.BLACK);

        // Toggle Button To Swap Between A* or Dijkstra Pathfinding
        JToggleButton togglePathFind = new JToggleButton("A*");
        togglePathFind.addActionListener(e -> {
            toggleMode = !toggleMode;
            if (toggleMode) {
                togglePathFind.setText("Dijkstra");
            } else {
                togglePathFind.setText("A*");
            }
        });

        // Toggle Button To Swap Not Displaying or Displaying The Cost
        JToggleButton toggleCostDisplay = new JToggleButton("Display Cost");
        toggleCostDisplay.addActionListener(e -> {
            toggleCost = !toggleCost;
            for (int col = 0; col < maxSize; col++) {
                for (int row = 0; row < maxSize; row++) {
                    NodeType currentNode = node[col][row];
                    if (toggleCost) {
                        currentNode.setText("<html><span style='font-size: 7px;'>F:" + currentNode.fCost + "<br>G:" + currentNode.gCost + "</html>");
                    } else {
                        currentNode.setText("");
                        if(currentNode == startNode){
                            currentNode.setText("S");
                        }
                        if(currentNode == endNode){
                            currentNode.setText("E");
                        }
                    }
                }
            }
        });

        // Runs One of The Pathfinding Algorithm When Pressed
        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> {
            if (togglePathFind.getText().equals("A*")) {
                AStarPathFinding aStarPathFinder = new AStarPathFinding(node, startNode, endNode, maxSize, this);
                aStarPathFinder.InstantPathFind();
                UpdatePanel((int) timeTaken, step, finalStep);
            } else {
                DijkstraPathFinding dijkstraPathFinder = new DijkstraPathFinding(node, startNode, endNode, maxSize, this);
                dijkstraPathFinder.InstantPathFind();
                UpdatePanel((int) timeTaken, step, finalStep);
            }
            runButton.setEnabled(false);
        });

        // Creates Text Fields For User Input
        sizeInput = new JTextField("30");
        blockInput = new JTextField("30");
        startInputX = new JTextField("5");
        startInputY = new JTextField("5");
        endInputX = new JTextField("26");
        endInputY = new JTextField("26");

        // Generates a Grid
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> {
            try {
                int tempSize = Integer.parseInt(sizeInput.getText());
                int tempBlockInput = Integer.parseInt(blockInput.getText());
                int tempStartX = Integer.parseInt(startInputX.getText())-1;
                int tempStartY = Integer.parseInt(startInputY.getText())-1;
                int tempEndX = Integer.parseInt(endInputX.getText())-1;
                int tempEndY = Integer.parseInt(endInputY.getText())-1;

                if (tempSize > 0) {
                    if (tempBlockInput >= 0 && tempBlockInput <= 100) {
                        if(((tempStartX <= tempSize) && (tempStartY <= tempSize) && (tempEndX <= tempSize) && (tempEndY <= tempSize) && !((tempStartX == tempEndX) && (tempStartY == tempEndY)))){
                            maxSize = tempSize;
                            blockProbability = tempBlockInput;
                            startPosX = tempStartX;
                            startPosY = tempStartY;
                            endPosX = tempEndX;
                            endPosY = tempEndY;
                            grid.GenerateGrid(maxSize, blockProbability, startPosX, startPosY, endPosX, endPosY);
                            runButton.setEnabled(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Start and End must be within the grid and not overlapping.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Block probability must be between 0 to 100.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Grid size must be greater than zero.");
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(null,
                        "Invalid input. Please enter valid row, column, and block probability values.");
            }
        });

        // Button to Return to The Menu
        JButton returnButton = new JButton("Return to Menu");
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(getContentPane(), "1");
            }
        });

        // Button and Input For Inputting and Loading Templates
        templateNum = new JTextField();
        JButton gen2Button = new JButton("Load");
        gen2Button.addActionListener(e -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("Grids.txt"));
                String line;
                int gridCount = 0;
                while ((line = reader.readLine()) != null) {
                    if (line.equals("#")) {
                        gridCount++;
                    }
                }
                if (Integer.parseInt(templateNum.getText()) > 0 && Integer.parseInt(templateNum.getText()) <= gridCount) {
                    runButton.setEnabled(true);
                    int tempTemplateNum = Integer.parseInt(templateNum.getText());
                    grid.GenerateGrid("Grids.txt", tempTemplateNum);
                } else {
                    JOptionPane.showMessageDialog(null, "There is only " + gridCount + " grids saved.");
                }
            } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid number.");
            }
        });

        // Button To Save Current Grid
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> statRecord.Save());

        // Layout For Inputs Panel
        GridBagConstraints gbcA = new GridBagConstraints();
        gbcA.anchor = GridBagConstraints.WEST;
        gbcA.fill = GridBagConstraints.HORIZONTAL;
        gbcA.weightx = 1.0;
        gbcA.insets = new Insets(5, 5, 5, 5);
        gbcA.gridx = 0;
        gbcA.gridy = 0;
        inputsPanel.add(togglePathFind, gbcA);

        gbcA.gridy = 1;
        inputsPanel.add(toggleCostDisplay, gbcA);


        JPanel sizePanel = new JPanel(new GridBagLayout());
        sizePanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
        sizePanel.setBackground(Color.BLACK);
        GridBagConstraints gbcASize = new GridBagConstraints();
        gbcASize.anchor = GridBagConstraints.WEST;
        gbcASize.fill = GridBagConstraints.HORIZONTAL;
        gbcASize.insets = new Insets(0, 5, 0, 5);
        gbcASize.gridx = 0;
        gbcASize.gridy = 0;
        sizePanel.add(new JLabel("<html><font color='white'>Grid Size</html>"), gbcASize);
        gbcASize.weightx = 1.0;
        gbcASize.gridx = 1;
        gbcASize.gridy = 0;
        sizePanel.add(sizeInput, gbcASize);

        gbcA.gridy = 2;
        inputsPanel.add(sizePanel, gbcA);


        JPanel blockPanel = new JPanel(new GridBagLayout());
        blockPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
        blockPanel.setBackground(Color.BLACK);
        GridBagConstraints gbcABlock = new GridBagConstraints();
        gbcABlock.anchor = GridBagConstraints.WEST;
        gbcABlock.fill = GridBagConstraints.HORIZONTAL;
        gbcABlock.insets = new Insets(0, 5, 0, 5);
        gbcABlock.gridx = 0;
        gbcABlock.gridy = 0;
        blockPanel.add(new JLabel("<html><font color='white'>Wall Chance</html>"), gbcABlock);
        gbcABlock.weightx = 1.0;
        gbcABlock.gridx = 1;
        gbcABlock.gridy = 0;
        blockPanel.add(blockInput, gbcABlock);

        gbcA.gridy = 3;
        inputsPanel.add(blockPanel, gbcA);


        JPanel startPanel1 = new JPanel(new GridBagLayout());
        startPanel1.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
        startPanel1.setBackground(Color.BLACK);
        startPanel1.add(new JLabel("<html><font color='white'>Start Cords</html>"), gbcA);

        gbcA.gridy = 4;
        inputsPanel.add(startPanel1, gbcA);


        JPanel startPanel2 = new JPanel(new GridBagLayout());
        startPanel2.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
        startPanel2.setBackground(Color.BLACK);
        GridBagConstraints gbcAStart = new GridBagConstraints();
        gbcAStart.anchor = GridBagConstraints.WEST;
        gbcAStart.fill = GridBagConstraints.HORIZONTAL;
        gbcAStart.insets = new Insets(0, 5, 0, 5);
        gbcAStart.gridx = 0;
        gbcAStart.gridy = 0;
        gbcAStart.weightx = 1.0;
        startPanel2.add(startInputX, gbcAStart);
        gbcAStart.gridx = 1;
        gbcAStart.gridy = 0;
        startPanel2.add(startInputY, gbcAStart);

        gbcA.gridy = 5;
        inputsPanel.add(startPanel2, gbcA);


        JPanel endPanel1 = new JPanel(new GridBagLayout());
        endPanel1.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
        endPanel1.setBackground(Color.BLACK);
        endPanel1.add(new JLabel("<html><font color='white'>End Cords</html>"), gbcA);

        gbcA.gridy = 6;
        inputsPanel.add(endPanel1, gbcA);


        JPanel endPanel2 = new JPanel(new GridBagLayout());
        endPanel2.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
        endPanel2.setBackground(Color.BLACK);
        GridBagConstraints gbcAEnd = new GridBagConstraints();
        gbcAEnd.anchor = GridBagConstraints.WEST;
        gbcAEnd.fill = GridBagConstraints.HORIZONTAL;
        gbcAEnd.insets = new Insets(0, 5, 0, 5);
        gbcAEnd.gridx = 0;
        gbcAEnd.gridy = 0;
        gbcAEnd.weightx = 1.0;
        endPanel2.add(endInputX, gbcAEnd);
        gbcAEnd.gridx = 1;
        gbcAEnd.gridy = 0;
        endPanel2.add(endInputY, gbcAEnd);

        gbcA.gridy = 7;
        inputsPanel.add(endPanel2, gbcA);

        gbcA.gridy = 8;
        inputsPanel.add(runButton, gbcA);

        gbcA.gridy = 9;
        inputsPanel.add(generateButton, gbcA);


        JPanel loadPanel = new JPanel(new GridBagLayout());
        loadPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
        loadPanel.setBackground(Color.BLACK);
        GridBagConstraints gbcANum = new GridBagConstraints();
        gbcANum.anchor = GridBagConstraints.WEST;
        gbcANum.fill = GridBagConstraints.HORIZONTAL;
        gbcANum.insets = new Insets(0, 5, 0, 5);
        gbcANum.gridx = 0;
        gbcANum.gridy = 0;
        gbcANum.weightx = 1.0;
        loadPanel.add(gen2Button, gbcANum);
        gbcANum.gridx = 1;
        gbcANum.gridy = 0;
        loadPanel.add(templateNum, gbcANum);

        gbcA.gridy = 10;
        gbcA.insets = new Insets(50, 5, 5, 5);
        inputsPanel.add(loadPanel, gbcA);

        gbcA.gridy = 11;
        gbcA.insets = new Insets(5, 5, 5, 5);
        inputsPanel.add(saveButton, gbcA);

        gbcA.gridy = 12;
        gbcA.insets = new Insets(100, 5, 5, 5);
        inputsPanel.add(returnButton, gbcA);



        // Displays The Grid And 3 Additional Labels
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.add(gridPanel, BorderLayout.CENTER);
        displayPanel.setBackground(Color.BLACK);

        // Creates a Grid
        grid.GenerateGrid(maxSize, blockProbability, startPosX, startPosY, endPosX, endPosY);


        // Creates and Adds New Labels Under The Grid
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 50));
        label1.setFont(new Font("Arial", Font.PLAIN, 15));
        label1.setBorder(new LineBorder(Color.BLACK));

        label2.setFont(new Font("Arial", Font.PLAIN, 15));
        label2.setBorder(new LineBorder(Color.BLACK));

        label3.setFont(new Font("Arial", Font.PLAIN, 15));
        label3.setBorder(new LineBorder(Color.BLACK));

        bottomPanel.add(label1);
        bottomPanel.add(label2);
        bottomPanel.add(label3);
        bottomPanel.setBackground(Color.WHITE);
        displayPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Adds The Inputs Panel and The Grid Into Scene 2
        scene2.add(inputsPanel, BorderLayout.WEST);
        scene2.add(displayPanel, BorderLayout.CENTER);


        // Create Card Layouts to Swap Between
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        getContentPane().add(scene1, "1");
        getContentPane().add(scene2, "2");

        // Shows The Title Screen
        cardLayout.show(getContentPane(), "1");
    }

    //Updating The Panels Label
    public void UpdatePanel(int timeTaken, int step, int finalStep) {
        label1.setText(" Time Taken: " + timeTaken + "ms.");
        label2.setText(" Tiles Checked: " + step + ".");
        label3.setText(" Tiles to Goal: " + finalStep + ".");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Screen test = new Screen();
            test.setVisible(true);
        });
    }
}