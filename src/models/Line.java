package models;

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
}
