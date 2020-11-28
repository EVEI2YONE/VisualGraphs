package models.shapes;


import javafx.scene.paint.Color;

public abstract class Shape implements DisplayGraphInterface, ShapeCalculationInterface {
    protected int
        x, y,
        width, height;
    protected Color
            primaryFill, secondaryFill,
            primaryStroke, secondaryStroke;
    protected Object
            value;

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void setPrimaryFill(Color currFill) {
        primaryFill = currFill;
    }
    public void setSecondaryFill(Color prevFill) {
        secondaryFill = prevFill;
    }
    public Color getPrimaryFill() {
        return primaryFill;
    }
    public Color getSecondaryFill() {
        return secondaryFill;
    }

    public void setPrimaryStroke(Color currStroke) {
        primaryStroke = currStroke;
    }
    public void setSecondaryStroke(Color prevStroke) {
        secondaryStroke = prevStroke;
    }
    public Color getPrimaryStroke() {
        return primaryStroke;
    }
    public Color getSecondaryStroke() {
        return secondaryStroke;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    public Object getValue() {
        return value;
    }
    public Shape copy() {
        return null;
    }

    abstract public double distanceFromBounds(Shape other);
    abstract public double pointDistanceFromBounds(int x, int y);
    abstract public boolean overlaps(Shape other);
}
