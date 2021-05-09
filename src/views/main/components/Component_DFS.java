package views.main.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import models.graph.Graph;
import views.main.graph_helper.DefaultGraphHelper;
import views.main.graph_helper.GraphHelper;

import java.util.List;

public class Component_DFS extends AlgorithmComponent {

    //Creates MenuItem and user can define what happens when user selects it
    public Component_DFS(Canvas c) {
        super(c);
    }
    @Override
    public void setOnAction() {

    }

    @Override
    public String getComponentName() {
        return "DFS";
    }

    @Override
    public GraphHelper getGraphHelper() {
        return new DefaultGraphHelper();
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
