package dbBuilder;

import javafx.scene.canvas.GraphicsContext;
import models.graph.DisplayGraphInterface;
import models.shapes.Shape;
import models.graph.Vertex;

public class Row extends Vertex implements DisplayGraphInterface {

    private StringBuilder name;
    private String type;
    private RowConnection connection;
    private Row next;

    public Row(String label) {
        super(label);
    }

    public Row(String label, Shape val) {
        super(label, val);
    }

    @Override
    public void display(GraphicsContext graphicsContext) {

    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int compareTo(Vertex o) {
        return super.compareTo(o);
    }
}
