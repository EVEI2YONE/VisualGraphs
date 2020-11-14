package views.side;

import dbBuilder.DBMLGrammarParser;
import javafx.application.Application;
import javafx.stage.Stage;
import models.graph.Graph;

public class DBMLGraphApplicationFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {

    }

    public static void main(String[] args) {
        //launch(args);
        Graph graph = DBMLGrammarParser.parseDB("C:\\Users\\azva_\\IdeaProjects\\VisualGraphs\\src\\resources\\text\\dbTest.txt");
        graph.debug();
        int i = 0;
    }
}