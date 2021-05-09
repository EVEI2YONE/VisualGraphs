package views.main.graph_helper;

import models.graph.Graph;

public interface GraphBuilder {
    /*
    STEP 1
        Graph generates connections between Vertices in this format "%s -> %s".

        Graph graph = new Graph(); // new graph is created before the function is called
        graph.addVertices("a", "b");

        Note: Undirected connection is also created and accessible if %s matches for b -> a connection
     */
    public Graph generateGraph();

}
