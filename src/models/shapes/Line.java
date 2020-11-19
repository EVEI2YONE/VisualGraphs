package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.graph.MyMath;

public class Line extends Shape {
    public Line(int x1, int y1, int x2, int y2) {
        this.x = x1;
        this.y = y1;
        this.width = x2;
        this.height = y2;
    }

    public double distanceFromBounds(Shape other) {
        double
            xPoints[] = { x, other.getX(), width },
            yPoints[] = { y, other.getY(), height },
            distance = Math.abs(MyMath.distancePointFromLine(xPoints, yPoints));
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
}
