package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.graph.MyMath;

public class Circle extends Shape {

    public Circle(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Circle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currFill = Color.WHITE;
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

    public double pointDistanceFromBounds(int x, int y) {
        //x and y parameter is from mouse click
        //assume that circle is a perfect circle
        x -= this.x;
        y -= this.y;
        double distance = Math.sqrt(x*x + y*y);
        //check if distance is within bounds vs away from bounds
        distance *= (x < 0 || y < 0) ? -1: 1;
        //negative values get higher priority
        return distance;
    }

    @Override
    public void display(GraphicsContext graphicsContext) {

    }
}
