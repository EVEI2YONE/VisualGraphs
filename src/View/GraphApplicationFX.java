package View;
import controllers.CanvasController;
import controllers.GraphController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class GraphApplicationFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    GraphController gc = new GraphController();
    String filename = "";
    int option = 0;
    int width = 750;
    int height = 600;
    double radius = 15;
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        HBox upper = new HBox();
        Button run = new Button("run");
        Button randomize = new Button("randomize");
        Button fileSelector = new Button("upload file");
        Button build = new Button("build");

        MenuButton menuButton = new MenuButton("direction");
        MenuItem undirected = new MenuItem("undirected");
        MenuItem directed = new MenuItem("directed");
        menuButton.getItems().addAll(undirected, directed);

        upper.getChildren().addAll(run, randomize, build, fileSelector, menuButton);
        upper.setMinWidth(stage.getWidth());
        upper.setStyle("-fx-background-color: #c7c6c6");

        FXMLLoader canvas_fxml = new FXMLLoader(getClass().getResource("../resources/fxml/canvas-graph.fxml"));
        Parent canvas_node = canvas_fxml.load();
        root.getChildren().addAll(upper, canvas_node);
        CanvasController.setDimension(width, height);

        //-------------------------------
        //DETERMINES OPTIONS AT WHEN 'RUN' IS SELECTED
        undirected.setOnAction(e -> {
            menuButton.setText("undirected");
            option = 0;
        });
        directed.setOnAction(e -> {
            menuButton.setText("directed");
            option = 1;
        });
        menuButton.setOnAction(e -> {
            if(option == 0) {

            }
            else {

            }
        });

        //FINDS FILE TO BUILD GRAPH
        fileSelector.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.showOpenDialog(stage);
            try {
                fc.setInitialDirectory(new File("C:/Users/azva_/IdeaProjects/VisualGraphs/src/resources/text"));
                File selectedFile = fc.showOpenDialog(stage);
                filename = selectedFile.getAbsolutePath();
            }catch(Exception ex) {
                System.out.println("error");
            }
        });
        //CONSTRUCTS GRAPH RANDOMLY
        randomize.setOnAction(e -> {
            gc.generateRandomGraph(10);
            System.out.println("randomizing graph");
        });
        //CONSTRUCTS GRAPH FROM FILE
        build.setOnAction(e -> {
            gc.readFile(filename);
            System.out.println("reading file: " + filename);
        });
        run.setOnAction(e -> {
            if(CanvasController.isBuilt())
                return;
            //INITALIZE GRAPH CALCULATIONS
            gc.setWidth((int)(width*.80));
            gc.setHeight((int)(height*.80));
            gc.setRadius(radius);

            //CALCULATES GRAPH PLACEMENT
            gc.init();
            System.out.println("initializing graph");
            gc.debug();
            //GET READY TO DRAW
            CanvasController.setGraphController(gc);
        });
        //------------------------------

        stage.setScene(new Scene(root));
        stage.setWidth(width);
        stage.setHeight(height);
        stage.show();
    }
}
