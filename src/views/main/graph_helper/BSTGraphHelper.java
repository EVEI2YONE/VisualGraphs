package views.main.graph_helper;

import models.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BSTGraphHelper extends GraphHelper {
    Integer[] arr;
    Random random = new Random(0);
    int depth = 4;

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
        arr[index] = random.nextInt(200) + 1;
        int leftChild = (index+1) * 2 - 1;
        int rightChild = (index+1) * 2;
        if(random.nextBoolean())
            fillSpaces(leftChild);
        if(random.nextBoolean())
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

    @Override
    public Graph generateGraph() {
        graph = new Graph();
        //generates random array/bst structure
        populateArr();
        System.out.println(contents());
        //sorts array/bst in order to test logic
        sortArr();
        //make the connections by parsing the sorted arr
        parseBST(0);
        return graph;
    }

    @Override
    public void calculatePlacement() {

    }

    @Override
    public void selfSort() {

    }

    @Override
    public void injectGraphData() {

    }
}
