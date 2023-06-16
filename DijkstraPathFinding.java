import java.util.ArrayList;

public class DijkstraPathFinding {
    protected final NodeType[][] node;
    protected final NodeType startNode, endNode;
    protected NodeType currentNode;
    protected ArrayList<NodeType> availableList = new ArrayList<>();
    protected final int maxSize;
    protected static int step, finalStep;
    protected int bestNodeIndex;
    protected long x;
    protected static boolean endReached = false;
    protected final Screen test;

    public DijkstraPathFinding(NodeType[][] node, NodeType StartNode, NodeType EndNode, int maxSize, Screen test) {
        this.node = node;
        this.startNode = StartNode;
        this.endNode = EndNode;
        this.maxSize = maxSize;
        currentNode = StartNode;
        this.test = test;
    }

    //Pathfinding Using Dijkstra Algorithm
    public void InstantPathFind() {
        ResetValues();

        x = System.currentTimeMillis();
        while (!endReached && (Screen.getStep() < maxSize * maxSize)) {

            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.IsChecked();
            availableList.remove(currentNode);

            if (row - 1 >= 0) {
                AvailableNode(node[col][row - 1]);
            }
            if (row + 1 < maxSize) {
                AvailableNode(node[col][row + 1]);
            }
            if (col - 1 >= 0) {
                AvailableNode(node[col - 1][row]);
            }
            if (col + 1 < maxSize) {
                AvailableNode(node[col + 1][row]);
            }

            if (availableList.size() == 0) {
                long y = System.currentTimeMillis();
                Screen.setStep(step);
                Screen.setTimeTaken((int) (y - x));
                break;
            }

            bestNodeIndex = 0;

            DijkstraPathCheck();

            currentNode = availableList.get(bestNodeIndex);

            if (currentNode == endNode) {
                endReached = true;
                Traceback();
            }
            step++;
        }
    }

    // Adds Gathered Node To A List And Checks For Plausibility
    protected void AvailableNode(NodeType node) {
        if (!node.available && !node.checked && !node.wall) {
            node.IsAvailable();
            node.parent = currentNode;
            availableList.add(node);
        }
    }

    // Traces Back A Path to The Start From The End Node
    protected void Traceback() {
        NodeType Current = endNode;

        while (Current != startNode) {
            Current = Current.parent;

            if (Current != startNode) {
                Current.IsPath();
                finalStep++;
            }
        }

        // Generates Time Taken
        long y = System.currentTimeMillis();
        Screen.setTimeTaken((int) (y - x));
        Screen.setStep(step);
        Screen.setFinalStep(finalStep);
    }

    // Resets All Needed Variables
    protected void ResetValues() {
        availableList.clear();
        step = 0;
        finalStep = 0;
        Screen.setStep(0);
        Screen.setFinalStep(0);
        Screen.setTimeTaken(0);
        endReached = false;
    }

    // Checking Node Values For Dijkstra Algorithm
    private void DijkstraPathCheck() {
        for (int i = 0; i < availableList.size(); i++) {
            if (availableList.get(i).gCost < availableList.get(bestNodeIndex).gCost) {
                bestNodeIndex = i;
            }
        }
    }
}