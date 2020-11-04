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
        //paint vertices
        for (Vertex v : gc.getVertices()) {
            Circle c = (Circle) v.getValue();
            //draw vertex objects (circles)
            if (c != null) {
                drawCircle(g, c);
                drawString(g, v);
            }
        }
        //paint edges
        for (Edge e : gc.getEdges()) {
            drawEdge(g, e);
            //paint edge component (arrowhead)
            if(gc.getGraph().isDirected() && e.isDirected()) {
                drawArrow(g, e);
            }
        }
    }

    public void drawString(GraphicsContext g, Vertex v) {
        Circle c = (Circle) v.getValue();
        double radius = c.getRadius();
        Font font = new Font("TimesRoman", radius);
        g.setFont(font);
        g.setStroke(Color.BLACK);
        g.fillText(v.getLabel(), c.getX() - radius / 4, c.getY() + radius / 4);
    }
    public void drawCircle(GraphicsContext g, Circle c) {
        double radius = gc.getRadius();
        g.setStroke(c.getColor());
        g.strokeOval(c.getX() - radius, c.getY() - radius, radius * 2, radius * 2);
    }
    public void drawEdge(GraphicsContext g, Edge e) {
        g.setFill(e.getColor());
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
            angle = 30;
        unit[0] *= gc.getRadius(); unit[0] += pivotX;
        unit[1] *= gc.getRadius(); unit[1] += pivotY;
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
    public static void repaint() { pin.paintComp(); }

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
