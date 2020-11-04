package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import models.*;

import java.util.List;

import static models.MyMath.rotateLineAbout;

public class CanvasController {
    private GraphController gc;
    private static CanvasController pin;
    private Circle selected = null;
    @FXML Canvas canvas;

    public CanvasController() {
        pin = this;
        if(canvas == null)
            canvas = new Canvas(500, 500);
    }

    public void paintComp() {
        if(canvas == null || gc == null)
            return;
        if(gc.getVertices() == null || gc.getEdges() == null)
            return;
        resizeCanvas();
        GraphicsContext g = canvas.getGraphicsContext2D();
        clearCanvas(g);
        //paint edges
        for (Edge e : gc.getEdges()) {
            drawEdge(g, e);
            //paint edge component (arrowhead)
            if(gc.getGraph().isDirected() && e.isDirected()) {
                drawArrow(g, e);
            }
        }
        //paint vertices
        for (Vertex v : gc.getVertices()) {
            Circle c = (Circle) v.getValue();
            //draw vertex objects (circles)
            drawCircle(g, c);
            drawString(g, v);
        }
    }

    public static void rotateGraph() {
        double
            pivotX = pin.canvas.getWidth()/2,
            pivotY = pin.canvas.getHeight()/2;
        Circle circle;
        for(int i = 0; i < 360; i++) {
            for (Vertex v : pin.gc.getVertices()) {
                circle = (Circle) v.getValue();
                if (circle == null) continue;
                double
                    x2, y2, step[];
                    x2 = circle.getX();
                    y2 = circle.getY();
                    step = rotateLineAbout(pivotX, pivotY, x2, y2, 1);
                    circle.setX(step[0]);
                    circle.setY(step[1]);

            }
            //draw updated graph
            try {
                Thread.sleep(20);
            } catch (Exception ex) { }
            pin.gc.updateEdges();
            CanvasController.repaint();
        }
    }
    public static void rotateGraphAveragePivot() {
        if(pin.gc == null) return;
        List<Vertex> list = pin.gc.getVertices();
        //try and implement rotate plane
        double
                xPoints[] = new double[list.size()],
                yPoints[] = new double[list.size()];

        Circle circle;
        for(int i = 0; i < list.size(); i++) {
            circle = (Circle) list.get(i).getValue();
            if(circle == null) continue;
            xPoints[i] = circle.getX();
            yPoints[i] = circle.getY();
        }
        for(int i = 0; i < 360; i++) {
            MyMath.rotatePlane(xPoints, yPoints, 1);
            for(int j = 0; j < list.size(); j++) {
                circle = (Circle) list.get(j).getValue();
                //if(circle == null) continue;
                circle.setX(xPoints[j]);
                circle.setY(yPoints[j]);
            }
            //draw updated graph
            try {
                Thread.sleep(20);
            } catch (Exception ex) { }
            pin.gc.updateEdges();
            CanvasController.repaint();
        }
    }

    public void drawString(GraphicsContext g, Vertex v) {
        Circle c = (Circle) v.getValue();
        double radius = c.getRadius();
        Font font = new Font("TimesRoman", radius);
        g.setFont(font);
        g.setFill(c.getStroke());
        g.fillText(v.getLabel(), c.getX() - radius / 4, c.getY() + radius / 4);
    }
    public void drawCircle(GraphicsContext g, Circle c) {
        double
            diam = gc.getRadius(),
            x = c.getX()-diam,
            y = c.getY()-diam;
        diam *= 2;
        g.setFill(c.getColor());
        g.fillOval(x, y, diam, diam);
        g.setStroke(c.getStroke());
        g.strokeOval(x, y, diam, diam);
    }
    public void drawEdge(GraphicsContext g, Edge e) {
        //g.setFill(e.getColor());
        g.setStroke(e.getColor());
        double
            x1 = e.getXStart(),
            y1 = e.getYStart(),
            x2 = e.getXEnd(),
            y2 = e.getYEnd();
        g.strokeLine(x1, y1, x2, y2);
    }
    public void drawArrow(GraphicsContext g, Edge e) {
        double
            x1 = e.getXStart(),
            y1 = e.getYStart(),
            pivotX = e.getXEnd(),
            pivotY = e.getYEnd(),
            unit[] = MyMath.getUnitVector(pivotX, pivotY, x1, y1),
            angle = 20,
            r = 15;
        unit[0] *= r; unit[0] += pivotX;
        unit[1] *= r; unit[1] += pivotY;
        double[] upper = MyMath.rotateLineAbout(pivotX,pivotY,unit[0],unit[1],angle);
        double[] lower = MyMath.rotateLineAbout(pivotX,pivotY,unit[0],unit[1],-angle);
        double[] start = new double[3],
                end = new double[3];
        start[0] = upper[0];
        start[1] = lower[0];
        start[2] = pivotX;
        end[0] = upper[1];
        end[1] = lower[1];
        end[2] = pivotY;
        g.setFill(e.getColor());
        g.fillPolygon(start, end, 3);

        //g.strokeLine(start[0], end[0], pivotX, pivotY);
        //g.strokeLine(start[1], end[1], pivotX, pivotY);
    }
    public void clearCanvas(GraphicsContext g) {
        g.clearRect(0,0,canvas.getWidth(), gc.getHeight());
    }
    public static void resizeCanvas() {
        pin.canvas.setWidth(pin.gc.getWidth());
        pin.canvas.setHeight(pin.gc.getHeight());
    }

    public static void setGraphController(GraphController gc) {
        if(gc == null)
            return;
        pin.gc = gc;
        pin.paintComp();
    }
    public static void repaint() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pin.paintComp();
            }
        });
        thread.start();
    }

    public static void setDimension(int width, int height) {
        pin.canvas.setWidth(width);
        pin.canvas.setHeight(height);
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        if(selected == null)
            return;
        System.out.println("dragging over");
        selected.setX((int)mouseEvent.getX());
        selected.setY((int)mouseEvent.getY());
        gc.updateEdges();
        //repaint();
        paintComp();
    }
    public void onMousePressed(MouseEvent mouseEvent) {
        if(gc == null)
            return;
        System.out.println("drag entered");
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        selected = (Circle)gc.findNode(x, y);
    }

}
