package models;

public class MyMath {
    public static double distancePointFromLine(double x0, double y0, double x1, double y1, double x2, double y2) {
        double distance = 0.0;

        return distance;
    }

    public static double[] averageVector(double x, double y, double[] doubleXs, double[] doubleYs) {
        double[] vector = new double[2];

        return vector;
    }

    public static boolean overlappingCircles(Circle c1, Circle c2){
        boolean overlaps = false;
        double
            x1 = c1.getX(),
            y1 = c1.getY(),
            x2 = c2.getX(),
            y2 = c2.getY();
        if(calculateDistance(x1, y1, x2, y2) < c1.getRadius()+c2.getRadius())
            overlaps = true;
        return overlaps;
    }

    public static double calculateDistance(double x0, double y0, double x1, double y1) {
        double
            x2 = Math.pow(x1-x0, 2),
            y2 = Math.pow(y1-y0, 2),
            distance = Math.sqrt((x2 + y2));

        return distance;
    }

    public static double[] getUnitVector(double x1, double y1, double x2, double y2) {
        double[] vector = new double[2];
        double distance = calculateDistance(x1, y1, x2, y2);
        vector[0] = (x2-x1)/distance;
        vector[1] = (y2-y1)/distance;
        return vector;
    }

    public static boolean linesIntersect(double...doubles) {
        boolean intersect = false;
        double
            x1 = doubles[0],
            y1 = doubles[1],
            x2 = doubles[2],
            y2 = doubles[3],
            x3 = doubles[4],
            y3 = doubles[5],
            x4 = doubles[6],
            y4 = doubles[7];

        return intersect;
    }
    public static boolean linesIntersectsCircle(Circle circle, int x1, int y1, int x2, int y2) {
        double
            x0 = circle.getX(),
            y0 = circle.getY(),
            radius = circle.getRadius(),
            numerator, denominator, distance;

        numerator = (y2-y1)*x0 - (x2-x1)*y0 + x2*y1 - y2*x1;
        numerator = Math.abs(numerator);
        denominator = Math.pow(y2-y1, 2) + Math.pow(x2-x1, 2);
        denominator = Math.sqrt(denominator);
        distance = Math.abs(numerator/denominator);
        return distance < radius;
    }

    public static double[] polarRotateUnitVector(double x1, double y1, double x2, double y2, double angle, double r) {
        double[] rotatedVector = new double[2];
        double a = Math.abs(angle);
        double
            cosA = Math.cos(a),
            sinA = Math.sin(90.0-a),
            unitVector[] = getUnitVector(x1, y1, x2, y2);
        double
            ux = unitVector[0],
            uy = unitVector[1],
            h = r * sinA,
            hy = h * ux,
            hx = h * uy,
            r_d = r * cosA,
            r_dx = r_d * ux,
            r_dy = r_d * uy;
        double x3, y3;
        if(angle > 0) {
            x3 = x2 - r_dx - hx;
            y3 = y2 - r_dy + hy;
        }
        else {
            x3 = x2 - r_dx + hx;
            y3 = y2 - r_dy - hy;
        }

        rotatedVector[0] = x3;
        rotatedVector[1] = y3;
        return rotatedVector;
    }

    public class ArrowHead {


    }

}
