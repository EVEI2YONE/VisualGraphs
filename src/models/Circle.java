package models;

import javafx.scene.paint.Color;

public class Circle<E> {
    private double x;
    private double y;
    private double radius;
    private Color color;
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

    public double getX() { return (double) Math.round(x); }

    public void getX(double x) {
        this.x = x;
    }
    public void setX(double x) {
        this.x = (double) x;
    }

    public double getY() {
        return (double) Math.round(y);
    }

    public void getY(double y) {
        this.y = y;
    }
    public void setY(double y) {
        this.y = (double) y;
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


}
