import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NodeType extends JButton implements ActionListener {
    protected int col, row, gCost, hCost, fCost;
    protected boolean wall, start, end, available, checked;
    private static NodeType currentNode;
    protected NodeType parent;

    // Empty Node
    public NodeType(int col, int row) {
        this.col = col;
        this.row = row;

        setBackground(Color.white);
        setForeground(Color.black);
        addActionListener(this);
    }

    // Converts Node Into Start Node
    public void IsStart(){
        setBackground(Color.cyan);
        setForeground(Color.black);
        setText("S");
        start = true;
    }

    // Converts Node Into End Node
    public void IsEnd(){
        setBackground(Color.red);
        setForeground(Color.black);
        setText("E");
        end = true;
    }

    // Converts Node Into Wall Node
    public void IsWall(){
        setBackground(Color.black);
        setForeground(Color.black);
        wall = true;
    }

    // Determines if Node Can be Checked by Pathfinding Algorithm
    public void IsAvailable(){
        available = true;
    }

    // Determines if Node is Already Checked by Pathfinding Algorithm
    public void IsChecked(){
        if(!start && !end){
            setBackground(Color.yellow);
            setForeground(Color.black);
        }
        checked = true;
    }

    // Colors The Node If Determined To Be The Path
    public void IsPath(){
        setBackground(Color.green);
        setForeground(Color.black);
    }

    // Allows Interaction With The Node To Convert From Empty to Wall and Likewise
    public void actionPerformed(ActionEvent e) {
        try {
            if (wall) {
                wall = false;
                setBackground(Color.white);
                setForeground(Color.black);
                currentNode.setText("");
                currentNode = null;
            } else if (start) {
                IsStart();
            } else if (end) {
                IsEnd();
            } else {
                IsWall();
                currentNode.setText("");
            }
        } catch (NullPointerException ignored) {}
    }
}