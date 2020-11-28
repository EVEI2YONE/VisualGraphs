package models.shapes;

public interface ShapeCalculationInterface {
    public double distanceFromBounds(Shape other);
    public double pointDistanceFromBounds(int x, int y);
    public boolean overlaps(Shape other);
}
