package models;

import javafx.scene.paint.Color;

import java.io.*;
import java.util.Arrays;

public class FileParser {

    private static Graph graph;
    private static String directory = "C:/Users/azva_/IdeaProjects/VisualGraphs/src/resources/text";
    public static Graph parseFile(String filename) throws IOException{
        graph = new Graph();
        graph.setIfAlreadyPlaced(false);
        File file;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            file = new File(filename);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String[] ext = filename.trim().split(".*\\.");
            //String[] temp = filename.trim().split("");

            String line;
            String[] content;
            String current = "";
            int count = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(count++);
                boolean vertices = false;
                line = line.trim();
                content = line.split("\\s+");
                String temp = content[0];
                if(temp.contains(":")) {
                    vertices = true;
                    current = temp;
                }
                else{
                    vertices = false;
                }
                if(vertices) { //PARSE ADJACENCY LIST
                    content[0] = current.substring(0, current.length()-1); //strip ":"
                    current = content[0];
                    for (int i = 1; i < content.length; i++) {

                        graph.addVertices(current, content[i]); // "A -> B"
                    }
                }
                else { //ASSIGN POSITION TO CURRENT VERTEX
                    setValue(current, content);
                    graph.setIfAlreadyPlaced(true);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("File doesn't exist! in FILE PARSER");
            return null;
        }
        finally {
            if(fr != null)
                fr.close();
            if(br != null)
                br.close();
        }
        return graph;
    }
    private static void setValue(String current, String[] content) {
        Vertex v = graph.getVertex(current);
        int
            x = Integer.parseInt(content[0]),
            y = Integer.parseInt(content[1]),
            w = Integer.parseInt(content[2]),
            h = Integer.parseInt(content[3]);
        Shape circle = new Circle(x, y, w, h);
        v.setValue(circle);
    }

    public static void saveFile(Graph current) throws IOException {
        if(current == null) return;
        FileWriter bw = null;
        try {
            String filename = "testFile.txt";
            bw = new FileWriter("C:/Users/azva_/IdeaProjects/VisualGraphs/src/resources/text/" + filename);
            //go through all vertices and print its adjacency list
            for(Vertex v : current.getVertices()) {
                //write current vertex
                bw.write(v.toString() + ":");
                //write adjacency list of vertex
                for(Vertex adjacent : v.getAdjacencyList()) {
                    bw.write(" " + adjacent.toString());
                }
                bw.write("\n");
                int
                    x = v.getValue().getX(),
                    y = v.getValue().getY(),
                    w = v.getValue().getWidth(),
                    h = v.getValue().getHeight();
                bw.write(String.format("%d %d %d %d\n", x, y, w, h));
            }
        }catch(Exception ex) {
            System.out.println("Issue with saving file in FILE PARSER");
        }finally {
            if(bw != null)
                bw.close();
        }
    }
}
