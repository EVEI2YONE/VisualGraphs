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
        int
                x, y,
                shapeWidth,
                shapeHeight;

        List<Vertex> degrees = getVertices()
                .stream()
                .sorted(Comparator.comparing(Vertex::getDegree))
                .collect(Collectors.toList());
        //parse each Circle/Vertex in the graph
        for(Vertex v : degrees) {
            Circle current = (Circle) v.getValue();
            ArrayList<Vertex> list = (ArrayList<Vertex>)v.getAdjacencyList();
            //skip nodes w/ no edge
            if(list.size() == 0)
                continue;
                //sort nodes with only 1 edge
            else if(list.size() == 1) {
                //TODO: REFACTOR TO CONSIDER ANY SHAPE BEING SELF-SORTED
                Circle end = (Circle) (list.get(0).getValue());
                x = end.getX();
                y = end.getY();
                shapeWidth = (int) end.getWidth();
                shapeHeight = (int) end.getHeight();
                double[] uvect = MyMath.getUnitVector(x, y, shapeWidth/2.0, shapeHeight/2.0);
                do {
                    x += (int)(uvect[0] * shapeWidth);
                    y += (int)(uvect[1] * shapeHeight);
                    current.setX(x);
                    current.setY(y);
                }while(overlaps(current));
            }
            //sort/replace from lower to higher degrees
            else {
                Vertex previous = null;

            }
        }
    }

    @Override
    public void injectEdgeShape() {
        for(Edge e : graph.getEdges())
            if(e.isDirected())
                e.setValue(new Arrow(0, 0, 0, 0));
            else
                e.setValue(new Line(0, 0, 0, 0));
    }

    //View <-> Controller data interaction (for PaintComponent)
    public List<Vertex> getVertices() {
        if(graph == null)
            return null;
        return graph.getVertices();
    }
}
