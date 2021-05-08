package views.main.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import models.GraphAlgorithms.*;
import models.graph.Graph;
import views.main.controller.CanvasControls;
import views.main.graph_helper.DefaultGraphHelper;
import views.main.graph_helper.GraphHelper;

public abstract class AlgorithmComponent implements VisualAlgorithmInterface {
    private static GraphType graphType = GraphType.UNDIRECTED;
    private static AlgorithmComponent selected = null;

    //canvasControls is responsible for peripheral inputs (keyboard + mouse) on graph
    protected static CanvasControls canvasControls = null;
    //graphHelper is responsible for generating custom graph structures
    protected GraphHelper graphHelper = new DefaultGraphHelper();

    protected Canvas canvas = null; // canvas will always be created first
    protected Graph graph = null; // this is generate when user interacts with UI components
    //ColorOptions options;
    protected MenuItem menuItem = new MenuItem(); // this is custom based on developer's implementation


    public void setText(String text) {
        menuItem.setText(text);
    }

    public AlgorithmComponent(Canvas c) {
        canvas = c;
        if(canvasControls == null)
            canvasControls = new CanvasControls(canvas);
        initMenuItem();
    }

    private void initMenuItem() {
        menuItem.setOnAction(e -> {
            //keep track of selected menu item to prevent searching list
            setSelectedMenuItem(this);
            setOnAction();
            focusGraph();
        });
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
    public void setGraphHelper(GraphHelper gh) { graphHelper = gh; }

    public static void setSelectedMenuItem(AlgorithmComponent component) {
        selected = component;
    }
    public static AlgorithmComponent getSelectedMenuItem() { return selected; }

    public void setGraphType(GraphType type) {
        graphType = type;
    }
    public GraphType getGraphType() { return graphType; }

    public void focusGraph() {
        canvasControls.setGraph(graph);
        canvasControls.collectGraphShapes();
        canvasControls.displayGraph();
    }
}
