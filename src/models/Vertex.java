package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vertex implements Comparable<Vertex>{
    private Object value;
    private String label;
    private boolean isVisited = false;
    private List<Vertex> adjacencyList = new ArrayList<>();
    private List<Edge> edgeList = new ArrayList<>();
    private int degree;

    public Vertex(String label) {
        this.label = label;
        value = null;
    }
    public Vertex(String label, Object val) {
        this.label = label;
        value = val;
    }

    //GETTERS AND SETTERS
    //--------------------------------
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) { this.value = value; }

    private void addAdjacent(Vertex other) {
        int i;
        for(i = 0; i < adjacencyList.size(); i++) {
            if(adjacencyList.get(i).equals(other))
                return;
            if(toString().compareTo(other.toString()) > 0)
                break;
        }
        adjacencyList.add(i, other);
    }
    public void addEdge(Edge other) {
        int i;
        for(i = 0; i < edgeList.size(); i++) {
            if(edgeList.get(i).equals(other))
                return;
            if(edgeList.get(i).toString().compareTo(other.toString()) > 0)
                break;
        }
        edgeList.add(i, other);
        addAdjacent(other.getTo());
        degree++;
    }

    public void sort() {
        Collections.sort(edgeList);
        Collections.sort(adjacencyList);
    }
    public List<Vertex> getAdjacencyList() {
        return adjacencyList;
    }
    public List<Edge> getAdjancencyEList() { return edgeList; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isVisited() {
        return isVisited;
    }
    public void setVisited(boolean visited) {
        isVisited = visited;
    }
    //--------------------------------
    public int getDegree() { return degree; }

    @Override
    public String toString() {
        return label;
    }
    public boolean equals(Vertex other) {
        return label.equals(other.getLabel());
    }

    @Override
    public int compareTo(Vertex o) {
        return toString().compareTo(o.toString());
    }
}
