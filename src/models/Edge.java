package models;

public class Edge<E> {
    private Vertex from;
    private Vertex to;
    private E value;
    private boolean directed = false;
    private String label;

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

    public E getValue() {
        return value;
    }
    public void setValue(E value) {
        this.value = value;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    //--------------------------------

    @Override
    public String toString() {
        return String.format("%s -> %s", from, to);
    }
    public boolean equals(Edge other) {
        return label.equals(other.getLabel());
    }

    //DRAWING EDGES IN PAINT COMPONENT
    private double xStart, xEnd; //horizontal
    private double yStart, yEnd; //vertical
    private double u_hor;
    private double u_vert;

    public double getxStart() {
        return xStart;
    }
    public void setxStart(double xStart) {
        this.xStart = xStart;
    }

    public double getxEnd() {
        return xEnd;
    }
    public void setxEnd(double xEnd) {
        this.xEnd = xEnd;
    }

    public double  getyStart() {
        return yStart;
    }
    public void setyStart(double yStart) {
        this.yStart = yStart;
    }

    public double getyEnd() {
        return yEnd;
    }
    public void setyEnd(double yEnd) {
        this.yEnd = yEnd;
    }

    public double getUHorizontal() { return u_hor; }
    public void setUHorizontal(double h) { u_hor = h; }

    public double getUVertical() { return u_vert; }
    public void setUVertical(double v) { u_vert = v; }
}
