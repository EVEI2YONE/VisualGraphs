package models;


import javafx.scene.paint.Color;

public abstract class Shape {
    protected int
        x, y,
        width, height;
    protected Color
            currFill, prevFill,
            currStroke, prevStroke;
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

    public void setWidth(double width) {
        this.width = (int)width;
    }
    public void setHeight(double height) {
        this.height = (int)height;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public void setCurrFill(Color currFill) {
        this.currFill = currFill;
    }
    public void setPrevFill(Color prevFill) {
        this.prevFill = prevFill;
    }
    public Color getCurrFill() {
        return currFill;
    }
    public Color getPrevFill() {
        return prevFill;
    }

    public void setCurrStroke(Color currStroke) {
        this.currStroke = currStroke;
    }
    public void setPrevStroke(Color prevStroke) {
        this.prevStroke = prevStroke;
    }
    public Color getCurrStroke() {
        return currStroke;
    }
    public Color getPrevStroke() {
        return prevStroke;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    public Object getValue() {
        return value;
    }

    abstract public double distanceFromBounds(Shape other);
}
