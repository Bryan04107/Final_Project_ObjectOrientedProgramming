import javax.swing.JPanel;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Grid {
    private final JPanel gridPanel;
    private final Screen test;

    public Grid(JPanel gridPanel, Screen test) {
        this.gridPanel = gridPanel;
        this.test = test;
    }

    //Generates A Grid With User Input
    public void GenerateGrid(int maxSize, int blockProbability, int StartNodeX, int StartNodeY, int EndNodeX, int EndNodeY) {
        if (gridPanel != null) {
            gridPanel.removeAll();

            gridPanel.setLayout(new GridLayout(maxSize, maxSize)); // Set the new grid layout

            // Create and add nodes to the grid panel
            test.node = new NodeType[maxSize][maxSize];
            for (int col = 0; col < maxSize; col++) {
                for (int row = 0; row < maxSize; row++) {
                    test.node[col][row] = new NodeType(col, row);
                    gridPanel.add(test.node[col][row]);

                    // Generate walls randomly based on blockProbability value
                    if (blockProbability > 0 && blockProbability <= 100) {
                        int randomNum = (int) (Math.random() * 100) + 1;
                        if (randomNum <= blockProbability) {
                            if (!(col == StartNodeX && row == StartNodeY) && !(col == EndNodeX && row == EndNodeY)) {
                                test.node[col][row].IsWall();
                            }
                        }
                    }
                }
            }

            // Revalidate and repaint the grid panel to reflect the changes
            SetStartNode(StartNodeX, StartNodeY);
            SetEndNode(EndNodeX, EndNodeY);
            NodeCost nodeCost = new NodeCost(test.node, test.startNode, test.endNode, maxSize, test);
            nodeCost.SetNodesCost();
            
            test.UpdatePanel(Screen.getTimeTaken(), Screen.getStep(), Screen.getFinalStep());
            gridPanel.revalidate();
            gridPanel.repaint();
        }
    }

    //Loads A Grid From Previously Saved Template
    public void GenerateGrid(String filename, int templateNum) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            gridPanel.removeAll();
            String line;
            int row = 0;
            int currentTemplateNum = 0;
            while ((line = reader.readLine()) != null) {
                if (line.equals("#")) {
                    currentTemplateNum++;
                    continue;
                }
                if (currentTemplateNum == templateNum) {
                    for (int col = 0; col < line.length(); col++) {
                        test.node[col][row] = new NodeType(col, row);
                        gridPanel.add(test.node[col][row]);
                        char c = line.charAt(col);
                        if (c == '0') {
                            test.node[col][row].IsWall();
                        } else if (c == 'S') {
                            SetStartNode(col, row);
                        } else if (c == 'E') {
                            SetEndNode(col, row);
                        }
                    }
                    row++;
                }
            }
            Screen.setMaxSize(row);
            NodeCost nodeCost = new NodeCost(test.node, test.startNode, test.endNode, row, test);
            nodeCost.SetNodesCost();
            gridPanel.setLayout(new GridLayout(row, row));
            gridPanel.revalidate();
            gridPanel.repaint();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Sets The StartNode
    private void SetStartNode(int col, int row) {
        test.node[col][row].IsStart();
        test.startNode = test.node[col][row];
    }

    //Sets The EndNode
    private void SetEndNode(int col, int row) {
        test.node[col][row].IsEnd();
        test.endNode = test.node[col][row];
    }
}