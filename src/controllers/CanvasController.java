package controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.*;

import java.util.List;

import static models.MyMath.rotateLineAbout;

public class CanvasController {
    private GraphController gc;
    private static CanvasController pin;
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
            drawShape(g, v);
            drawString(g, v);
        }
    }

    public static void rotateGraph(double width, double height) {
        double
            pivotX = width/2,
            pivotY = height/2; //add an offset for future graph translations
        for(int i = 0; i < 360; i++) {
            pin.gc.rotateGraph(pivotX, pivotY, 1);
            //draw updated graph
            try {
                Thread.sleep(20);
            } catch (Exception ex) {
            }
            pin.gc.updateEdges();
            CanvasController.repaint();
        }
    }
    public static void rotateGraphAveragePivot() {
        if(pin.gc == null)
            return;
        for(int i = 0; i< 360; i++) {
            pin.gc.rotateGraphAveragePivot(1);
            //draw updated graph
            try {
                Thread.sleep(20);
            } catch (Exception ex) {
            }
            pin.gc.updateEdges();
            CanvasController.repaint();
        }
    }

    public void drawString(GraphicsContext g, Vertex v) {
        Circle c = (Circle) v.getValue();
        double radius = c.getRadius();
        double x = c.getX()-radius/4;
        double y = c.getY()+radius/4;
        Font font = new Font("TimesRoman", radius);
        g.setFont(font);
        g.setFill(c.getStroke());
        g.fillText(v.getLabel(), x, y);
    }
    public void drawShape(GraphicsContext g, Vertex v) {
        Circle c = (Circle)v.getValue();
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
        g.clearRect(0,0,canvas.getWidth()*2, canvas.getHeight()*2);
    }
    public static void resizeCanvas() {
        pin.canvas.setWidth(pin.gc.getWidth()*2);
        pin.canvas.setHeight(pin.gc.getHeight()*2);
    }
    public static GraphicsContext getCanvasGraphics() { return pin.canvas.getGraphicsContext2D(); }
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

    private Circle
        selectedNode = null,
        previousNode = null;
    private Edge
        selectedEdge = null,
        previousEdge = null;
    boolean mouseDragged;
    public void onMouseDragged(MouseEvent mouseEvent) {
        if(selectedNode == null)
            return;
        mouseDragged = true;
        selectedNode.setX((int)mouseEvent.getX());
        selectedNode.setY((int)mouseEvent.getY());
        gc.updateEdges();
        paintComp();
        if(mouseDragged && keyPressed) {
            System.out.println("combinations of key and mouse drag");
        }
    }

    boolean mousePressed;
    public void onMousePressed(MouseEvent mouseEvent) {
        if(gc == null)
            return;
        mousePressed = true;
        mouseDragged = true;

        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        selectedNode = (Circle)gc.findNode(x, y);
        selectedEdge = gc.findEdge(x, y);
        if(selectedEdge != null) {
            selectedEdge.setColor(Color.GREEN);
            Edge temp = gc.getGraph().getEdgeCouple(selectedEdge.toString());
            temp.setColor(Color.GREEN);
        }
        if(selectedNode != null) {

        }
    }
    public void onMouseReleased(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        if(selectedEdge != null) {
            selectedEdge.setColor(Color.GREEN);
            gc.getGraph().getEdgeCouple(selectedEdge.toString()).setColor(Color.GREEN);
            repaint();
        }
//        System.out.println("mouse key released");
        mousePressed = false;
        mouseDragged = false;
    }

    //TODO: BE RIGHT BACK
    boolean keyPressed;
    public static void onKeyPressed(KeyCode event) {
        pin.keyPressed = true;
        if(event.getCode() == KeyCode.DELETE.getCode()) {
            if (pin.selectedNode != null) {
                pin.gc.debugAdjacency();
                System.out.printf("selected: %s was removed\n\n\n\n\n", pin.gc.getGraph().getVertex(pin.selectedNode));
                pin.gc.getGraph().removeVertex(pin.selectedNode);
                pin.gc.debugAdjacency();
                pin.selectedNode = null;
            }
            if (pin.selectedEdge != null) {
                System.out.printf("selected: %s was removed\n", pin.selectedEdge.toString());
                pin.gc.getGraph().removeEdge(pin.selectedEdge);
                pin.selectedEdge = null;
            }


        }
        repaint();
        //used with item selection and mouse drag
    }
    public static void onKeyReleased(KeyCode event) {
        pin.keyPressed = false;
        //used with mouse drag
    }
}
