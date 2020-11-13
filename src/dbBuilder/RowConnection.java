package dbBuilder;

import models.Edge;
import models.Line;
import models.Shape;
import models.Vertex;

public class RowConnection extends Edge {
    public RowConnection(Vertex a, Vertex b, String label) {
        super(a, b, label);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int compareTo(Edge o) {
        return super.compareTo(o);
    }

}
