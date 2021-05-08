package views.main.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import models.graph.Graph;

import java.util.List;

public class Component_DFS extends AlgorithmComponent {

    //Creates MenuItem and user can define what happens when user selects it
    public Component_DFS(Canvas c) {
        super(c);
        menuItem.setText("DFS");
    }
    @Override
    public void setOnAction() {

    }

    //Build constructs the graph based default or custom GraphHelper object
    //If custom, then user has to setGraphHelper(GraphHelper custom)
    //  prior to calling this function
    @Override
    public void build() {
        graphHelper.initGraph(canvas);
        graph = graphHelper.getGraph();
        focusGraph();
    }

    @Override
    public boolean validateStructure() {
        return false;
    }

    @Override
    public boolean setupAlgorithm() {
        return false;
    }

    @Override
    public void runAlgorithm() {

    }
}
