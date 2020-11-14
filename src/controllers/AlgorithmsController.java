package controllers;

import models.graph.Edge;
import models.graph.Graph;
import models.graph.GraphAlgorithms;
import models.graph.GraphAlgorithms.*;

import javafx.scene.paint.Color;
import models.graph.Vertex;

public class AlgorithmsController {
    private GraphAlgorithms algorithms;

    public AlgorithmsController(Graph g) {
        algorithms = new GraphAlgorithms(g);
        setColors(Color.GREEN, Color.RED, Color.DARKCYAN);
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                begin();
            }
        });
        thread.start();
    }

    private void begin() {
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
    public void setColors(Color traversing, Color visited, Color edgeTraversal) {
        algorithms.setCurrentColor(traversing);
        algorithms.setVisitColor(visited);
        algorithms.setEdgeColor(edgeTraversal);
    }
    private void clearColors() {
        for(Edge e : algorithms.getGraph().getEdges()) {
            if(e == null) continue;
            e.getFrom().getValue().setCurrFill(Color.WHITE);
            e.setColor(Color.BLACK);
        }
    }
}
