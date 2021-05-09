package views.main.graph_helper;

public interface GraphCalculations {
    /*
    STEP 2
        Calculate placement of nodes within the graph as where to be displayed on canvas

        Graph structure should be created at this point
     */
    public void calculatePlacement();
    /*
    STEP 3 (optional)
        Based on initial placement of graph nodes, adjust placements to however user implements
     */
    public void selfSort();
}
