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

import java.util.List;

public class CanvasController {
    private GraphController gc;
    private GraphicsContext g;
    public int getWidth() { return (int)canvas.getWidth(); }
    public int getHeight() { return (int)canvas.getHeight(); }

    @FXML Canvas canvas;

    public void paintComp() {
        if(canvas == null || gc == null)
            return;
        if(gc.getVertices() == null || gc.getEdges() == null)
            return;
        clearCanvas(g);
        //paint edges
        for (Edge e : gc.getEdges()) {
            e.getValue().displayShape(g);
        }
        //paint vertices
        for (Vertex v : gc.getVertices()) {
            //draw vertex objects (circles)
            v.getValue().displayShape(g);
            v.getValue().displayText(g);
        }
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
    private boolean addEdge;
    private Edge newEdge;
    public void onMouseDragged(MouseEvent mouseEvent) {
        if(currentItem == null) return;
        int
            x = (int)mouseEvent.getX(),
            y = (int)mouseEvent.getY();
        if(keyPressed == KeyCode.SHIFT) {
            System.out.println("shift section");
            if(currentItem == null) return;
            addEdge = true;
            Shape shape = currentItem.getItem();
            //create new line to draw
            if(newEdge == null && shape.getClass() == Circle.class) {
                Vertex from = gc.getGraph().getVertex(shape);
                newEdge = new Edge(from, null, currentItem.getItem().toString());
                Line line = new Line(from.getValue().getX(), from.getValue().getY(), x, y);
                newEdge.setValue(line);
                gc.getGraph().addEdge(newEdge);
            }
            //update new line
            if(newEdge != null) {
                Line line = (Line)newEdge.getValue();
                line.setWidth(x);
                line.setHeight(y);
            }
        }
        else {
            System.out.println("regular dragging");
            currentItem.getItem().setX(x);
            currentItem.getItem().setY(y);
        }
        gc.updateEdges();
        repaint();
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
        if(addEdge) {
            List<Item> temp = gc.findNodes(x, y);
            boolean flag = true;
            for(Item item : temp) {
                if(item.getItem().getClass() == Circle.class) {
                    if(item.getItem().getValue() != newEdge.getFrom()) {
                        Vertex v1 = newEdge.getFrom();
                        Vertex v2 = gc.getGraph().getVertex(item.getItem().getValue());
                        String label = v1.getLabel() + " -> " + v2.getLabel();
                        gc.getGraph().addVertices(v1.getLabel(), v2.getLabel());
                        if(gc.getGraph().getEdge(label) == null) {
                            Line line1 = new Line(0, 0, 0, 0);
                            gc.getGraph().getEdge(label).setValue(line1);
                            Line line2 = new Line(0, 0, 0, 0);
                            gc.getGraph().getEdgeCouple(label).setValue(line1);
                        }
                        flag = false;
                        break;
                    }
                }
            }
            if(flag)
                gc.getGraph().removeEdge(newEdge);
            newEdge= null;
        }
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

    public void init() {
         g = canvas.getGraphicsContext2D();
    }
}
