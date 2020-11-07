package models;

import javafx.scene.paint.Color;

public class Circle extends Shape {
//    private double x;
//    private double y;
//    private double radius;
//    private Color color = Color.WHITE;
//    private Color stroke = Color.BLACK;
//    //variable to draw the text inside

    public Circle(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Circle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currFill = Color.BLACK;
        this.currStroke = Color.BLACK;
    }
    public Circle(int x, int y, int radius, Color fill, Color stroke) {
        this.x = x;
        this.y = y;
        this.width = radius;
        this.height = radius;
        this.currFill = fill;
        this.currStroke = stroke;
    }
    public double distanceFromBounds(Shape other) {
        double distance = Double.MAX_VALUE;
        if(other.getClass() == Circle.class)
            distance = Math.abs(MyMath.calculateDistance(other.getX(), other.getY(), x, y));
        return distance;
    }
}
