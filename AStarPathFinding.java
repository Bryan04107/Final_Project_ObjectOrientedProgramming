public class AStarPathFinding extends DijkstraPathFinding {

    public AStarPathFinding(NodeType[][] node, NodeType StartNode, NodeType EndNode, int maxSize, Screen test) {
        super(node, StartNode, EndNode, maxSize, test);
    }

    //Pathfinding Using A* Algorithm
    @Override
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

            AStarPathCheck();

            currentNode = availableList.get(bestNodeIndex);

            if (currentNode == endNode) {
                endReached = true;
                Traceback();
            }
            step++;
        }
    }

    // Checking Node Values For A* Algorithm
    private void AStarPathCheck() {
        int BestNodeFCost = 999;
        for (int i = 0; i < availableList.size(); i++) {
            if (availableList.get(i).fCost < BestNodeFCost) {
                bestNodeIndex = i;
                BestNodeFCost = availableList.get(i).fCost;
            } else if (availableList.get(i).fCost == BestNodeFCost) {
                if (availableList.get(i).gCost < availableList.get(bestNodeIndex).gCost) {
                    bestNodeIndex = i;
                }
            }
        }
    }
}