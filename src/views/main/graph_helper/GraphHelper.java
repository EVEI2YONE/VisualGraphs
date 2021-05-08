package views.main.graph_helper;

import javafx.scene.canvas.Canvas;
import models.*;
import models.graph.Edge;
import models.graph.Graph;
import models.graph.MyMath;
import models.graph.Vertex;
import models.shapes.Arrow;
import models.shapes.Circle;
import models.shapes.Line;
import models.shapes.Shape;

import java.util.*;
import java.util.stream.Collectors;

public abstract class GraphHelper implements GraphBuilder, GraphCalculations, GraphDataInjector {
    protected Graph graph = null;
    protected int width, height;

    public Graph getGraph() { return graph; }
    public void setGraph(Graph g) { graph = g; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    private void setDimensions(Canvas canvas) {
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();
    }

    public void initGraph(Canvas canvas) {
        setDimensions(canvas);
        graph = generateGraph();
        calculatePlacement();
        selfSort();
        injectEdgeShape();
        updateEdges();
    }

    //helper functions
    public void updateEdges() {
        for(Edge e : graph.getEdges()) {
            //if(e.getFrom().getValue() != null && e.getTo().getValue() != null)
            updateEdge(e);
        }
    }
    public void updateEdges(Vertex vertex) {
        if(vertex == null) return;
        for(Edge e : (ArrayList<Edge>)vertex.getAdjacencyEList()) {
            //if(e.getFrom().getValue() != null && e.getTo().getValue() != null)
            updateEdge(e);
        }
    }
    //main function
    public void updateEdge(Edge e) {
        Shape start = e.getFrom().getValue();
        Shape end = e.getTo().getValue();
        if(start == null || end == null)
            return;
        double
                hor = end.getX() - start.getX(),
                vert = end.getY() - start.getY(),
                distance = Math.sqrt(hor * hor + vert * vert),
                u_hor = hor / distance,
                u_vert = vert / distance,
                horShift = (int) (u_hor * start.getWidth()/2.0),
                vertShift = (int) (u_vert * start.getHeight()/2.0);
        e.setXStart(start.getX() + horShift);
        e.setYStart(start.getY() + vertShift);

        horShift = (int) (u_hor * end.getWidth()/2.0);
        vertShift = (int) (u_vert * end.getHeight()/2.0);
        e.setXEnd(end.getX() - horShift);
        e.setYEnd(end.getY() - vertShift);
    }
    public void updateNewEdge(Edge newEdge, int x, int y) {
        Shape start = newEdge.getFrom().getValue();
        if(start == null) return;
        double
                hor = x - start.getX(),
                vert = y - start.getY(),
                distance = Math.sqrt(hor*hor + vert*vert),
                u_hor = hor/distance,
                u_vert = vert/distance,
                horShift = (int) (u_hor * start.getWidth()/2.0),
                vertShift = (int) (u_vert * start.getHeight()/2.0);
        newEdge.setXStart(start.getX() + horShift);
        newEdge.setYStart(start.getY() + vertShift);

        newEdge.setXEnd(x);
        newEdge.setYEnd(y);
    }

    public boolean overlaps(Shape shape){
        for(int i = 0; i < graph.getVertices().size(); i++) {
            Shape temp = graph.getVertices().get(i).getValue();
            if(temp == null || temp == shape) continue;
            //if(MyMath.overlappingCircles(circle, temp))
            if(shape.distanceFromBounds(temp) < 0)
                return true;
        }
        return false;
    }
}
