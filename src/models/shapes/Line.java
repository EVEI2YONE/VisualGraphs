package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.graph.MyMath;

public class Line extends Shape implements ShapeManipulation {
    public Line(int x1, int y1, int x2, int y2) {
        this.x = x1;
        this.y = y1;
        this.width = x2;
        this.height = y2;
    }

    public double distanceFromBounds(Shape other) {
        double distance = 50;
        if(other.getClass() == Line.class || other.getClass() == Arrow.class) {
            double
                x1 = x,
                y1 = y,
                x2 = width,
                y2 = height,
                x3 = other.getX(),
                y3 = other.getY(),
                x4 = other.getWidth(),
                y4 = other.getHeight(),
                xPoints[] = { x1, x2, x3, x4 },
                yPoints[] = { y1, y2, y3, y4 };
            if(MyMath.linesIntersect(xPoints, yPoints)) {
                distance = -1;
            }
        }
        else {
            double
                xPoints[] = { x, other.getX(), width },
                yPoints[] = { y, other.getY(), height };
            distance = Math.abs(MyMath.distancePointFromLine(xPoints, yPoints));
        }
        return distance;
    }

    public double pointDistanceFromBounds(int x, int y) {
        double
                xPoints[] = { this.x, x, width },
                yPoints[] = { this.y, y, height },
                distance = Math.abs(MyMath.distancePointFromLine(xPoints, yPoints));
        return distance;
    }

    @Override
    public boolean overlaps(Shape other) {
        return false;
    }

    @Override
    public void displayShape(GraphicsContext g) {
        g.setStroke(primaryStroke);
        double
            x1 = x,
            y1 = y,
            x2 = width,
            y2 = height;
        g.strokeLine(x1, y1, x2, y2);
    }

    @Override
    public void displayText(GraphicsContext graphicsContext) {

    }

    @Override
    public void displayData(GraphicsContext graphicsContext) {

    }

    @Override
    public void rotate(double angle, double pivotX, double pivotY) {
        double
            x1 = x,
            y1 = y,
            x2 = width,
            y2 = height,
            xPoints[] = { x1, x2 },
            yPoints[] = { y1, y2 };
        MyMath.rotatePlaneAbout(pivotX, pivotY, xPoints, yPoints, angle);
        x = (int)xPoints[0];
        y = (int)yPoints[0];
    }

    @Override
    public Shape copy() {
        Shape other = new Line(x, y, width, height);
        other.setPrimaryFill(primaryFill);
        other.setSecondaryFill(secondaryFill);
        other.setPrimaryStroke(primaryStroke);
        other.setSecondaryStroke(secondaryStroke);
        other.setValue(value);
        return other;
    }

    @Override
    public void translate(double xDestination, double yDestination) {

    }
}
