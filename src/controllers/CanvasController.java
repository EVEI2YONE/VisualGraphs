package controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Item;
import models.graph.Edge;
import models.graph.Graph;
import models.graph.MyMath;
import models.graph.Vertex;
import models.shapes.Arrow;
import models.shapes.Circle;
import models.shapes.Line;
import models.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

public class CanvasController {
    private GraphController gc;
    private GraphicsContext g;
    public int getWidth() { return (int)canvas.getWidth(); }
    public int getHeight() { return (int)canvas.getHeight(); }

    @FXML Canvas canvas;

    private List<Shape> shapes = new ArrayList<>();
    public void collectGraphShapes() {
        shapes.clear();
        for (Edge e : gc.getEdges())
            if(e.getValue() != null) {
                //update whether to draw arrows on edges
                if(e.getValue().getClass() == Arrow.class) {
                    boolean drawArrow = false;
                    if(gc.getGraph().isDirected()) {
                        drawArrow = true;
                    }
                    ((Arrow)e.getValue()).setDrawArrow(drawArrow);
                }
                shapes.add(e.getValue());
            }
        //paint vertices
        for (Vertex v : gc.getVertices()) {
            if(v.getValue() != null)
                shapes.add(v.getValue());
        }
    }

    public void paintComp() {
        if(canvas == null || gc == null)
            return;
        if(gc.getVertices() == null || gc.getEdges() == null)
            return;
        clearCanvas(g);
        //paint edges
        for (Shape shape : shapes) {
            shape.displayShape(g);
            shape.displayText(g);
        }
        Shape arrow = newEdge == null ? null : newEdge.getValue();
        if(arrow != null)
            arrow.displayShape(g);
    }

    public void clearCanvas(GraphicsContext g) {
        g.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
    }

    public void setGraphController(GraphController gc) {
        if(gc == null)
            return;
        this.gc = gc;
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
    public void addEdge() {
        List<Item> tempList = gc.findNodes(x, y);
        if(tempList.size() > 0) {
            Shape shape = tempList.get(0).getItem();
            if(shape.getClass() == Circle.class) {
                Vertex v2 = gc.getGraph().getVertex(shape);
                String
                        a = newEdge.getFrom().getLabel(),
                        b = v2.getLabel(),
                        label = a + " -> " + b;
                Edge edge = gc.getGraph().getEdge(label);
                if(edge == null) { //edge doesn't exist, so insert
                    edge = gc.getGraph().addVertices(a, b);
                    edge.setValue(newEdge.getValue()); //update shape
                    edge = gc.getGraph().getEdgeCouple(label);
                    edge.setValue(new Line(0, 0, 0, 0));
                } else if(!edge.isDirected()) { //edge exists, but isn't an arrow
                    edge.setValue(newEdge.getValue());
                    edge.setDirected(true);
                }
                gc.updateEdges();
            }
        }
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
            addEdge = true;
            Shape shape = null;
            //new edge, circle source item
            if(currentItem != null)
                shape = currentItem.getItem();
            if(newEdge == null && currentItem != null && shape.getClass() == Circle.class) {
                Vertex v1 = gc.getGraph().getVertex(shape);
                newEdge = new Edge(v1, null, "temp");
                Arrow arrow = new Arrow(shape.getX(), shape.getY(), x, y);
                newEdge.setValue(arrow);
            }
            if(newEdge != null) {
                gc.updateNewEdge(newEdge, x, y);
            }
        }
        else {
            currentItem.getItem().setX(x);
            currentItem.getItem().setY(y);
        }
        gc.updateEdges();
        paintComp();
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
        paintComp();
    }

    public void onMouseReleased(MouseEvent mouseEvent) {
        x = (int) mouseEvent.getX();
        y = (int) mouseEvent.getY();
        mousePressed = false;
        mouseDragged = false;
        if(addEdge) {
            addEdge();
            gc.getGraph().sort();
            gc.getGraph().debug();
            collectGraphShapes();
            newEdge = null;
            addEdge = false;
        }
        currentItem = null;
        paintComp();
    }

    //TODO: BE RIGHT BACK
    private KeyCode keyPressed;;
    private static Edge line = new Edge(null, null, "temp");
    public void onKeyPressed(KeyCode event) {
        keyPressed = event;
        if(keyPressed == KeyCode.SHIFT) {
            Vertex v = null; //pin.gc.getGraph().getVertex(pin.selectedNode);
            if(v == null) return;
            line.setFrom(v);
            line.setXStart(prevX);
            line.setYStart(prevY);
        }
        paintComp();
        //used with item selection and mouse drag
    }
    public void onKeyReleased(KeyCode event) {
        keyPressed = event;
        if(keyPressed == KeyCode.DELETE) {
            if(currentItems != null && currentItems.length > 0) {
                Shape shape = currentItems[0].getItem();
                Graph g = gc.getGraph();
                if (shape.getClass() == Circle.class) {
                    g.removeVertex(shape);
                } else if (shape.getClass() == Line.class || shape.getClass() == Arrow.class) {
                    Edge edge = g.getEdge(shape);
                    Shape couple = g.getEdgeCouple(edge.getLabel()).getValue();
                    g.removeEdge(shape);
                }
                collectGraphShapes();
                currentItem = null;
                currentItems = null;
            }
        }
        else if(keyPressed == KeyCode.SHIFT) {
            newEdge = null;
        }
        keyPressed = null;
        //used with mouse drag
        paintComp();
    }

    public void init() {
         g = canvas.getGraphicsContext2D();
    }
}
