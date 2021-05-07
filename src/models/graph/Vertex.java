package models.graph;

import models.shapes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vertex implements Comparable<Vertex>{
    private Shape value;
    private String label;
    private boolean isVisited = false;
    private List<Vertex> adjacencyList = new ArrayList<>();
    private List<Edge> edgeList = new ArrayList<>();
    private int degree;

    public Vertex(String label) {
        this.label = label;
        value = null;
    }
    public Vertex(String label, Shape val) {
        this.label = label;
        value = val;
    }

    //GETTERS AND SETTERS
    //--------------------------------
    public Shape getValue() {
        return value;
    }
    public void setValue(Shape value) { this.value = value; }

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
    public List<Edge> getAdjacencyEList() { return edgeList; }

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
        //create own lexigraphical comparison -_-
        String a = toString();
        String b = o.toString();
        boolean aNum = isNumber(a);
        boolean bNum = isNumber(b);
        if(aNum && bNum) {
            return (Integer.parseInt(a) - Integer.parseInt(b));
        }
        else {
            return a.compareTo(b);
        }
    }

    private boolean isNumber(String value) {
        try {
            Integer valid = Integer.parseInt(value.toString());
        }catch(Exception e) {
            return false;
        }
        return true;
    }

    //---------------- REMOVAL ----------------
    public void removeEdge(Edge edge) {
        edgeList.remove(edge);
        degree--;
    }
    public void removeAdjacent(Vertex vertex) {
        adjacencyList.remove(vertex);
    }
}
