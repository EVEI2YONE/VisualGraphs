package views.main.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.Item;
import models.graph.Edge;
import models.graph.Graph;
import models.graph.MyMath;
import models.graph.Vertex;
import models.shapes.Arrow;
import models.shapes.Circle;
import models.shapes.Line;
import models.shapes.Shape;
import views.main.graph_helper.BSTGraphHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CanvasControls {
    private Canvas canvas;
    private Graph graph;

    public CanvasControls(Canvas c) {
        canvas = c;
        init();
    }
    public void setGraph(Graph g) {
        graph = g;
    }
    public void setCanvas(Canvas c) { canvas = c; }

    //TODO: IMPLEMENT GRAPH MANIPULATION FOR DRAG AND DROP
    public void init() {
        canvas.setOnMousePressed(e -> {
            if(graph == null) return;
            onMousePressed(e);
        });

        canvas.setOnMouseDragged(e -> {
            if(graph == null) return;
            onMouseDragged(e);
        });

        canvas.setOnMouseReleased(e -> {
            if(graph == null) {
                System.out.println("graph is null");
                return;
            }
            onMouseReleased(e);
        });
        canvas.setOnKeyPressed(e -> {
            if(graph == null) return;
            onKeyPressed(e.getCode());
        });
        canvas.setOnKeyReleased(e -> {
            if(graph == null) return;
            onKeyReleased(e.getCode());
        });
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

    /*
    TODO: FIGURE OUT HOW TO SELECT NODE OR EDGE,
        THEN UPDATE CONNECTIONS ACCORDINGLY (WITHOUT GRAPH HELPER),
        AND REPAINT THE ENTIRE GRAPH (DUE TO INTERSECTING LINES)
     */
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
                Vertex v1 = graph.getVertex(shape);
                newEdge = new Edge(v1, null, "temp");
                Arrow arrow = new Arrow(shape.getX(), shape.getY(), x, y);
                newEdge.setValue(arrow);
            }
            if(newEdge != null) {
                updateNewEdge(newEdge, x, y);
            }
        }
        else {
            currentItem.getItem().setX(x);
            currentItem.getItem().setY(y);
        }
        updateEdges();
        displayGraph();
    }
    private boolean mousePressed;
    public void onMousePressed(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        prevItems = currentItems;
        currentItems = findItems(x, y);
        //items are sorted based on distance
        if(currentItems.length == 0) return;
        currentItem = currentItems[0];
        mousePressed = true; mouseDragged = true;
        prevX = x; prevY = y;
        displayGraph();
    }

    public void onMouseReleased(MouseEvent mouseEvent) {
        x = (int) mouseEvent.getX();
        y = (int) mouseEvent.getY();
        System.out.printf("%d, %d\n", x, y);
        mousePressed = false;
        mouseDragged = false;
        if(addEdge) {
            addEdge();
            graph.sort();
            graph.debug();
            collectGraphShapes();
            newEdge = null;
            addEdge = false;
        }
        currentItem = null;
        displayGraph();
    }

    //TODO: BE RIGHT BACK
    private KeyCode keyPressed;;
    private static Edge line = new Edge(null, null, "temp");
    public void onKeyPressed(KeyCode event) {
//        keyPressed = event;
//        if(keyPressed == KeyCode.SHIFT) {
//            Vertex v = null; //pin.gc.getGraph().getVertex(pin.selectedNode);
//            if(v == null) return;
//            line.setFrom(v);
//            line.setXStart(prevX);
//            line.setYStart(prevY);
//        }
//        paintComp();
        //used with item selection and mouse drag
    }
    public void onKeyReleased(KeyCode event) {
//        keyPressed = event;
//        if(keyPressed == KeyCode.DELETE) {
//            if(currentItems != null && currentItems.length > 0) {
//                Shape shape = currentItems[0].getItem();
//                Graph g = gc.getGraph();
//                if (shape.getClass() == Circle.class) {
//                    g.removeVertex(shape);
//                } else if (shape.getClass() == Line.class || shape.getClass() == Arrow.class) {
//                    Edge edge = g.getEdge(shape);
//                    Shape couple = g.getEdgeCouple(edge.getLabel()).getValue();
//                    g.removeEdge(shape);
//                }
//                collectGraphShapes();
//                currentItem = null;
//                currentItems = null;
//            }
//        }
//        else if(keyPressed == KeyCode.SHIFT) {
//            newEdge = null;
//        }
//        keyPressed = null;
//        //used with mouse drag
//        paintComp();
    }








    public void updateEdges() {
        for(Edge e : graph.getEdges()) {
            //if(e.getFrom().getValue() != null && e.getTo().getValue() != null)
            updateEdge(e);
        }
    }
    public void updateEdges(Vertex vertex) {
        if(vertex == null) return;
        for(Edge e : (ArrayList<Edge>)vertex.getAdjacencyEList()) {
            //if(e.getFrom().getValue() != null && e.getTo().getValue() != null)
            updateEdge(e);
        }
    }
    //main function
    public void updateEdge(Edge e) {
        Shape start = e.getFrom().getValue();
        Shape end = e.getTo().getValue();
        if(start == null || end == null)
            return;
        double
                hor = end.getX() - start.getX(),
                vert = end.getY() - start.getY(),
                distance = Math.sqrt(hor * hor + vert * vert),
                u_hor = hor / distance,
                u_vert = vert / distance,
                horShift = (int) (u_hor * start.getWidth()/2.0),
                vertShift = (int) (u_vert * start.getHeight()/2.0);
        e.setXStart(start.getX() + horShift);
        e.setYStart(start.getY() + vertShift);

        horShift = (int) (u_hor * end.getWidth()/2.0);
        vertShift = (int) (u_vert * end.getHeight()/2.0);
        e.setXEnd(end.getX() - horShift);
        e.setYEnd(end.getY() - vertShift);
    }
    public void updateNewEdge(Edge newEdge, int x, int y) {
        Shape start = newEdge.getFrom().getValue();
        if(start == null) return;
        double
                hor = x - start.getX(),
                vert = y - start.getY(),
                distance = Math.sqrt(hor*hor + vert*vert),
                u_hor = hor/distance,
                u_vert = vert/distance,
                horShift = (int) (u_hor * start.getWidth()/2.0),
                vertShift = (int) (u_vert * start.getHeight()/2.0);
        newEdge.setXStart(start.getX() + horShift);
        newEdge.setYStart(start.getY() + vertShift);

        newEdge.setXEnd(x);
        newEdge.setYEnd(y);
    }
    public void addEdge() {
        List<Item> tempList = findNodes(x, y);
        if(tempList.size() > 0) {
            Shape shape = tempList.get(0).getItem();
            if(shape.getClass() == Circle.class) {
                Vertex v2 = graph.getVertex(shape);
                String
                        a = newEdge.getFrom().getLabel(),
                        b = v2.getLabel(),
                        label = a + " -> " + b;
                Edge edge = graph.getEdge(label);
                if(edge == null) { //edge doesn't exist, so insert
                    edge = graph.addVertices(a, b);
                    edge.setValue(newEdge.getValue()); //update shape
                    edge = graph.getEdgeCouple(label);
                    edge.setValue(new Line(0, 0, 0, 0));
                } else if(!edge.isDirected()) { //edge exists, but isn't an arrow
                    edge.setValue(newEdge.getValue());
                    edge.setDirected(true);
                }
                updateEdges();
            }
        }
    }
    public List<Item> findNodes(int x, int y) {
        List<Item> items = new ArrayList<>();
        for(Vertex v : graph.getVertices()) {
            if(v.getValue() == null) continue;
            Circle current = (Circle)v.getValue();
            double distance = current.pointDistanceFromBounds(x, y);
            if(distance < 3) {
                items.add(new Item(current, distance));
                Collections.sort(items);
                if(items.size() == 3)
                    items.remove(2);
            }
        }
        return items;
    }
    public Item[] findItems(int x, int y) {
        List<Item> list = new ArrayList<>();
        list.addAll(findEdges(x, y));
        list.addAll(findNodes(x, y));
        Collections.sort(list);
        //convert List<Item> to Item[]
        Item[] array = new Item[list.size()];
        list.toArray(array);
        return array;
    }
    public List<Item> findEdges(int x, int y) {
        if(graph == null) return null;
        List<Item> items = new ArrayList<>();
        //x, y represents mouse click
        int pxThreshold = 4;
        int epsilon = 2;
        for(Edge e : graph.getEdges()) {
            double
                    x1 = e.getXStart(),
                    y1 = e.getYStart(),
                    x2 = e.getXEnd(),
                    y2 = e.getYEnd(),
                    xPoints[] = {x1, x, x2},
                    yPoints[] = {y1, y, y2};
            double distance = Math.round(MyMath.distancePointFromLine(xPoints, yPoints));
            //RANGE EXCLUSION
            if(!MyMath.isBetween(x1, x, x2, epsilon)) continue;
            else if(!MyMath.isBetween(y1, y, y2, epsilon)) continue;
            //DISTANCE
            if(distance <= pxThreshold) {
                items.add(new Item(e.getValue(), distance));
                Collections.sort(items);
                if(items.size() == 3) {
                    items.remove(2);
                }
            }
        }
        return items;
    }






    //DISPLAY GRAPH
    public void displayGraph() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        clearCanvas(g);
        if(graph == null) {
            System.out.println("graph is null - clearing canvas");
            return;
        }
        if(graph.getVertices() == null || graph.getEdges() == null)
            return;

        //paint edges
        for (Shape shape : shapes) {
            shape.displayShape(g);
            shape.displayText(g);
        }

        int x = (int)(canvas.getWidth()/2 - BSTGraphHelper.boxwidth/2);
        int y = (int)(canvas.getHeight()/2 - BSTGraphHelper.boxheight/2);

        g.setStroke(Color.BLACK);
        g.strokeRect(x, y, BSTGraphHelper.boxwidth, BSTGraphHelper.boxheight);

    }
    private List<Shape> shapes = new ArrayList<>();
    public void collectGraphShapes() {
        shapes.clear();
        if(graph == null) {
            System.out.println("graph is null - clearing shapes");
            return;
        }
        for (Edge e : graph.getEdges())
            if(e.getValue() != null) {
                //update whether to draw arrows on edges
                if(e.getValue().getClass() == Arrow.class) {
                    boolean drawArrow = false;
                    if(graph.isDirected()) {
                        drawArrow = true;
                    }
                    ((Arrow)e.getValue()).setDrawArrow(drawArrow);
                }
                shapes.add(e.getValue());
            }
        //paint vertices
        for (Vertex v : graph.getVertices()) {
            if(v.getValue() != null)
                shapes.add(v.getValue());
        }
    }
    public void clearCanvas(GraphicsContext g) {
        g.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
    }
}
