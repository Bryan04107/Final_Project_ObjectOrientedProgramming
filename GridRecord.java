import java.io.FileWriter;
import java.io.IOException;

public class GridRecord {
    private final Screen test;

    public GridRecord(Screen test) {
        this.test = test;
    }

    // Saves The Current Grid Into A Text File
    public void Save() {
        try (FileWriter fw = new FileWriter("Grids.txt", true)) {
            fw.write("#\n");
            for (int row = 0; row < Screen.getMaxSize(); row++) {
                for (int col = 0; col < Screen.getMaxSize(); col++) {
                    NodeType currentNode = test.node[col][row];
                    if (currentNode.wall) {
                        fw.write('0');
                    } else if (currentNode == test.startNode) {
                        fw.write('S');
                    } else if (currentNode == test.endNode) {
                        fw.write('E');
                    } else {
                        fw.write('-');
                    }
                }
                fw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}