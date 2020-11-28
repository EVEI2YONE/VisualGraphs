package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.graph.MyMath;

public class Arrow extends Line {

    public Arrow(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public double distanceFromBounds(Shape other) {
        return super.distanceFromBounds(other);
    }

    @Override
    public double pointDistanceFromBounds(int x, int y) {
        return super.pointDistanceFromBounds(x, y);
    }

    @Override
    public void displayShape(GraphicsContext g) {
        super.displayShape(g);
        double
                x1 = x,
                y1 = y,
                pivotX = width,
                pivotY = height,
                unit[] = MyMath.getUnitVector(pivotX, pivotY, x1, y1),
                angle = 20,
                r = 15;
        unit[0] *= r; //unit[0] += pivotX;
        unit[1] *= r; //unit[1] += pivotY;

//        g.setStroke(Color.GREEN);
//        g.strokeOval(pivotX-r, pivotY-r, r*2, r*2);
        double[] upper = MyMath.rotateLineAbout(0, 0,unit[0],unit[1], angle);
        double[] lower = MyMath.rotateLineAbout(0, 0,unit[0],unit[1],-angle);

//        g.strokeLine(pivotX, pivotY, pivotX+upper[0], pivotY+upper[1]);
//        g.strokeLine(pivotX, pivotY, pivotX+lower[0], pivotY+lower[1]);

        double[] start = new double[3],
                end = new double[3];
        start[0] = upper[0]+pivotX;
        start[1] = lower[0]+pivotX;
        start[2] = pivotX;
        end[0] = upper[1]+pivotY;
        end[1] = lower[1]+pivotY;
        end[2] = pivotY;
        g.setFill(primaryStroke);
        g.fillPolygon(start, end, 3);
        g.setStroke(Color.BLACK);
    }

    @Override
    public void displayText(GraphicsContext graphicsContext) {
        super.displayText(graphicsContext);
    }

    @Override
    public void displayData(GraphicsContext graphicsContext) {
        super.displayData(graphicsContext);
    }
}
