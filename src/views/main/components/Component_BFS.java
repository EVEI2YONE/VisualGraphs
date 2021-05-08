package views.main.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import models.graph.Graph;

import java.util.List;

public class Component_BFS extends AlgorithmComponent {

    public Component_BFS(Canvas c) {
        super(c);
        menuItem.setText("BFS");
    }

    @Override
    public void setOnAction() {

    }

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
