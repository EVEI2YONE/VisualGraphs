package models.shapes;

public interface ShapeManipulation {
    public void rotate(double angle, double pivotX, double pivotY);
    public void translate(double xDestination, double yDestination);
}
