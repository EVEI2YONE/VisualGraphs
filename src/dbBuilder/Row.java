package dbBuilder;

import javafx.scene.canvas.GraphicsContext;
import models.Shape;
import models.Vertex;

public class Row extends Vertex implements DisplayGraph {
    @Override
    public void display(GraphicsContext graphicsContext) {

    }

    public enum Type { INT, VARCHAR }
    private StringBuilder name;
    private Type type;
    private RowConnection connection;
    private Row next;

    public Row(String label) {
        super(label);
    }

    public Row(String label, Shape val) {
        super(label, val);
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
