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
import shapes.Circle;
import shapes.Line;
import shapes.Shape;

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
        Shape s = v.getValue();
        double
            width = s.getWidth(),
            height = s.getHeight(),
            wOffset = width/8,
            hOffset = height/8,
            x = s.getX() - wOffset,
            y = s.getY() + hOffset;
        Font font = new Font("TimesRoman", height/2);
        g.setFont(font);
        g.setFill(s.getCurrStroke());
        g.fillText(v.getLabel(), x, y);
    }
    public void drawShape(GraphicsContext g, Vertex v) {
        Shape c = v.getValue();
        double
            width = c.getWidth(),
            height = c.getHeight(),
            x = c.getX()-(width/2),
            y = c.getY()-(height/2);
        if(c.getClass() == Circle.class) {
            g.setFill(c.getCurrFill());
            g.fillOval(x, y, width, height);
            g.setStroke(c.getCurrStroke());
            g.strokeOval(x, y, width, height);
        }
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
//        pin.canvas.setWidth(pin.getWidth()*2);
//        pin.canvas.setHeight(pin.getHeight()*2);
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

    private void updateItems(int x, int y) {
//        double distance = MyMath.calculateDistance(prevX, prevY, x, y);
//        currentItems = gc.findItems(x, y);
//
//        prevItems = currentItems;
    }
    private void updateNodes(int x, int y) {
        previousNode = selectedNode;
        selectedNode = (Circle)gc.findNode(x, y);
        //TODO: FLIP COLOR STATE OF SELECTED/DESELECTED NODE
        //Can this be done without a previous state variable?
        if(selectedNode != null) {
            /*
            if(previousNode != null) {
                Color temp = previousNode.getPrevFill();
                previousNode.setPrevFill(previousNode.getCurrFill());
                previousNode.setCurrFill(temp);
            }
            else {
                Color temp = selectedNode.getPrevFill();
                selectedNode.setPrevFill(selectedNode.getCurrFill());
                selectedNode.setCurrFill(temp);
            }
             */
        }
    }
    private void updateEdges(int x, int y) {
        previousEdge = selectedEdge;
        selectedEdge = gc.findEdge(x, y);
        //temp = gc.findItems(x, y);
        if(selectedEdge != null) {
            Edge temp;
            if(previousEdge != null) {
                temp = gc.getGraph().getEdgeCouple(previousEdge.toString());
                temp.setColor(Color.BLACK);
                previousEdge.setColor(Color.BLACK);
            }
            selectedEdge.setColor(Color.GREEN);
            temp = gc.getGraph().getEdgeCouple(selectedEdge.toString());
            temp.setColor(Color.GREEN);
        }
    }

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
        int curr = (currentItems == null) ? 0 : currentItems.length,
            prev = (prevItems == null) ? 0 : prevItems.length;
        System.out.printf("prevItems: %d, currItems: %d\n", curr, prev);

        prevItems = currentItems;
        //TODO: MAKE SURE CORRECT ITEMS ARE FOUND
        currentItems = gc.findItems(x, y);
        //TODO: MAKE SURE CORRECT ITEMS ARE FILTERED
        //gc.filterItems(currentItems, prevItems, x, y);
        //items are sorted based on distance
        currentItem = currentItems[0];

        mousePressed = true; mouseDragged = true;
        prevX = x; prevY = y;
        repaint();
    }

    public void onMouseReleased(MouseEvent mouseEvent) {
        x = (int) mouseEvent.getX();
        y = (int) mouseEvent.getY();
        mousePressed = false;
        mouseDragged = false;
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
