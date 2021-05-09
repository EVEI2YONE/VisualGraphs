package views.main.components;

import javafx.scene.canvas.Canvas;
import views.main.graph_helper.BSTGraphHelper;
import views.main.graph_helper.GraphHelper;

public class Component_BST extends AlgorithmComponent {
    public Component_BST(Canvas c) {
        super(c);
    }

    @Override
    public String getComponentName() {
        return "Binary Search Tree";
    }

    @Override
    public GraphHelper getGraphHelper() {
        return new BSTGraphHelper();
    }

    @Override
    public void setOnAction() {

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
