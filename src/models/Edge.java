package models;

import javafx.scene.paint.Color;

public class Edge implements Comparable<Edge> {
    private Vertex from;
    private Vertex to;
    private Object value;
    private boolean directed = false;
    private String label;
    private Color color = Color.BLACK;

    //SETTING UP THE GRAPH STRUCTURE
    public Edge(Vertex a, Vertex b, String label) {
        from = a;
        to = b;
        this.label = label;
        this.value = null;
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

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    //--------------------------------

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

    //DRAWING EDGES IN PAINT COMPONENT
    private double xStart, xEnd; //horizontal
    private double yStart, yEnd; //vertical
    private double u_hor;
    private double u_vert;

    public double getXStart() {
        return xStart;
    }
    public void setXStart(double xStart) {
        this.xStart = xStart;
    }

    public double getXEnd() {
        return xEnd;
    }
    public void setXEnd(double xEnd) {
        this.xEnd = xEnd;
    }

    public double  getYStart() {
        return yStart;
    }
    public void setYStart(double yStart) {
        this.yStart = yStart;
    }

    public double getYEnd() {
        return yEnd;
    }
    public void setYEnd(double yEnd) {
        this.yEnd = yEnd;
    }

    public double getUHorizontal() { return u_hor; }
    public void setUHorizontal(double h) { u_hor = h; }

    public double getUVertical() { return u_vert; }
    public void setUVertical(double v) { u_vert = v; }
}
