package controllers;

import models.Circle;
import models.Graph;
import models.GraphAlgorithms;
import models.GraphAlgorithms.*;
import models.Vertex;

import javafx.scene.paint.Color;

public class AlgorithmsController {
    private GraphAlgorithms algorithms;

    public AlgorithmsController(Graph g) {
        algorithms = new GraphAlgorithms(g);
        setColors(Color.GREEN, Color.RED);
    }
    public AlgorithmsController() {}

    public void setGraph(Graph g) {
        algorithms = new GraphAlgorithms(g);
    }
    public Graph getGraph() {
        if(algorithms == null)
            return null;
        return algorithms.getGraph();
    }

    //DEFAULT GRAPH SETTINGS
    OperationType operationType = OperationType.SEARCH;
    SearchType searchType = SearchType.DFS;

    public void setUpGraph(OperationType operation, SearchType search, GraphType graphType) {
        //user helper method for start and end vertices
            operationType = operation;
            switch(graphType) {
                case UNDIRECTED: algorithms.setDirected(false); break;
                case DIRECTED: algorithms.setDirected(true); break;
            }
            searchType = search;
    }
    public void setStarting(Vertex v) {
        algorithms.setStarting(v);
    }
    public void setEnding(Vertex v) {
        algorithms.setEnding(v);
    }
    public void startOperation() {
        algorithms.resetGraph();
        clearColors();
        switch(operationType){
            case SEARCH:
                switch(searchType) {
                case DFS:
                    algorithms.DFSSearch();
                    break;
                case BFS:
                    algorithms.BFSSearch();
                    break;
                case DIJKSTRA:
                    algorithms.DijkstraSearch();
            }
            case TRANSPOSE: algorithms.Transpose(); break;
            case TRANSITIVE:algorithms.TransitiveClosure(); break;
        }

    }
    public void setColors(Color traversing, Color visited) {
        algorithms.setCurrentColor(traversing);
        algorithms.setVisitColor(visited);
    }
    public void clearColors() {
        for(Vertex v : algorithms.getGraph().getVertices())
            ((Circle)v.getValue()).setColor(Color.BLACK);
    }
}
