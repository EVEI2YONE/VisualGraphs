package dbBuilder;

import models.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DBGrammarParser {
    private static Graph graph;
    public static Graph parseDB(String filename) throws IOException {
        graph = new Graph();
        File file;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            file = new File(filename);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String[] ext = filename.trim().split(".*\\.");

            String line;
            String[] content;
            String current = "";
            while ((line = br.readLine()) != null) {
                if(line.equals("") || line.equals("\n")) continue;
                boolean vertices = false;
                line = line.trim();
                content = line.split("\\s+");
                String temp = content[0];
                try {
                    if (temp.contains("Table")) {
                        parseTable(line, br);
                    }
                }catch(Exception ex) {
                    System.out.println("Error reading Table: " + line);
                }

//                if(temp.contains("Table")) {
//                    vertices = true;
//                    current = temp;
//                }
//                else{
//                    vertices = false;
//                }
//                if(vertices) { //PARSE ADJACENCY LIST
//                    content[0] = current.substring(0, current.length()-1); //strip ":"
//                    current = content[0];
//                    for (int i = 1; i < content.length; i++) {
//
//                        graph.addVertices(current, content[i]); // "A -> B"
//                    }
//                }
//                else { //ASSIGN POSITION TO CURRENT VERTEX
//                    //setValue(current, content);
//                    graph.setIfAlreadyPlaced(true);
//                }
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

    private static void parseTable(String header, BufferedReader br) {

    }

    private static void parseContent() {

    }

    private static void parseName() {

    }
}


/*
    TABLES -> TABLE NAME LCURLY CONTENT RCURLY SPACING TABLES
    TABLE -> Table
    LCURLY -> '{'
    RCURLY -> '}'
    SPACING -> ' ' | '\n'
    CONTENT -> (NAME TYPE | NAME TYPE PK) CONTENT
    NAME -> (A-Z | a-z | _ | $ | EPSILON) VAR
    TYPE -> 'int' | 'varchar'
    PK -> 'pk'
    EPSILON -> ''
 */