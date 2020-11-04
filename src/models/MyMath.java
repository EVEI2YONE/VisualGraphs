package models;

import controllers.CanvasController;

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
    public static int testCase = 0;

    public static boolean linesIntersect(double...doubles) {
        testCase++;
        int i = testCase;
        double
            x1 = doubles[0], //line 1 start
            y1 = doubles[1],
            x2 = doubles[2], //line 1 end
            y2 = doubles[3],
            x3 = doubles[4], //line 2 start
            y3 = doubles[5],
            x4 = doubles[6], //line 2 end
            y4 = doubles[7];
        double
            m1 = (y2-y1) / (x2-x1),
            m3 = (y4-y3) / (x4-x3),
            b1 = y1 - (m1 * x1),
            b3 = y3 - (m3 * x3),
            x0 = (b1-b3) / (m3-m1),
            y0 = m1*x0 + b1;
        //rotate lines by 1 degree and recalculate
        if(x2-x1 == 0 || x4 == x3 || m3 == m1) {
            return linesIntersect(rotatePlane(doubles, 5, 0, 0));
        }
        double
            uLine[] = getUnitVector(x1, y1, x2, y2),
            uIntersect[] = getUnitVector(x0, y0, x2, y2);
        if(uLine[0] - uIntersect[0] < 0.00000001 && uLine[1] - uIntersect[1] < 0.00000001)
            return true;
        return false;
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

    public static double[] rotatePlane(double[] coordinates, double angle, int centerX, int centerY) {
        double newCoordinates[] = new double[coordinates.length], distance, temp[];
        for(int i = 0; i < coordinates.length; i += 2) {
            distance = calculateDistance(centerX, centerY, coordinates[i], coordinates[i+1]);
            temp = rotateLineAbout(centerX, centerY, coordinates[i], coordinates[i+1], angle);
            newCoordinates[i] = temp[0];
            newCoordinates[i+1] = temp[1];
        }
        return newCoordinates;
    }
    //TODO: FIX METHOD ROTATE LINE ABOUT AN ORIGIN
    public static double[] rotateLineAbout(double x1, double y1, double x2, double y2, double alpha) {
        double
            coords[] = new double[2],
            x = x2-x1,
            y = y2-y1,
            degrees = Math.atan2(y,x),
            distance = Math.sqrt(x*x + y*y),
            theta = degrees;
            alpha = (2*Math.PI)/360*(alpha);
            theta += alpha;
            x = distance * Math.cos(theta);
            y = distance * Math.sin(theta);
            coords[0] = (x1 + x);
            coords[1] = (y1 + y);
            return coords;
    }
}
