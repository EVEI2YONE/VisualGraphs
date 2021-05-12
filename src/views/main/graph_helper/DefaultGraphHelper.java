package views.main.graph_helper;

import models.Item;
import models.graph.*;
import models.shapes.*;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultGraphHelper extends GraphHelper{

    private static long seed = 3;
    private int nodeLimit = 6;
    private int edgeLimit = 15;
    private static Random random = new Random(seed);
    private static String charSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public char[] getCharSet(int nodes) {
        char[] set = new char[nodes];
        for(int i = 0; i < nodes; i++) {
            set[i] = charSet.charAt(i);
        }
        return set;
    }

    public DefaultGraphHelper() {}
    public DefaultGraphHelper(Graph g) {
        graph = g;
    }

    @Override
    public Graph generateGraph() {
        graph = new Graph();
        char[] charSet = getCharSet(nodeLimit);
        char v1, v2;
        int size = charSet.length;
        for(int i = 0; i < edgeLimit; i++) {
            v1 = charSet[random.nextInt(size)];
            do {
                v2 = charSet[random.nextInt(size)];
            } while (v1 == v2);
            graph.addVertices(v1 + "", v2 + "");
        }
        return graph;
    }

    @Override
    public void calculatePlacement() {
        if(graph == null)
            return;
        int //total diameter or height/width
            shapeWidth = 30,
            shapeHeight = 30;
        double vert = 0, hor = 0; //x = horizontal, y = vertical
        int widthLim = width-shapeWidth;
        int heightLim = height-shapeHeight;
        int size = graph.getVertices().size();
        int rowCount = width/shapeWidth; //testing purposes for aligning objects in a row

        int count;
        for(int i = 0; i < size; i++) {
            count = 0;
            Shape circle = new Circle(0, 0, shapeWidth, shapeHeight);
            graph.getVertices().get(i).setValue(circle);
            circle.setValue(graph.getVertices().get(i).getLabel());
            boolean test1, test2, test3;
            do {
                if(count == 20) {
                    widthLim *= 1.5;
                    heightLim *= 1.5;
                    count = 0;
                }
                hor = random.nextInt(widthLim) + shapeWidth/2;
                vert = random.nextInt(heightLim) + shapeHeight/2;
                circle.setX((int)hor);
                circle.setY((int)vert);
                //hor  = ((i % rowCount) * diam) + radius;
                //vert = ((i / rowCount) * diam) + radius;
                //TODO: INSTEAD OF CALCULATING OPTIMAL PLACEMENT, RANDOMLY PLACE (NON-OVERLAPPING)
                test1 = false;//intersects(circle); //once placed, check if line interects
                test2 = false;//intersects(graph.getVertices().get(i)); //after placed, check if edges intersect
                test3 = false;//overlaps(circle);
            } while(test1 || test2 || test3);
        }
        //sort Graph (adjacency) lists
        //TODO: THEN SHIFT THE CIRCLE SUCH THAT THEY ARE CLOSE TO A MINIMUM AVERAGE DISTANCE
    }

    @Override
    public void selfSort() {
        sortOverlap();
        minimizeIntersections();
    }
    public void sortOverlap() {
        List<Vertex> overlaps;
        do {
            overlaps = getOverlaps();
            for(Vertex v1 : overlaps) {
                for(Vertex v2 : overlaps) {
                    if(v1 == v2) continue;
                    //push v2
                    if(v1.getValue().overlaps(v2.getValue())) {
                        Circle c = (Circle) v2.getValue();
                        int offsetX = c.getWidth() * ((random.nextBoolean()) ? -1 : 1);
                        int offsetY = c.getHeight() * ((random.nextBoolean()) ? -1 : 1);
                        int x = c.getX();
                        int y = c.getY();
                        c.setX(x + offsetX);
                        c.setY(y + offsetY);
                    }

                }
                overlaps.remove(v1);
            }
        } while(overlaps.size() > 0);
    }

    public List<Vertex> getOverlaps() {
        List<Vertex> overlaps = new ArrayList<>();
        for (Vertex v1 : graph.getVertices()) {
            for (Vertex v2 : graph.getVertices()) {
                if (v1 == v2) continue;
                if (v1.getValue().overlaps(v2.getValue())) {
                    if (!overlaps.contains(v1))
                        overlaps.add(v1);
                    if (!overlaps.contains(v2))
                        overlaps.add(v2);
                }
            }
        }
        return overlaps;
    }

    public void minimizeIntersections() {

    }


    @Override
    public void injectGraphData() {

    }
}
