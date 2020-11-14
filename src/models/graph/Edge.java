package models.graph;

import javafx.scene.paint.Color;
import shapes.Line;
import shapes.Shape;

public class Edge implements Comparable<Edge> {
    private Vertex from;
    private Vertex to;
    private Shape line = new Line(0, 0, 0, 0);
    private boolean directed = false;
    private String label;
    private Color color = Color.BLACK;
    private int state = 0;

    //SETTING UP THE GRAPH STRUCTURE
    public Edge(Vertex a, Vertex b, String label) {
        from = a;
        to = b;
        this.label = label;
        line.setValue(null);
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
        return line;
    }
    public void setValue(Object value) {
        this.line.setValue(value);
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    //--------------------------------
    public double getXStart() {
        return line.getX();
    }
    public void setXStart(double xStart) {
        line.setX((int)xStart);
    }

    public double getXEnd() {
        return line.getWidth();
    }
    public void setXEnd(double xEnd) {
        line.setWidth((int)xEnd);
    }

    public double  getYStart() {
        return line.getY();
    }
    public void setYStart(double yStart) {
        line.setY((int)yStart);
    }

    public double getYEnd() {
        return line.getHeight();
    }
    public void setYEnd(double yEnd) {
        line.setHeight((int)yEnd);
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
