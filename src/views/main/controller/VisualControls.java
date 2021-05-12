package views.main.controller;

import javafx.scene.paint.Color;
import models.graph.Vertex;
import models.graph.Edge;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class VisualControls {
    Dictionary<String, Object> dict = new Hashtable<>();

    public void focusVertex(Vertex vertex) {

    }

    public void focusEdge(Edge edge, String edgeStroke) {
        Double stroke = (Double) dict.get(edgeStroke);
        edge.getValue().setStrokeWeight("temp", stroke);
    }

    public void setFill(String key, Color val) {
        dict.put(key, val);
    }

    public void setStroke(String key, Double val) {
        dict.put(key, val);
    }
}
