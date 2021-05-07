package controllers;

import models.graph.Edge;
import models.graph.Graph;
import models.GraphAlgorithms;
import models.GraphAlgorithms.*;

import javafx.scene.paint.Color;
import models.graph.Vertex;

public class AlgorithmsController {
    private GraphAlgorithms algorithms = new GraphAlgorithms();

    public void setGraph(Graph g) {
        algorithms.setGraph(g);
    }

    //DEFAULT GRAPH SETTINGS
    OperationType operationType = OperationType.SEARCH;
    SearchType searchType = SearchType.BFS;

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
    public void setColors() {
        Color
            traversing = Color.rgb(78, 210, 187, .6),
            visited = Color.rgb(216, 13, 13, .7),
            edgeTraversal = Color.rgb(216, 99, 20, .9);
        algorithms.currentColor = traversing;
        algorithms.visitColor = visited;
        algorithms.currentEdgeColor = edgeTraversal;
    }
    private void clearColors() {
        for(Edge e : algorithms.getGraph().getEdges()) {
            if(e == null) continue;
            e.getFrom().getValue().setPrimaryFill(Color.WHITE);
            e.getValue().setPrimaryStroke(Color.BLACK);
        }
    }

    public void setCanvasController(CanvasController canvasController) {
        algorithms.setCanvasController(canvasController);
    }
}
