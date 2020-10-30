package Model;

import View.GraphPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class GraphAlgorithms {
    public enum OperationType { SEARCH, TRANSPOSE, TRANSITIVE }
    public enum SearchType { DFS, BFS, DIJKSTRA }
    public enum GraphType { UNDIRECTED, DIRECTED }

    private boolean directed;
    private Color visitColor;
    private Color currentColor;
    private Vertex starting;
    private Vertex ending;

    Graph graph = null;
    public GraphAlgorithms(Graph g) {
        graph = g;
    }

    //GETTERS AND SETTERS
    //--------------------------------
    public boolean isDirected() {
        return directed;
    }
    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public Color getVisitColor() {
        return visitColor;
    }
    public void setVisitColor(Color visitColor) {
        this.visitColor = visitColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }
    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public Graph getGraph() {
        return graph;
    }
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setStarting(Vertex v) { starting = v; }
    public void setEnding(Vertex v) { ending = v; }
    //--------------------------------

    public void TransitiveClosure() {}
    public void Transpose() {}
    public void DijkstraSearch() {}
    public void BFSSearch() {
        //create queue
        //get starting vertex
        //enqueue starting vertex
        //loop while queue isn't empty
        //get dequeued vertex
        //set to visited
        //get String adjacency list of vertex
        //parse adjacency list
        //get vertex with findVertex(String label)
        //if vertex is NOT visited, enqueue
    }
    public void DFSSearch() {
        //Create stack
        Stack<Vertex> stack = new Stack<>();
        //push first viable element
        Vertex v = getFirst();
        if(v == null) return;
            stack.push(v);
        Edge next;
        //begin DFS
        while(!stack.isEmpty()) {
            //System.out.println(v); //<- PRINTS OUT DFS TRAVERSAL
            //mark current as visited
            v.setVisited(true);
            update(v, currentColor);
            //find next vertex in the graph on first search
            next = getNext(v);
            if(v != ending && next != null) {
                update(v, visitColor);
                //move to next vertex
                v = next.getTo();
                stack.push(v);
                continue;
            }
            //path exhausted
            v = stack.pop();
        }
    }

    public Edge getNext(Vertex v) {
        for(Edge e : (ArrayList<Edge>) v.getAdjancencyEList())
            if(!e.getTo().isVisited()) {
                //settings are to return (un)directed edge
                if (!directed)
                    return e;
                else if (e.isDirected())
                    return e;
            }
        return null;
    }
    public Vertex getFirst() {
        if(starting != null)
            return starting;
        //go through all edges and find first source node/vertex
        for(Edge e : graph.getEdges()) {
            Edge edge = getNext(e.getFrom());
            if(edge != null)
                return edge.getFrom();
        }
        return null;
    }
    public void resetGraph() {
        for(Vertex v : graph.getVertices())
            v.setVisited(false);
    }

    //CALLS STATIC CLASS IN GRAPH PANEL TO UPDATE GRAPH'S COLORS - VERY VERSATILE
    public void update(Vertex v, Color c) {
        GraphPanel.update(v, c);
    }
    public void printAdjacencyMatrix() {

    }
}
