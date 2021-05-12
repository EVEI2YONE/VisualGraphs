package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.graph.MyMath;

public class Circle extends Shape {

    String primaryFill = "primaryFill";
    String primaryStroke = "primaryStroke";
    String strokeWeight = "strokeWeight";
    public Circle(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Circle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setFill(primaryFill, Color.WHITE);
        setStrokeColor(primaryStroke, Color.BLACK);
        setStrokeWeight(strokeWeight, 1.0);
    }
    public Circle(int x, int y, int radius, Color fill, Color stroke) {
        this.x = x;
        this.y = y;
        this.width = radius;
        this.height = radius;
        setFill(primaryFill, fill);
        setStrokeColor(primaryStroke, stroke);
        setStrokeWeight(strokeWeight, 1.0);
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
    public boolean overlaps(Shape other) {
        if(other.getClass() == Circle.class) {
            double distLimit = (width + other.getWidth()) / 2.0;
            double dist = MyMath.calculateDistance(x, y, other.getX(), other.getY());
            if(dist < distLimit)
                return true;
        }
        return false;
    }

    @Override
    public void displayShape(GraphicsContext g) {
        double
            xOff = x-(width/2),
            yOff = y-(height/2);

        g.setFill(getFill(primaryFill));
        g.fillOval(xOff, yOff, width, height);
        g.setStroke(getStrokeColor(primaryStroke));
        g.setLineWidth(getStrokeWeight(strokeWeight));
        g.strokeOval(xOff, yOff, width, height);
        g.setLineWidth(1.0);
    }

    @Override
    public void displayText(GraphicsContext g) {
        if(value == null) return;
        double
                wOffset = width/8,
                hOffset = height/8,
                xOff = x - wOffset,
                yOff = y + hOffset;
        Font font = new Font("TimesRoman", height/2);
        g.setFont(font);
        g.setFill(getStrokeColor("primaryStroke"));
        g.fillText(value.toString(), xOff, yOff);
    }

    @Override
    public void displayData(GraphicsContext graphicsContext) {

    }
}
