package dbBuilder;

import java.util.ArrayList;
import java.util.List;

public class Table extends Shape {
    private List<Row> rows = new ArrayList<>();
    private boolean primaryKey;
    public Table() {

    }

    @Override
    public double distanceFromBounds(Shape other) {
        return 0;
    }

    @Override
    public double pointDistanceFromBounds(int x, int y) {
        return 0;
    }
}
