public class NodeCost {
    private final NodeType[][] node;
    private final NodeType StartNode, EndNode;
    private final int maxSize;
    private final Screen test;

    public NodeCost(NodeType[][] node, NodeType StartNode, NodeType EndNode, int maxSize, Screen test) {
        this.node = node;
        this.StartNode = StartNode;
        this.EndNode = EndNode;
        this.maxSize = maxSize;
        this.test = test;
    }

    // Sets The Cost For All The Nodes
    public void SetNodesCost() {
        int CurrentCol = 0;
        int CurrentRow = 0;

        while (CurrentCol < maxSize && CurrentRow < maxSize) {
            GetNodeCost(node[CurrentCol][CurrentRow]);
            CurrentCol++;
            if (CurrentCol == maxSize) {
                CurrentCol = 0;
                CurrentRow++;
            }
        }
    }

    //Generates Cost for The Nodes
    private void GetNodeCost(NodeType node) {
        int xDistance = Math.abs(node.col - StartNode.col);
        int yDistance = Math.abs(node.row - StartNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - EndNode.col);
        yDistance = Math.abs(node.row - EndNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;

        if (test.toggleCost && node != StartNode && node != EndNode) {
            node.setText("<html><span style='font-size: 7px;'>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
        }
    }
}