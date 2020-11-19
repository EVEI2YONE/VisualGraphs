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
    public int getWidth() { return (int)canvas.getWidth(); }
    public int getHeight() { return (int)canvas.getHeight(); }

    @FXML Canvas canvas;

    public void paintComp() {
        if(canvas == null || gc == null)
            return;
        if(gc.getVertices() == null || gc.getEdges() == null)
            return;
        GraphicsContext g = canvas.getGraphicsContext2D();
        clearCanvas(g);
        //paint edges
        for (Edge e : gc.getEdges()) {
            e.getValue().displayShape(g);
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
    }
    public void clearCanvas(GraphicsContext g) {
        g.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
    }

    public void setGraphController(GraphController gc) {
        if(gc == null)
            return;
        this.gc = gc;
        paintComp();
    }
    public void repaint() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                paintComp();
            }
        });
        thread.start();
    }

    public void setDimension(int width, int height) {
        canvas.setWidth(width);
        canvas.setHeight(height);
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
    public void onKeyPressed(KeyCode event) {
        keyPressed = event;
        if(event == KeyCode.SHIFT) {
            Vertex v = null; //pin.gc.getGraph().getVertex(pin.selectedNode);
            if(v == null) return;
            line.setFrom(v);
            line.setXStart(prevX);
            line.setYStart(prevY);
        }
        repaint();
        //used with item selection and mouse drag
    }
    public void onKeyReleased(KeyCode event) {
        keyPressed = null;
        if(event.getCode() == KeyCode.DELETE.getCode()) {
            if(currentItems != null) {
                Shape shape = currentItems[0].getItem();
                Graph g = gc.getGraph();
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
