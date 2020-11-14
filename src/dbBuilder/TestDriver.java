package dbBuilder;

import models.Graph;

import java.io.IOException;

public class TestDriver {
    public static void main(String[] args) throws IOException {
        Graph graph = DBGrammarParser.parseDB("C:/Users/azva_/IdeaProjects/VisualGraphs/src/resources/text/dbTest.txt");
        graph.sort();
        graph.debug();
        DBGrammarParser.testMethod();
    }
}
