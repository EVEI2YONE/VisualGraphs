package Controller;

import Model.Circle;
import Model.Graph;
import Model.GraphAlgorithms;
import Model.GraphAlgorithms.*;
import Model.Vertex;

import java.awt.*;

public class AlgorithmsController {
    private static GraphAlgorithms algorithms;

    public AlgorithmsController(Graph g) {
        algorithms = new GraphAlgorithms(g);
        setColors(Color.GREEN, Color.RED);
    }
    public void setGraph(Graph g) { algorithms = new GraphAlgorithms(g); }

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
