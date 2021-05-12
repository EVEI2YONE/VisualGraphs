package models.shapes;


import javafx.scene.paint.Color;
import java.util.Dictionary;
import java.util.Hashtable;

public abstract class Shape implements DisplayGraphInterface, ShapeCalculationInterface {
    protected int
        x, y,
        width, height;
    protected Dictionary<String, Object>
        dictColorFill = new Hashtable<>(),
        dictColorStroke = new Hashtable<>(),
        dictNumber = new Hashtable<>();

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

    public void setFill(String key, Color currFill) {
        dictColorFill.put(key, currFill);
    }
    public Color getFill(String key) {
        return (Color) dictColorFill.get(key);
    }

    public void setStrokeColor(String key, Color currStroke) {
        dictColorStroke.put(key, currStroke);
    }
    public Color getStrokeColor(String key) { return (Color) dictColorStroke.get(key); }

    public Double getStrokeWeight(String key) {
        return (Double) dictNumber.get(key);
    }
    public void setStrokeWeight(String key, Double strokeWeight) {
        dictNumber.put(key, strokeWeight);
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
