package models;

import controllers.CanvasController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

public class MyMath {
    public static final double RadToDeg = (360/(2*Math.PI));
    public static final double DegToRad = (2*Math.PI)/360;

    public static boolean isBetween(double a, double b, double c, double epsilon) {
        if((a-epsilon <= b && b <= c+epsilon) || (c-epsilon <= b && b <= a+epsilon))
            return true;
        return false;
    }
    public static double distancePointFromLine(double[] xPoints, double[] yPoints) {
        double
            x1 = xPoints[0],
            x = xPoints[1],
            x2 = xPoints[2],
            y1 = yPoints[0],
            y = yPoints[1],
            y2 = yPoints[2],
            theta = Math.atan2((y2-y1), (x2-x1));//Math.atan2((y2-y1), (x2-x1)) * MyMath.RadToDeg;
        //normalize line slope and equation
        MyMath.rotatePlane(xPoints, yPoints, -theta);
        double d = Math.abs(yPoints[1] - yPoints[0]);
        return d;
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

    public static boolean onAxis(double x, double y, double axis) {
        double angle = Math.atan2(y, x);
        double e = 0.0001;
        return (Math.abs(angle-axis) < e) || (Math.abs(angle-(axis+180)) < e);
    }

    public static boolean overlappingLines(double[] xPoints, double[] yPoints) {
        double
            r1 = calculateDistance(xPoints[0], yPoints[0], xPoints[1], yPoints[1]),
            r2 = calculateDistance(xPoints[2], yPoints[2], xPoints[3], yPoints[3]),
            c1x = (xPoints[0]+xPoints[1]) / 2,
            c1y = (yPoints[0]+yPoints[1]) / 2,
            c2x = (xPoints[2]+xPoints[3]) / 2,
            c2y = (yPoints[2]+yPoints[3]) / 2;
        Circle
            c1 = new Circle(c1x, c1y, r1),
            c2 = new Circle(c2x, c2y, r2);
        return overlappingCircles(c1, c2);
    }
    public static boolean linesIntersect(double[] xPoints, double[] yPoints) {
        testCase++;
        int i = testCase;
        double
            x1 = xPoints[0], //line 1 start
            y1 = yPoints[0],
            x2 = xPoints[1], //line 1 end
            y2 = yPoints[1],
            x3 = xPoints[2], //line 2 start
            y3 = yPoints[2],
            x4 = xPoints[3], //line 2 end
            y4 = yPoints[3],
            line1x = x2-x1,
            line1y = y2-y1,
            line2x = x4-x3,
            line2y = y4-y3;
        /*
        Note: Parallel and Perpendicular
            1) parallel : either both slopes are 0 or Infinite (2 && checks)
                - may not overlap
                - issue still persists when solving for x0, y0
            2) perpendicular : one slope is 0 or Infinite (2 || checks)
                - still intersects
         */
        /*
        //parallel about y axis
        if(onAxis(line1x, line1y, 90) && onAxis(line2x,line2y, 90)) {
            return overlappingLines(xPoints, yPoints);
        }//parallel about x axis
        else if(onAxis(line1x, line1y, 0) && onAxis(line2x, line2y, 0)) {
            return overlappingLines(xPoints, yPoints);
        }//perpendicular
        else if(onAxis(line1x, line1y, 90) || onAxis(line2x, line2y, 90)) {
            rotatePlane(xPoints, yPoints, 7);
            return linesIntersect(xPoints, yPoints);
        }
         */

        double
            m1 = (y2-y1) / (x2-x1),
            m3 = (y4-y3) / (x4-x3),
            b1 = y1 - (m1 * x1),
            b3 = y3 - (m3 * x3),
            x0 = (b1-b3) / (m3-m1), //parallel issue here when m3==m1
            y0 = m1*x0 + b1;
        //rotate lines by 1 degree and recalculate
        double
            uLine[] = getUnitVector(x1, y1, x2, y2),
            uIntersect[] = getUnitVector(x0, y0, x2, y2);
        if(Math.abs(uLine[0] - uIntersect[0]) < 0.000001 && Math.abs(uLine[1] - uIntersect[1]) < 0.000001)
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

    public static void rotatePlane(double[] xPoints, double[] yPoints, double angle) {
        double
            temp[],
            coords[][] = new double[2][1],
            pivotX=0, pivotY=0;

        int i;
        for(i = 0; i < xPoints.length; i++) {
            pivotX += xPoints[i];
            pivotY += yPoints[i];
        }
        pivotX /= i;
        pivotY /= i;

        for(i = 0; i < xPoints.length; i++) {
            temp = rotatePointAbout(pivotX, pivotY, xPoints[i], yPoints[i], angle);
            xPoints[i] = temp[0];
            yPoints[i] = temp[1];
        }
    }
    public static void rotatePlaneAbout(double pivotX, double pivotY, double[] xPoints, double[] yPoints, double angle) {
        double temp[];
        for(int i = 0; i < xPoints.length; i++) {
            temp = rotateLineAbout(pivotX, pivotY, xPoints[i], yPoints[i], angle);
            xPoints[i] = temp[0];
            yPoints[i] = temp[1];
        }
    }
    //TODO: FIX METHOD ROTATE LINE ABOUT AN ORIGIN
    public static double[] rotatePointAbout(double x1, double y1, double x2, double y2, double alpha) {
        double
            coords[] = new double[2],
            x = x2-x1,
            y = y2-y1,
            degrees = Math.atan2(y,x),
            distance = Math.sqrt(x*x + y*y),
            theta = degrees;
        theta += alpha;
        x = distance * Math.cos(theta);
        y = distance * Math.sin(theta);
        coords[0] = (x1 + x);
        coords[1] = (y1 + y);
        return coords;
    }
    public static double[] rotateLineAbout(double x1, double y1, double x2, double y2, double alpha) {
        double
            coords[] = new double[2],
            x = x2-x1,
            y = y2-y1,
            degrees = Math.atan2(y,x),
            distance = Math.sqrt(x*x + y*y),
            theta = degrees;
            alpha = DegToRad*(alpha);
            theta += alpha;
            x = distance * Math.cos(theta);
            y = distance * Math.sin(theta);
            coords[0] = (x1 + x);
            coords[1] = (y1 + y);
            return coords;
    }
}
