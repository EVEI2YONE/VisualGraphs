package models.graph;

import models.shapes.*;

public class Edge implements Comparable<Edge> {
    private Vertex from;
    private Vertex to;
    private Shape value;
    private boolean directed = false;
    private String label;
    private int state = 0;

    //SETTING UP THE GRAPH STRUCTURE
    public Edge(Vertex a, Vertex b, String label) {
        from = a;
        to = b;
        this.label = label;
        value = null;
    }

    //GETTERS AND SETTERS
    //--------------------------------
    public boolean isDirected() {
        return directed;
    }
    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public Vertex getFrom() {
        return from;
    }
    public void setFrom(Vertex from) {
        this.from = from;
    }

    public Vertex getTo() {
        return to;
    }
    public void setTo(Vertex to) {
        this.to = to;
    }

    public Shape getValue() {
        return value;
    }
    public void setValue(Shape value) {
        this.value = value;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    //--------------------------------
    public double getXStart() {
        return value.getX();
    }
    public void setXStart(double xStart) {
        value.setX((int)xStart);
    }

    public double getXEnd() {
        return value.getWidth();
    }
    public void setXEnd(double xEnd) {
        value.setWidth((int)xEnd);
    }

    public double  getYStart() {
        return value.getY();
    }
    public void setYStart(double yStart) {
        value.setY((int)yStart);
    }

    public double getYEnd() {
        return value.getHeight();
    }
    public void setYEnd(double yEnd) {
        value.setHeight((int)yEnd);
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", from, to);
    }
    public boolean equals(Edge other) {
        return label.equals(other.getLabel());
    }
    @Override
    public int compareTo(Edge o) {
        return toString().compareTo(o.toString());
    }
}
