package controllers;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import static models.MyMath.distancePointFromLine;
import static models.MyMath.rotateLineAbout;

public class GraphController {
    private Graph graph = null;
    private double spacing = 1.5;
    private static String charSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public char[] getCharSet(int nodes) {
        char[] set = new char[nodes];
        for(int i = 0; i < nodes; i++) {
            set[i] = charSet.charAt(i);
        }
        return set;
    }
    public void generateRandomGraph(int nodeLimit, int edgeLimit) {
        graph = new Graph();
        char[] charSet = getCharSet(nodeLimit);
        char v1, v2;
        int size = charSet.length;
        for(int i = 0; i < edgeLimit; i++) {
            v1 = charSet[random.nextInt(size)];
            do {
                v2 = charSet[random.nextInt(size)];
            }while(v1 == v2);
            graph.addVertices(v1 + "", v2 + "");
        }
    }
    public void calculatePlacement() {
        if(graph == null)
            return;
        int shapeWidth = 15;
        int shapeHeight = 15;
        width = CanvasController.getWidth();
        height = CanvasController.getHeight();
        double vert = 0, hor = 0; //x = horizontal, y = vertical
        int widthLim = width-shapeWidth;
        int heightLim = height-shapeHeight;
        int size = graph.getVertices().size();
        int rowCount = width/shapeWidth; //testing purposes for aligning objects in a row

        int count;
        for(int i = 0; i < size; i++) {
            count = 0;
            Shape circle = new Circle(0, 0, shapeWidth, shapeHeight);
            graph.getVertices().get(i).setValue(circle);
            boolean test1, test2, test3;
            do {
                if(count == 20) {
                    widthLim *= 1.5;
                    heightLim *= 1.5;
                    count = 0;
                }
                hor = random.nextInt(widthLim) + shapeWidth/2;
                vert = random.nextInt(heightLim) + shapeHeight/2;
                circle.setX((int)hor);
                circle.setY((int)vert);
                //hor  = ((i % rowCount) * diam) + radius;
                //vert = ((i / rowCount) * diam) + radius;
                //TODO: INSTEAD OF CALCULATING OPTIMAL PLACEMENT, RANDOMLY PLACE (NON-OVERLAPPING)
                test1 = false;//intersects(circle); //once placed, check if line interects
                test2 = false;//intersects(graph.getVertices().get(i)); //after placed, check if edges intersect
                test3 = false;//overlaps(circle);
            } while(test1 || test2 || test3);
        }
        //sort Graph (adjacency) lists
        graph.sort();
        //TODO: THEN SHIFT THE CIRCLE SUCH THAT THEY ARE CLOSE TO A MINIMUM AVERAGE DISTANCE
    }
    public void init() {
        //selfSort();
        updateEdges();
    }
    //TODO: SORT INTO GROUPS WITH MINIMUM INTERSECTIONS
    public void selfSort() {
        int
            x, y,
            shapeWidth,
            shapeHeight;

        List<Vertex> degrees = getVertices()
                .stream()
                .sorted(Comparator.comparing(Vertex::getDegree))
                .collect(Collectors.toList());
        //parse each Circle/Vertex in the graph
        for(Vertex v : degrees) {
            Circle current = (Circle) v.getValue();
            ArrayList<Vertex> list = (ArrayList<Vertex>)v.getAdjacencyList();
            //skip nodes w/ no edge
            if(list.size() == 0)
                continue;
                //sort nodes with only 1 edge
            else if(list.size() == 1) {
                //TODO: REFACTOR TO CONSIDER ANY SHAPE BEING SELF-SORTED
                Circle end = (Circle) (list.get(0).getValue());
                x = end.getX();
                y = end.getY();
                shapeWidth = (int) end.getWidth();
                shapeHeight = (int) end.getHeight();
                double[] uvect = MyMath.getUnitVector(x, y, shapeWidth/2.0, shapeHeight/2.0);
                do {
                    x += (int)(uvect[0] * shapeWidth);
                    y += (int)(uvect[1] * shapeHeight);
                    current.setX(x);
                    current.setY(y);
                }while(overlaps(current));
            }
            //sort/replace from lower to higher degrees
            else {
                Vertex previous = null;

            }
        }
    }

    //--------------------------------
    private int width;
    private int height;

    private long seed = 1;
    private Random random = new Random(seed);

    //helper functions
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
        double hor = end.getX() - start.getX();
        double vert = end.getY() - start.getY();
        double distance = Math.sqrt(hor * hor + vert * vert);
        double u_hor = hor / distance;
        double u_vert = vert / distance;
        double horShift = (int) (u_hor * start.getWidth());
        double vertShift = (int) (u_vert * start.getHeight());
        e.setXStart(start.getX() + horShift);
        e.setYStart(start.getY() + vertShift);

        horShift = (int) (u_hor * end.getWidth());
        vertShift = (int) (u_vert * end.getHeight());
        e.setXEnd(end.getX() - horShift);
        e.setYEnd(end.getY() - vertShift);
    }

    public boolean overlaps(Shape shape){
        for(int i = 0; i < graph.getVertices().size(); i++) {
            Shape temp = graph.getVertices().get(i).getValue();
            if(temp == null || temp == shape) continue;
            //if(MyMath.overlappingCircles(circle, temp))
            if(shape.distanceFromBounds(temp) < 0)
                return true;
        }
        return false;
    }

    public Item[] findItems(int x, int y) {
        List<Object> list = new ArrayList<>();
        list.addAll(findEdges(x, y));
        list.addAll(findNodes(x, y));
        return (Item[]) list.stream().toArray();
    }

    public Edge findEdge(int x, int y) {
        List<Item> items = new ArrayList<>();
        //x, y represents mouse click
        int pxThreshold = 4;
        int epsilon = 10;
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
            if(!MyMath.isBetween(x1, x, x2, 1)) continue;
            else if(!MyMath.isBetween(y1, y, y2, epsilon)) continue;
            //DISTANCE
            if(distance <= pxThreshold)
                return e;
        }
        return null;
    }
    public Circle findNode(int x, int y) {
        List<Item> items = new ArrayList<>();
        for(Vertex v : graph.getVertices()) {
            if(v.getValue() == null) continue;
            Shape other = v.getValue();
            if (other.distanceFromBounds(other) < 3)
                return (Circle)v.getValue();
        }
        Collections.sort(items);
        return null;
    }

    public List<Item> findEdges(int x, int y) {
        List<Item> items = new ArrayList<>();
        //x, y represents mouse click
        int pxThreshold = 4;
        int epsilon = 10;
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
            if(!MyMath.isBetween(x1, x, x2, 1)) continue;
            else if(!MyMath.isBetween(y1, y, y2, epsilon)) continue;
            //DISTANCE
            if(distance <= pxThreshold)
                items.add(new Item(e, distance));
        }
        Collections.sort(items);
        return items;
    }
    public List<Item> findNodes(int x, int y) {
        List<Item> items = new ArrayList<>();
        for(Vertex v : graph.getVertices()) {
            if(v.getValue() == null) continue;
            Circle other = (Circle)v.getValue();
            double distance = Math.abs(MyMath.calculateDistance(other.getX(), other.getY(), x, y));
            if (other.distanceFromBounds(other) < 3)
                items.add(new Item(v, distance));
        }
        Collections.sort(items);
        return items;
    }

    //View <-> Controller data interaction (for single Circle instance)
    public Object getNode(int index) { return graph.getVertices().get(index).getValue(); }
    //View <-> Controller data interaction (for PaintComponent)
    public List<Vertex> getVertices() {
        if(graph == null)
            return null;
        return graph.getVertices();
    }
    public List<Edge> getEdges() {
        if(graph == null)
            return null;
        return graph.getEdges();
    }

    public Graph getGraph() { return graph; }
    public void debug() {
        System.out.println("Printing vertices");
        for(Vertex v : getVertices())
            System.out.println(v);
        System.out.println();

        System.out.println("Printing undirected edges");
        for(Edge e: getEdges())
            System.out.println(e);

        System.out.println("\nPrinting directed edges");
        for(Edge e: getEdges()) {
            if (e.isDirected())
                System.out.println(e);
            if(e.getFrom() == null)
                System.out.printf("from (%s) is null\n", e.getFrom());
            if(e.getTo() == null)
                System.out.printf("to (%s) is null\n", e.getTo());
        }
        System.out.println();
    }
    public void debugAdjacency(){
        System.out.println("Printing adjacency lists");
        for(Vertex v: graph.getVertices()) {
            System.out.println(v + " : " + v.getDegree());
            for(Vertex v2 : (ArrayList<Vertex>)v.getAdjacencyList()) {
                System.out.println("\t" + v2);
            }
        }

        System.out.println("\nPrinting Edge list");
        for(Edge e : graph.getEdges()) {
            if(e.isDirected())
                System.out.println(e);
        }
        int j = 0;
    }

    public void setGraph(Graph g) {
        graph = g;
    }
    public void resizeGraph(double mult) {
        if(Math.abs(mult) < 0.01)
            return;
        mult += 1;
        double
            shapeWidth,
            shapeHeight;
        if(graph == null) return;
        for(Vertex v : graph.getVertices()) {
            if(v.getValue() == null) continue;
            Shape current = v.getValue();
            shapeWidth = (current.getWidth() * mult);
            shapeHeight = (current.getHeight() * mult);
            current.setWidth((int)shapeWidth);
            current.setHeight((int)shapeHeight);
        }
        updateEdges();
    }

    public void rotateGraph(double pivotX, double pivotY, double alpha) {
        Shape shape;
        for (Vertex v : getVertices()) {
            shape = v.getValue();
            if (shape == null) continue;
            double
                    x2, y2, step[];
            x2 = shape.getX();
            y2 = shape.getY();
            step = rotateLineAbout(pivotX, pivotY, x2, y2, alpha);
            shape.setX((int)step[0]);
            shape.setY((int)step[1]);
        }
    }
    public void rotateGraphAveragePivot(double alpha) {
        List<Vertex> list = getVertices();
        //try and implement rotate plane
        double
                xPoints[] = new double[list.size()],
                yPoints[] = new double[list.size()];

        Circle circle;
        for (int i = 0; i < list.size(); i++) {
            circle = (Circle) list.get(i).getValue();
            if (circle == null) continue;
            xPoints[i] = circle.getX();
            yPoints[i] = circle.getY();
        }

        MyMath.rotatePlane(xPoints, yPoints, alpha);
        for (int j = 0; j < list.size(); j++) {
            circle = (Circle) list.get(j).getValue();
            circle.setX((int)xPoints[j]);
            circle.setY((int)yPoints[j]);
        }

    }
}
