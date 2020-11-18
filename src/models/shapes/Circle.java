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
        this.primaryFill = Color.WHITE;
        this.primaryStroke = Color.BLACK;
    }
    public Circle(int x, int y, int radius, Color fill, Color stroke) {
        this.x = x;
        this.y = y;
        this.width = radius;
        this.height = radius;
        this.primaryFill = fill;
        this.primaryStroke = stroke;
    }
    public double distanceFromBounds(Shape other) {
        double distance = Double.MAX_VALUE;
        if(other.getClass() == Circle.class)
            distance = Math.abs(MyMath.calculateDistance(other.getX(), other.getY(), x, y));
        return distance;
    }

    public double pointDistanceFromBounds(int x, int y) {
        double distance = MyMath.calculateDistance(x, y, this.x, this.y);
        if(distance < MyMath.calculateDistance(this.x, this.y, this.x+width, this.y+height))
            distance = 0;
        return distance;
    }

    @Override
    public void display(GraphicsContext graphicsContext) {

    }
}
