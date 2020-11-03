package View;
import controllers.AlgorithmsController;
import controllers.CanvasController;
import controllers.GraphController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Graph;
import models.GraphAlgorithms;
import javafx.scene.paint.Color;

import java.io.File;

public class GraphApplicationFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    GraphController gc = new GraphController();
    AlgorithmsController ac = new AlgorithmsController();
    String filename = "";
    int width = 750;
    int height = 600;
    double radius = 15;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setWidth(width);
        stage.setHeight(height);
        VBox root = new VBox();
        HBox upper = new HBox();

        Button randomize = new Button("randomize");
        nodes = new TextField("nodes");
        edges = new TextField("edges");
        Button fileSelector = new Button("upload file");

        MenuButton menuButton = new MenuButton("direction");
        MenuItem undirected = new MenuItem("undirected");
        MenuItem directed = new MenuItem("directed");
        menuButton.getItems().addAll(undirected, directed);

        Button operation = new Button("Run algorithm");

        upper.getChildren().addAll(randomize, nodes, edges, fileSelector, menuButton, operation);
        upper.setMinWidth(stage.getWidth());
        upper.setStyle("-fx-background-color: #c7c6c6");

        FXMLLoader canvas_fxml = new FXMLLoader(getClass().getResource("../resources/fxml/canvas-graph.fxml"));
        Parent canvas_node = canvas_fxml.load();
        canvas_node.setStyle("-fx-background-color: #f0e6e6");
        root.getChildren().addAll(upper, canvas_node);

        //CONTROL NODE SETUP
        setSize(nodes, edges);
        setTextFields(nodes, edges);
        setMenu(undirected, false);
        setMenu(directed, true);
        setFileSelector(fileSelector);
        setBuildType(randomize, true);
        startOperation(operation);
        //---------------------------------


        stage.setScene(new Scene(root));
        stage.show();
    }

    TextField nodes, edges;
    public void startOperation(Button operation) {
        operation.setOnAction(e -> {
            Graph g = gc.getGraph();
            if(g == null)
                return;
            if(ac.getGraph() == null || g != ac.getGraph())
                ac.setGraph(g);
            ac.setUpGraph(GraphAlgorithms.OperationType.SEARCH, GraphAlgorithms.SearchType.DFS, GraphAlgorithms.GraphType.DIRECTED);
            ac.setColors(Color.CYAN, Color.RED, Color.ORANGE);
            ac.startOperation();
        });
    }

    public int getCount(String type) {
        int count;
        switch(type) {
            case "nodes":
                try {
                    count = Integer.parseInt(nodes.getText());
                } catch (Exception e) { count = 7; }
                break;
            case "edges":
                try {
                    count = Integer.parseInt(edges.getText());
                }catch (Exception e) { count = 15; }
                break;
            default:
                count = 3;
        }
        System.out.printf("from text count: %d\n", count);
        return count;
    }

    public void setTextFields(TextField...fields) {
        for(TextField field : fields) {
            switch(field.getText().split(":")[0]) {
                case "nodes":
                    field.setPromptText("nodes: 2-20");
                    break;
                case "edges":
                    field.setPromptText("edges: 8-30");
                    break;
            }
            field.clear();
        }
    }
    public void setBuildType(Button build, boolean random) {
        if(random) {
            //CONSTRUCTS GRAPH RANDOMLY
            build.setOnAction(e -> {
                gc.generateRandomGraph(getCount("nodes"), getCount("edges"));
                run(stage);
            });
        }
        clearFields();
    }
    public void setFileSelector(Button fileSelector) {
        //FINDS FILE TO BUILD GRAPH
        fileSelector.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            try {
                fc.setInitialDirectory(new File("C:/Users/azva_/IdeaProjects/VisualGraphs/src/resources/text"));
                File selectedFile = fc.showOpenDialog(stage);
                filename = selectedFile.getAbsolutePath();
                gc.readFile(filename);
                System.out.println("reading file: " + filename);
                run(stage);
            }catch(Exception ex) {
                System.out.println("error");
            }
        });
        clearFields();
    }
    public void setMenu(MenuItem item, boolean directed) {
        //DETERMINES OPTIONS AT WHEN 'RUN' IS SELECTED
        if(directed) {
            item.setOnAction(e -> {
                if(gc.getGraph() == null)
                    return;
                gc.getGraph().setDirected(true);
                item.setText("directed");
                CanvasController.repaint();
            });
        }
        else {
            item.setOnAction(e -> {
                if(gc.getGraph() == null)
                    return;
                gc.getGraph().setDirected(false);
                item.setText("undirected");
                CanvasController.repaint();
            });
        }
    }
    public void setSize(Control...controls) {
        for(Control control : controls)
            control.setPrefWidth(80);
    }

    public void run(Stage stage) {
        //stage.sizeToScene();
        double w = stage.getWidth();
        double h = stage.getHeight();
        //INITALIZE GRAPH CALCULATIONS
        gc.setWidth((int)(w-16));
        gc.setHeight((int)(h)-64);
        gc.setRadius(radius);
        //CALCULATES GRAPH PLACEMENT
        gc.init();
        //gc.debug();
        //GET READY TO DRAW
        CanvasController.setGraphController(gc);
    }
    public void clearFields() {
        nodes.clear();
        edges.clear();
    }
}
