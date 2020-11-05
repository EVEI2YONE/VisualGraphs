package models;

import java.io.BufferedReader;
import java.util.Arrays;

public class FileParser {

    private static String line, v[];
    private static Graph graph;
    private static String charSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static Graph txtParser(BufferedReader br) {
        graph = new Graph();
        String line;
        String[] v;
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                v = line.split("\\s+");
                graph.addVertices(v[0], v[v.length - 1]); // "A -> B"
            }
        }catch(Exception ex) {
            return null;
        }
        return graph;
    }

    public static Graph customParser(BufferedReader br) {
        graph = new Graph();
        String[] v = new String[2],
            xPoints,
            yPoints;
        int groups,
            count;
        int len = charSet.length(),
            pos = 0;
        //Circle[] circles;
        double
            x, y, r = 10;
        try {
            groups = Integer.parseInt(br.readLine());
            for(int i = 0; i < groups; i++) {
                count = Integer.parseInt(br.readLine());
                for(int j = 0; j < count; j++) {
                    xPoints = br.readLine().trim().split("\\s+");
                    yPoints = br.readLine().trim().split("\\s+");
                    //circles = new Circle[xPoints.length];
                    for(int k = 0; k < xPoints.length; k++) {
                        x = Double.parseDouble(xPoints[k]);
                        y = Double.parseDouble(yPoints[k]);
                        Circle circle = new Circle(x, y, r);
                        v[0] = charSet.substring((pos)%len, (++pos)%len);
                        v[1] = charSet.substring((pos)%len, (++pos)%len);
                        Graph.setEdgeValue(null);
                        Graph.setVertexValue(circle);
                        graph.addVertices(v[0], v[v.length - 1]); // "A -> B"
                        System.out.println(v[0] + " -> " + v[v.length-1]);
                        System.out.println(Arrays.toString(xPoints));
                        System.out.println(Arrays.toString(yPoints));
                        System.out.println("----------------------");
                    }

                }
            }
        }
        catch(Exception ex) {
            System.out.println("ERROR READING FILE IN FILE PARSER");
            return null;
        }
        return graph;
    }
}
