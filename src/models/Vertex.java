package models;

import java.util.ArrayList;

public class Vertex<E> {
    private E value;
    private String label;
    private boolean isVisited = false;
    private ArrayList<Vertex> adjacencyList = new ArrayList<>();
    private ArrayList<Edge> edgeList = new ArrayList<>();

    public Vertex(String label) {
        this.label = label;
        value = null;
    }
    public Vertex(String label, E val) {
        this.label = label;
        value = val;
    }

    //GETTERS AND SETTERS
    //--------------------------------
    public E getValue() {
        return value;
    }
    public void setValue(E value) { this.value = value; }

    private void addAdjacent(Vertex other) {
        adjacencyList.add(other);
    }
    public void addEdge(Edge other) {
        edgeList.add(other);
        addAdjacent(other.getTo());
    }

    public ArrayList<Vertex> getAdjacencyList() {
        return adjacencyList;
    }
    public ArrayList<Edge> getAdjancencyEList() { return edgeList; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isVisited() {
        return isVisited;
    }
    public void setVisited(boolean visited) {
        isVisited = visited;
    }
    //--------------------------------

    @Override
    public String toString() {
        return label;
    }
    public boolean equals(Vertex other) {
        return label.equals(other.getLabel());
    }


}
