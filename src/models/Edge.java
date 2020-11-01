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
    private int xStart, xEnd; //horizontal
    private int yStart, yEnd; //vertical

    public int getxStart() {
        return xStart;
    }

    public void setxStart(int xStart) {
        this.xStart = xStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public void setxEnd(int xEnd) {
        this.xEnd = xEnd;
    }

    public int getyStart() {
        return yStart;
    }

    public void setyStart(int yStart) {
        this.yStart = yStart;
    }

    public int getyEnd() {
        return yEnd;
    }

    public void setyEnd(int yEnd) {
        this.yEnd = yEnd;
    }

}
