package controllers;
import javafx.scene.paint.Color;
import models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        Color color = Color.WHITE;
        double vert = 0, hor = 0; //x = horizontal, y = vertical
        int widthLim = (int) (width-diam);
        int heightLim = (int) (height-diam);
        int size = graph.getVertices().size();
        int rowCount = (int)(width/diam); //testing purposes for aligning objects in a row

        int count;
        for(int i = 0; i < size; i++) {
            count = 0;
            Circle circle = new Circle(hor, vert, radius);
            graph.getVertices().get(i).setValue(circle);
            boolean test1, test2, test3;
            do {
                if(count == 20) {
                    widthLim *= 1.5;
                    heightLim *= 1.5;
                    count = 0;
                }
                hor = random.nextInt(widthLim) + radius;
                vert = random.nextInt(heightLim) + radius;
                circle.setX(hor);
                circle.setY(vert);
                //hor  = ((i % rowCount) * diam) + radius;
                //vert = ((i / rowCount) * diam) + radius;
                //TODO: INSTEAD OF CALCULATING OPTIMAL PLACEMENT, RANDOMLY PLACE (NON-OVERLAPPING)
                test1 = false;//intersects(circle); //once placed, check if line interects
                test2 = false;//intersects(graph.getVertices().get(i)); //after placed, check if edges intersect
                test3 = overlaps(circle);
            } while(test1 || test2 || test3);
        }
        //sort Graph (adjacency) lists
        graph.sort();
        //TODO: THEN SHIFT THE CIRCLE SUCH THAT THEY ARE CLOSE TO A MINIMUM AVERAGE DISTANCE
        //sort Graph
        debugAdjacency();
    }
    public void init() {
        //selfSort();
        updateEdges();
    }
    //TODO: SORT INTO GROUPS WITH MINIMUM INTERSECTIONS
    public void selfSort() {
        double uHor, uVert;
        double x, y;
        double count;
        double netDist;

        List<Vertex> degrees = getVertices()
                .stream()
                .sorted(Comparator.comparing(Vertex::getDegree))
                .collect(Collectors.toList());
        //parse each Circle/Vertex in the graph
        for(Vertex v : degrees) {
            uHor = 0.0; uVert = 0.0;
            count = 0;  netDist = 0.0;
            Circle current = (Circle) v.getValue();
            ArrayList<Vertex> list = (ArrayList<Vertex>)v.getAdjacencyList();
            //skip nodes w/ no edge
            if(list.size() == 0)
                continue;
                //sort nodes with only 1 edge
            else if(list.size() == 1) {
                Circle end = (Circle) (list.get(0).getValue());
                x = end.getX();
                y = end.getY();
                double[] uvect = MyMath.getUnitVector(x, y, width/2, height/2);
                current.setX(x + (uvect[0] * diam*2));
                current.setY(y + (uvect[1] * diam*2));
                while(overlaps(current)) {
                    x = current.getX();
                    y = current.getY();
                    current.setX(x + (uvect[0] * diam*2));
                    current.setY(y + (uvect[1] * diam*2));
                }
            }
            //sort/replace from lower to higher degrees
            else {
                Vertex previous = null;

            }
        }
    }

    //GETTERS AND SETTERS
    //--------------------------------
    public double  getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
        diam = radius*2;
    }

    public double  getDiam() {
        return diam;
    }
    public void setDiam(double diam) {
        this.diam = diam;
        radius = diam/2;
    }

    public int  getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int  getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    //--------------------------------

    private double radius = 15;
    private double diam = radius*2;
    private int width = 100;
    private int height = 100;
    private long seed = 1;
    private Random random = new Random(seed);

/*
    //helper functions
    public boolean intersects(Circle current) {
        for(Edge e : graph.getEdges()) {
            Circle from = (Circle) e.getFrom().getValue();
            Circle to = (Circle) e.getTo().getValue();
            if(from == null || to == null) continue;
            if(intersects(current, e))
                return true;
        }
        return false;
    }
    public boolean intersects(Vertex vertex) {
        for(Edge e : (ArrayList<Edge>)vertex.getAdjancencyEList()) {
            for(Vertex v : graph.getVertices()) {
                if(intersects((Circle)v.getValue(), e))
                    return true;
            }
        }
        return false;
    }
    //main function
    public boolean intersects(Circle current, Edge edge) {
        if(current == null)
            return false;
        double x0, y0, x1, y1, x2, y2;
        x0 = current.getX();
        y0 = current.getY();

        x1 = edge.getxStart();
        y1 = edge.getyStart();
        x2 = edge.getxEnd();
        y2 = edge.getyEnd();

        double numerator = (y2-y1)*x0 - (x2-x1)*y0 + x2*y1 - y2*x1;
        numerator = Math.abs(numerator);
        double denominator = Math.pow(y2-y1, 2) + Math.pow(x2-x1, 2);
        denominator = Math.sqrt(denominator);
        double distance = Math.abs(numerator/denominator);
        return distance <= radius+2;
    }
*/
    //helper functions
    public void updateEdges() {
        for(Edge e : graph.getEdges()) {
            //if(e.getFrom().getValue() != null && e.getTo().getValue() != null)
            updateEdge(e);
        }
    }
    public void updateEdges(Vertex vertex) {
        if(vertex == null) return;
        for(Edge e : (ArrayList<Edge>)vertex.getAdjancencyEList()) {
            //if(e.getFrom().getValue() != null && e.getTo().getValue() != null)
            updateEdge(e);
        }
    }
    //main function
    public void updateEdge(Edge e) {
        Circle start = (Circle) e.getFrom().getValue();
        Circle end = (Circle) e.getTo().getValue();
        if(start == null || end == null)
            return;
        double hor = end.getX() - start.getX();
        double vert = end.getY() - start.getY();
        double distance = Math.sqrt(hor * hor + vert * vert);
        double u_hor = hor / distance;
        double u_vert = vert / distance;
        double horShift = (int) (u_hor * start.getRadius());
        double vertShift = (int) (u_vert * start.getRadius());
        e.setXStart(start.getX() + horShift);
        e.setYStart(start.getY() + vertShift);

        horShift = (int) (u_hor * end.getRadius());
        vertShift = (int) (u_vert * end.getRadius());
        e.setXEnd(end.getX() - horShift);
        e.setYEnd(end.getY() - vertShift);
        e.setUHorizontal(u_hor);
        e.setUVertical(u_vert);
    }

    public boolean overlaps(Circle circle){
        for(int i = 0; i < graph.getVertices().size(); i++) {
            Circle temp = (Circle)graph.getVertices().get(i).getValue();
            if(temp == null || temp == circle) continue;
            if(MyMath.overlappingCircles(circle, temp))
                return true;
        }
        return false;
    }

    public Object findNode(int x, int y) {
        Circle current = new Circle(x, y);
        for(Vertex v : graph.getVertices()) {
            if(v.getValue() == null) continue;
            if (MyMath.overlappingCircles(current, (Circle)v.getValue()))
                return v.getValue();
        }
        return null;
    }
    //main function

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
        radius *= (1+mult);
        diam = radius*2;
        if(graph == null) return;
        for(Vertex v : graph.getVertices()) {
            if(v.getValue() == null) continue;
            Circle current = (Circle) v.getValue();
            double r = current.getRadius();
            current.setRadius(radius);
        }
        updateEdges();
    }

    public void rotateGraph(double pivotX, double pivotY, double alpha) {
        Circle circle;
        for (Vertex v : getVertices()) {
            circle = (Circle) v.getValue();
            if (circle == null) continue;
            double
                    x2, y2, step[];
            x2 = circle.getX();
            y2 = circle.getY();
            step = rotateLineAbout(pivotX, pivotY, x2, y2, alpha);
            circle.setX(step[0]);
            circle.setY(step[1]);
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
            circle.setX(xPoints[j]);
            circle.setY(yPoints[j]);
        }

    }
}
