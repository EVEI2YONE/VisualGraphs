package models;

import controllers.CanvasController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Graph {
    private boolean isAlreadyPlaced = false;
    private boolean directed = true;
    public boolean isDirected() { return directed; }
    public void setDirected(boolean dir) { directed = dir; }

    private List<Edge> edges = new ArrayList<>();
    private List<Vertex> vertices = new ArrayList<>();

    public List<Edge> getEdges() { return edges; }
    public List<Vertex> getVertices() { return vertices; }

    public void setIfAlreadyPlaced(boolean status) { isAlreadyPlaced = status; }
    public boolean isAlreadyPlaced() { return isAlreadyPlaced; }

    public boolean addEdge(Edge e) {
        for(Edge edge: edges) {
            if (edge.equals(e)) {//check if edge is already in the list
                return false;
            }
        }
        e.setDirected(false);
        edges.add(e);
        return true;
    }
    public boolean addVertex(Vertex v) {
        for(Vertex vertex: vertices) {
            if(vertex.equals(v) || vertex == v) {
                return false;
            }
        }
        vertices.add(v);
        return true;
    }

    public void sort() {
        Collections.sort(edges);
        Collections.sort(vertices);
        vertices.forEach(vertex -> vertex.sort());
    }

    public void addVertices(String v1, String v2) {
        //get or create vertices
        Vertex a = getVertex(v1);
        Vertex b = getVertex(v2);
        if(a == null)
            a = new Vertex(v1);
        if(b == null)
            b = new Vertex(v2);
        //add to list (set)
        addVertex(a);
        addVertex(b);

        //create edge
        Edge e = getEdge(a + " -> " + b);
        Edge e2 = getEdge(b + " -> " + a);
        if(e == null)
            e = new Edge(a, b, a + " -> " + b);
        if(e2 == null)
            e2 = new Edge(b, a, b + " -> " + a);
        //add to list (set)
        addEdge(e);
        addEdge(e2);
        //set directed and add to adjacency list
        e.setDirected(true);
        a.addEdge(e);
        b.addEdge(e2);
    }

    public Vertex getVertex(String label) {
        for(Vertex v : vertices)
            if(v.getLabel().equals(label))
                return v;
        return null;
    }
    public Vertex getVertex(Object value) {
        for (Vertex v : vertices) {
            if (v.getValue() == value) {
                return v;
            }
        }
        return null;
    }
    public Edge getEdge(String label) {
        for(Edge e : edges)
            if(e.getLabel().equals(label))
                return e;
        return null;
    }
    public Edge getEdgeCouple(String label) {
        String[] other = label.split(" -> ");
        return getEdge(other[1] + " -> " + other[0]);
    }

    //---------------- REMOVAL ----------------
    public void removeEdge(Edge edge) {
        Vertex v1 = edge.getFrom();
        Vertex v2 = edge.getTo();
        Edge couple = getEdgeCouple(edge.getLabel());
        //remove edge connection and adjacency
        v1.removeEdge(edge);
        v1.removeAdjacent(v2);
        //remove edge connection and adjacency
        v2.removeEdge(couple);
        v2.removeAdjacent(v1);
        //remove edges from the list
        edges.remove(edge);
        edges.remove(couple);
        //remove any connections to the vertices
        edge.setTo(null);
        edge.setFrom(null);
        couple.setTo(null);
        couple.setFrom(null);
    }
    public void removeEdge(Object value) {
        Shape temp;
        for(Edge e : edges) {
            temp = e.getValue();
            if(temp.getValue() == value) {
                removeEdge(e);
                break;
            }
        }
    }
    public void removeVertex(Object value) {
        if(value == null) return;
        Vertex vertex = getVertex(value);
        //removed coupled edges pointing to vertex in both adjacency lists and graph
        List<Vertex> list = vertex.getAdjacencyList();
        String label;
        for(int i = list.size()-1; i >= 0; i--) {
            Vertex v = list.get(i);
            label = vertex + " -> " + v;
            Edge edge = getEdge(label);
            removeEdge(edge);
        }
        vertices.remove(vertex);
    }
}
