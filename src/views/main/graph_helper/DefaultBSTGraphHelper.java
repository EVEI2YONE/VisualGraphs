package views.main.graph_helper;

import models.graph.Graph;
import models.graph.Vertex;
import models.shapes.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultBSTGraphHelper extends GraphHelper {
    Integer[] arr;
    Random random = new Random();
    int depth = 4;
    public static int boxwidth = 1, boxheight = 1;
    private int count = 0;

    private void populateArr() {
        arr = new Integer[getDepth(depth)];
        fillSpaces(0);
    }
    private int getDepth(int d) {
        int val = (int)(Math.round(Math.pow(2, d) - 1));
        System.out.println(val);
        return val;
    }
    private void fillSpaces(int index) {
        if(index >= arr.length) return;
        arr[index] = ++count;
        int leftChild = (index+1) * 2 - 1;
        int rightChild = (index+1) * 2;
        //if(random.nextBoolean())
            fillSpaces(leftChild);
        //if(random.nextBoolean())
            fillSpaces(rightChild);
    }
    private void swap(int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    private void sortArr() {

    }
    private boolean exists(int index) {
        //within bounds and element exists
        return (index < arr.length && arr[index] != null);
    }
    private void parseBST(int index) {
        //out of bounds or non-populated index
        if(!exists(index)) {
            return;
        }
        int leftChild = (index+1) * 2 - 1;
        int rightChild = (index+1) * 2;
        if(exists(leftChild)) {
            graph.addVertices(arr[index].toString(), arr[leftChild].toString());
            parseBST(leftChild);
        }
        if(exists(rightChild)) {
            graph.addVertices(arr[index].toString(), arr[rightChild].toString());
            parseBST(rightChild);
        }
    }
    private String contents() {
        String output = "";
        for(Integer val : arr) {
            if(val == null)
                output += "0 ";
            else
                output += val.toString() + " ";
        }
        return output;
    }
    public Vertex getVertex(int index) {
        for(Vertex v : graph.getVertices()) {
            if(v.getLabel().equals(arr[index].toString()))
                return v;
        }
        return null;
    }
    public int getCurrDepth(int index) {
        return (int) (Math.log(index+1) / Math.log(2.0) + 1);
    }
    //find the relative index in a depth row of a BST
    public int getIth(int index, int currDepth) {
        int nodesPerDepth = getNodesPerDepth(currDepth);
        return index - nodesPerDepth + 1;
    }

    @Override
    public Graph generateGraph() {
        graph = new Graph();
        //generates random array/bst structure
        populateArr();
        //sorts array/bst in order to test logic
        sortArr();
        //make the connections by parsing the sorted arr
        parseBST(0);
        return graph;
    }

    @Override
    public void calculatePlacement() {
        int shapeWidth = 30, shapeHeight = 30;
        int spacing = 5;
        int leafnodes = getNodesPerDepth(depth);
        boxwidth = (leafnodes - 1) * spacing + (shapeWidth * 2) * leafnodes - shapeWidth;
        boxheight = (depth) * spacing + shapeHeight * depth;

        int topleftx = (width - boxwidth)/2;
        int toplefty = (height - boxheight)/2;

        /*
        When parsing through the array, the vertices are not in corresponding order to indices.
        Find vertex in the graph by searching for the label of the BST node within the graph.
        Integer[] arr can have null values
         */
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == null) continue;
            Vertex v = graph.getVertex(arr[i].toString());//getVertex(i);
            if(v == null) continue;
            Circle c = new Circle(0, 0, shapeWidth, shapeHeight);
            v.setValue(c);
            c.setValue(v.getLabel());
            int currDepth = getCurrDepth(i);
            double sectionsX = getNodesPerDepth(currDepth) * 2;
            double ith = getIth(i, currDepth);
            int xOff = (int)(boxwidth / sectionsX * (ith*2+1));

            int yOff = (int)((double)(boxheight / depth) * currDepth) - (shapeHeight/2);
            c.setX(topleftx + xOff);
            c.setY(toplefty + yOff);
        }

    }

    public int getNodesPerDepth(int currDepth) {
        return (int)Math.pow(2, currDepth-1);
    }

    @Override
    public void selfSort() {

    }

    @Override
    public void injectGraphData() {

    }
}
