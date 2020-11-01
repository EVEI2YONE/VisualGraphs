package controllers;
import javafx.scene.paint.Color;
import models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class GraphController {
    private Graph graph = null;

    public void generateRandomGraph(int limit) {
        graph = new Graph();
        char v1, v2;
        int type, base, offset;
        for(int i = 0; i < limit; i++) {
            type = random.nextInt(3);
            base = 0;
            switch(type) {
                case 0: //lowercase
                    base = 'a';
                    offset = 26;
                    break;
                case 1: //uppercase
                    base = 'A';
                    offset = 26;
                    break;
                default: //digits
                    base = '0';
                    offset = 10;
                    break;
            }
            v1 = (char) (random.nextInt(offset) + base);
            v2 = (char) (random.nextInt(offset) + base);
            graph.addVertices(v1 + "", v2 + "");
        }
    }
    public boolean readFile(String filename) {
        graph = new Graph();
        try {
            File file = new File(filename);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] v;
            while((line = br.readLine()) != null) {
                line = line.trim();
                v = line.split("\\s+");
                graph.addVertices(v[0], v[v.length-1]);
            }
        } catch(Exception e) {
            //e.printStackTrace();
            System.out.println("File doesn't exist!");
            return false;
        }
        return true;
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
    private Random random = new Random();

    public void init() {
        if(graph == null)
            return;
        Color color = Color.BLACK;
        double vert = 0, hor = 0; //x = horizontal, y = vertical
        int widthLim = (int) (width-diam);
        int heightLim = (int) (height-diam);
        int size = graph.getVertices().size();
        int rowCount = (int)(width/diam); //testing purposes for aligning objects in a row

        for(int i = 0; i < size; i++) {
            Circle circle = new Circle(hor, vert, radius, color);
            graph.getVertices().get(i).setValue(circle);
            boolean test1, test2, test3;
            do {
                updateEdges();
                updateEdges(graph.getVertices().get(i));
                hor  = random.nextInt(widthLim) + radius;
                vert = random.nextInt(heightLim) + radius;
                circle.setX(hor);
                circle.setY(vert);
                    //hor  = ((i % rowCount) * diam) + radius;
                    //vert = ((i / rowCount) * diam) + radius;
                test1 = intersects(circle);
                test2 = intersects(graph.getVertices().get(i));
                test3 = overlaps(circle);
            } while(test1 || test2 || test3);
        }
        updateEdges();


    }

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
        int x0 = current.getX();
        int y0 = current.getY();

        int x1 = edge.getxStart();
        int y1 = edge.getyStart();
        int x2 = edge.getxEnd();
        int y2 = edge.getyEnd();

        int numerator = (y2-y1)*x0 - (x2-x1)*y0 + x2*y1 - y2*x1;
        numerator = Math.abs(numerator);
        double denominator = Math.pow(y2-y1, 2) + Math.pow(x2-x1, 2);
        denominator = Math.sqrt(denominator);
        double distance = Math.abs(numerator/denominator);
        return distance <= radius+2;
    }

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
        int hor = start.getX() - end.getX();
        int vert = start.getY() - end.getY();
        double distance = Math.sqrt(hor * hor + vert * vert);
        double u_hor = hor / distance;
        double u_vert = vert / distance;
        int horShift = (int) (u_hor * radius);
        int vertShift = (int) (u_vert * radius);
        e.setxStart(start.getX() - horShift);
        e.setyStart(start.getY() - vertShift);
        e.setxEnd(end.getX() + horShift);
        e.setyEnd(end.getY() + vertShift);
    }

    //helper functions
    public boolean overlaps(Circle circle){
        int hor = circle.getX();
        int vert = circle.getY();
        for(int i = 0; i < graph.getVertices().size(); i++) {
            Circle temp = (Circle)graph.getVertices().get(i).getValue();
            if(temp == null || temp == circle) continue;
            if(calculateDistance(hor, vert, temp) < radius)
                return true;
        }
        return false;
    }
    public Object findNode(int x, int y) {
        for(Vertex v : graph.getVertices())
            if(calculateDistance(x, y, (Circle)v.getValue()) < radius)
                return v.getValue();
        return null;
    }
    //main function
    public double calculateDistance(int hor, int vert, Circle circle) {
        if(circle == null)
            return Double.MAX_VALUE;
        int hor2 = circle.getX();
        int vert2 = circle.getY();
        double x2 = Math.pow(hor2-hor, 2);
        double y2 = Math.pow(vert2-vert, 2);
        double distance = Math.sqrt(x2 + y2);
        return Math.abs(distance);
    }

    //View <-> Controller data interaction (for single Circle instance)
    public Object getNode(int index) { return graph.getVertices().get(index).getValue(); }
    //View <-> Controller data interaction (for PaintComponent)
    public ArrayList<Vertex> getVertices() {
        if(graph == null)
            return null;
        return graph.getVertices();
    }
    public ArrayList<Edge> getEdges() {
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
}
