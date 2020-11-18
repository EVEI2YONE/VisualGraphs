package controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.*;
import models.graph.Edge;
import models.graph.Graph;
import models.graph.MyMath;
import models.graph.Vertex;
import models.shapes.Circle;
import models.shapes.Line;
import models.shapes.Shape;

public class CanvasController {
    private GraphController gc;
    private static CanvasController pin;
    public static int getWidth() { return (int)pin.canvas.getWidth(); }
    public static int getHeight() { return (int)pin.canvas.getHeight(); }

    @FXML Canvas canvas;

    public CanvasController() {
        pin = this;
        if(canvas == null) {
            canvas = new Canvas(500, 500);
        }
    }

    public void paintComp() {
        if(canvas == null || gc == null)
            return;
        if(gc.getVertices() == null || gc.getEdges() == null)
            return;
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
            //draw vertex objects (circles)
            v.getValue().displayShape(g);
            v.getValue().displayText(g);
        }
    }

    public static void rotateGraphAveragePivot() {
        if(pin.gc == null)
            return;
        pin.gc.calculateAveragePivot();
        for(int i = 0; i< 360; i++) {
            pin.gc.rotateGraphAveragePivot(1);
            //draw updated graph
            try {
                Thread.sleep(20);
            } catch (Exception ex) { }
            pin.gc.updateEdges();
            CanvasController.repaint();
        }
    }

    public void drawEdge(GraphicsContext g, Edge e) {
        //g.setFill(e.getColor());
        g.setStroke(e.getValue().getPrimaryFill());
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
        g.setFill(e.getValue().getPrimaryFill());
        g.fillPolygon(start, end, 3);

        //g.strokeLine(start[0], end[0], pivotX, pivotY);
        //g.strokeLine(start[1], end[1], pivotX, pivotY);
    }
    public void clearCanvas(GraphicsContext g) {
        g.clearRect(0,0,canvas.getWidth()*2, canvas.getHeight()*2);
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
    private Item
        prevItems[] = null,
        currentItems[] = null,
        filter[] = null,
        currentItem;
    private int
        x, y,
        prevX,
        prevY;

    private boolean mouseDragged;
    public void onMouseDragged(MouseEvent mouseEvent) {
        if(currentItem == null) return;
        int
            x = (int)mouseEvent.getX(),
            y = (int)mouseEvent.getY();
        currentItem.getItem().setX(x);
        currentItem.getItem().setY(y);
        gc.updateEdges();
        repaint();
        if(keyPressed == KeyCode.SHIFT && mouseDragged) {

        }
    }
    private boolean mousePressed;
    public void onMousePressed(MouseEvent mouseEvent) {
        if(gc == null) return;
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        prevItems = currentItems;
        currentItems = gc.findItems(x, y);
        //items are sorted based on distance
        if(currentItems.length == 0) return;
        currentItem = currentItems[0];

        mousePressed = true; mouseDragged = true;
        prevX = x; prevY = y;
        repaint();
    }

    public void onMouseReleased(MouseEvent mouseEvent) {
        System.out.println("mouse released");
        x = (int) mouseEvent.getX();
        y = (int) mouseEvent.getY();
        mousePressed = false;
        mouseDragged = false;
        currentItem = null;
    }

    //TODO: BE RIGHT BACK
    private KeyCode keyPressed;;
    private static Edge line = new Edge(null, null, "temp");
    public static void onKeyPressed(KeyCode event) {
        pin.keyPressed = event;
        if(event == KeyCode.SHIFT) {
            Vertex v = null; //pin.gc.getGraph().getVertex(pin.selectedNode);
            if(v == null) return;
            line.setFrom(v);
            line.setXStart(pin.prevX);
            line.setYStart(pin.prevY);
        }
        repaint();
        //used with item selection and mouse drag
    }
    public static void onKeyReleased(KeyCode event) {
        pin.keyPressed = null;
        if(event.getCode() == KeyCode.DELETE.getCode()) {
            if(pin.currentItems != null) {
                Shape shape = pin.currentItems[0].getItem();
                Graph g = pin.gc.getGraph();
                if(shape.getClass() == Circle.class) {
                    g.removeVertex(shape);
                }
                else if(shape.getClass() == Line.class) {
                    g.removeEdge(shape);
                }
            }
            repaint();
        }
        //used with mouse drag
    }
}
