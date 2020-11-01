package models;

import javax.swing.*;
import java.util.ArrayList;


public class Graph extends JPanel {
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Vertex> vertices = new ArrayList<>();

    public ArrayList<Edge> getEdges() { return edges; }
    public ArrayList<Vertex> getVertices() { return vertices; }

    public boolean addEdge(Edge e) {
        for(Edge edge: edges)
            if(edge.equals(e)) {//check if edge is already in the list
                return false;
            }
        e.setDirected(false);
        edges.add(e);
        return true;
    }
    public boolean addVertex(Vertex v) {
        for(Vertex vertex: vertices)
            if(vertex.equals(v) || vertex == v)
                return false;
        vertices.add(v);
        return true;
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
    public Edge getEdge(String label) {
        for(Edge e : edges)
            if(e.getLabel().equals(label))
                return e;
        return null;
    }
}
