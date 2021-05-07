package models;

import controllers.CanvasController;
import javafx.scene.paint.Color;
import models.graph.Edge;
import models.graph.Graph;
import models.graph.Vertex;

import java.util.*;

public class GraphAlgorithms {
    private CanvasController canvasController;
    public void setCanvasController(CanvasController cc) { canvasController = cc; }

    public enum OperationType { SEARCH, TRANSPOSE, TRANSITIVE }
    public enum SearchType { DFS, BFS, DIJKSTRA, BSIO }
    public enum GraphType { UNDIRECTED, DIRECTED }

    private boolean directed;
    public Color visitColor;
    public Color currentColor;
    public Color currentEdgeColor;
    private Vertex starting;
    private Vertex ending;
    private long rate = 800;

    Graph graph;

    //GETTERS AND SETTERS
    //--------------------------------
    public boolean isDirected() {
        return directed;
    }
    public void setDirected(boolean directed) {
        this.directed = directed;
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

    public void BinarySearchInOrder() {

    }

    Color queueing = Color.rgb(78, 210, 187, .6);
    //TODO: TURN QUEUE VERTICES INTO DIFFERENT COLOR OR STROKE WEIGHT
    public void BFSSearch() {
        //create queue
        Queue<Vertex> queue = new LinkedList<>();
        //get starting vertex
        v = getFirst();
        //enqueue starting vertex
        if(v == null)
            return;
        queue.add(v);
        Edge next;
        //loop while queue isn't empty
        while(!queue.isEmpty()) {
            //get dequeued vertex
            v = queue.remove();
            //set to visited
            v.setVisited(true);
            focusVertex(v, true);
            updateVertex(v, currentColor);
            //get adjacency list of edges to filter later when graph is undirected
            List<Edge> adj = v.getAdjacencyEList();
            //enqueue adjacency list
            bfsQueueNext(queue, adj);
            updateVertex(v, visitColor);
            focusVertex(v, false);
        }
    }

    private void bfsQueueNext(Queue<Vertex> queue, List<Edge> adj) {
        for(Edge e : adj) {
            Vertex neighbor = e.getTo();
            //neighbor is useless -_-
            if(neighbor.isVisited() || queue.contains(neighbor)) continue;

            boolean added = false;
            focusVertex(neighbor, true);
            updateEdge(e, currentEdgeColor);
            if(!graph.isDirected()) {
                added = queue.add(neighbor);
            }//directed graph && directed edge
            else if(e.isDirected()) {
                added = queue.add(neighbor);
            }
            //if(added)
            updateVertex(neighbor, queueing);
            focusVertex(neighbor, false);
            updateEdge(e, Color.RED);
        }
    }

    /*
        TODO: DFS SEARCH
            USE ANIMATION TO INDICATE STATUS OF NODE TRAVERSAL
            USE ANIMATION TO INDICATE END OF TRAVERSAL
            COLOR EDGE PRIOR TO POPPING STACK
     */
    public void DFSSearch() {
        //Create stack
        Stack<Vertex> stack = new Stack<>();
        //push first viable element
        v = getFirst();
        if(v == null)
            return;
        stack.push(v);
        Edge next;
        //begin DFS
        while(!stack.isEmpty()) {
            //System.out.println("at: " + v); //<- PRINTS OUT DFS TRAVERSAL
            v = stack.peek();
            //mark current as visited
            v.setVisited(true);
            updateVertex(v, currentColor);
            //find next vertex in the graph on first search
            next = getNext(v);
            if(v != ending && next != null) {
                updateVertex(v, visitColor);
                //move to next vertex
                v = next.getTo();
                stack.push(v);
            }
            else {
                //path exhausted
                v = stack.pop();
                //System.out.println("popped: " + v);
            }
        }
    }

    public Edge getNext(Vertex v) {
        for(Edge e : (ArrayList<Edge>) v.getAdjacencyEList()) {
            updateEdge(e, currentEdgeColor);
            if (!e.getTo().isVisited()) {
                //settings are to return (un)directed edge
                if (!directed) {
                    return e;
                }
                else if (e.isDirected()) {
                    return e;
                }
                updateEdge(e, Color.RED);
            }
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
        for(Vertex v : graph.getVertices()) {
            v.setVisited(false);
        }
    }

    private Vertex v;
    private Color c;

    public void setRate(long r) {
        //200 <= rate <= 3000
        if(r < 200)
            rate = 200;
        else if(r > 3000)
            rate = 3000;
        else
            rate = r;
    }
    //CALLS STATIC CLASS IN GRAPH PANEL TO UPDATE GRAPH'S COLORS - VERY VERSATILE
    public void focusVertex(Vertex v, boolean focus) {
        if(focus) {
            v.getValue().setStrokeWeight(2.0);
        }
        else {
            v.getValue().setStrokeWeight(1.0);
        }
    }

    public void updateVertex(Vertex v, Color c) {
        try {
            v.getValue().setPrimaryFill(c);
        Thread.sleep(rate);
        }catch(Exception e) { }
        canvasController.repaint();
    }
    public void updateEdge(Edge edge, Color c) {
        try {
            Thread.sleep(rate);
        }catch(Exception e) { }
        edge.getValue().setPrimaryStroke(c);
        Edge e2 = graph.getEdgeCouple(edge.toString());
        e2.getValue().setPrimaryStroke(c);
        canvasController.repaint();
    }
    public void printAdjacencyMatrix() {

    }
}
