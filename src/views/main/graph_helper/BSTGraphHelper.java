package views.main.graph_helper;

import models.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BSTGraphHelper extends GraphHelper {
    int[] arr = new int[20];
    Random random = new Random(3);

    private void populateArr() {
        for(int i = 0 ; i < arr.length; i++)
            arr[i] = (i+1);
        for(int i = 0; i < arr.length; i++) {
            swap(i, random.nextInt(arr.length));
        }
    }

    private void swap(int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void sortArr() {

    }

    private void parseBST() {

    }

    @Override
    public Graph generateGraph() {
        //generates random array/bst structure
        populateArr();
        System.out.println(arr.toString());
        //sorts array/bst in order to test logic
        sortArr();
        //make the connections by parsing the sorted arr
        graph = new Graph();
        //parse array/bst struct and create graph structure
        parseBST();
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
