package views.main.components;

import controllers.CanvasController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import models.GraphAlgorithms;
import models.GraphAlgorithms.*;
import models.graph.Edge;
import models.graph.Graph;
import models.graph.Vertex;
import models.shapes.Arrow;
import models.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

public abstract class AlgorithmComponent implements VisualAlgorithmInterface {
    private static GraphType graphType = GraphType.UNDIRECTED;
    private static AlgorithmComponent selected = null;

    //canvasControls is responsible for peripheral inputs (keyboard + mouse) on graph
    protected static CanvasControls canvasControls = null;
    //graphHelper is responsible for generating custom graph structures
    protected GraphHelper graphHelper = new GraphHelper();

    protected Canvas canvas = null; // canvas will always be created first
    protected Graph graph = null; // this is generate when user interacts with UI components
    //ColorOptions options;
    protected MenuItem menuItem = null; // this is custom based on developer's implementation

    public AlgorithmComponent(Canvas c) {
        canvas = c;
        if(canvasControls == null)
            canvasControls = new CanvasControls(canvas);
    }

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
