package models;

import javafx.scene.paint.Color;

public class Circle {
    private double x;
    private double y;
    private double radius;
    private Color color = Color.WHITE;
    private Color stroke = Color.BLACK;
    //variable to draw the text inside

    public Circle(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Circle(double x, double y, double radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public double getX() { return x; }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getStroke() { return stroke; }
    public void setStroke(Color c) { stroke = c; }


}
