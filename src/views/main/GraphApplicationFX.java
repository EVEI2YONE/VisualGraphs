package views.main;
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
import models.*;
import javafx.scene.paint.Color;
import models.graph.Graph;
import models.GraphAlgorithms;
import models.graph.MyMath;

import java.io.File;

public class GraphApplicationFX extends Application {

    public static void main(String[] args) {
        launch(args);


//        System.out.println("testing parallel");
//        testParallel();
//        System.out.println("testing non-intersecting orthogonal");
//        testOrthogonalFalse();
//        System.out.println("testing intersecting orthogonal");
//        testOrthogonalTrue();
//        System.out.println("testing intersections");
//        testIntersection();
    }

    public static void testParallel() {
        double[] xPoints, yPoints;
        //testing vector lines (means +/- combinations)
        xPoints = new double[] { 0, 5, 0, 0 }; //false: parallel
        yPoints = new double[] { 5, 5, 0, 5 };
        assertion(xPoints, yPoints, false);
        xPoints = new double[] { 0, 5, 5, 0 }; //false: parallel
        yPoints = new double[] { 5, 5, 0, 0 };
        assertion(xPoints, yPoints, false);
        xPoints = new double[] { 5, 0, 0, 0 }; //false: parallel
        yPoints = new double[] { 5, 5, 0, 5 };
        assertion(xPoints, yPoints, false);
        xPoints = new double[] { 5, 0, 5, 0 }; //false: parallel
        yPoints = new double[] { 5, 5, 0, 0 };
        assertion(xPoints, yPoints, false);
    }
    public static void testOrthogonalFalse() {
        double[] xPoints, yPoints;
        //left side
        xPoints = new double[] { 0, 5, -1, -1 }; //false: orthogonal
        yPoints = new double[] { 5, 5, 0, 9 };
        assertion(xPoints, yPoints, false);
        xPoints = new double[] { 0, 5, -1, -1 }; //false: orthogonal
        yPoints = new double[] { 5, 5, 9, 0 };
        assertion(xPoints, yPoints, false);


        //right side
        xPoints = new double[] { 5, 0, -1, -1 }; //false: orthogonal
        yPoints = new double[] { 5, 5, 0, 9 };
        assertion(xPoints, yPoints, false);
        xPoints = new double[] { 5, 0, -1, -1 }; //false: orthogonal
        yPoints = new double[] { 5, 5, 9, 0 };
        assertion(xPoints, yPoints, false);

    }
    public static void testOrthogonalTrue() {
        double[] xPoints, yPoints;
        xPoints = new double[] { 0, 5, 1, 1 }; //false: orthogonal
        yPoints = new double[] { 5, 5, 0, 9 };
        assertion(xPoints, yPoints, true);
        xPoints = new double[] { 0, 5, 1, 1 }; //false: orthogonal
        yPoints = new double[] { 5, 5, 9, 0 };
        assertion(xPoints, yPoints, true);
        xPoints = new double[] { 5, 0, 1, 1 }; //false: orthogonal
        yPoints = new double[] { 5, 5, 0, 9 };
        assertion(xPoints, yPoints, true);
        xPoints = new double[] { 5, 0, 1, 1 }; //false: orthogonal
        yPoints = new double[] { 5, 5, 9, 0 };
        assertion(xPoints, yPoints, true);
    }
    public static void testIntersection() {
        double[] xPoints, yPoints;
        xPoints = new double[] { 0, 6,  0, 1.99 }; //false: slightly not intersecting
        yPoints = new double[] { 6, 0, -2, 3.99 };
        assertion(xPoints, yPoints, false);

        xPoints = new double[] { 0, 6,  0, 2.01 }; //true: slightly past intersection
        yPoints = new double[] { 6, 0, -2, 4.01 };
        assertion(xPoints, yPoints, false);

        xPoints = new double[] { 0, 6,  0, 10 }; //false: intersection is further away
        yPoints = new double[] { 6, 0, -2, -1 };
        assertion(xPoints, yPoints, false);

        xPoints = new double[] { 0, 6, 0, -1 }; //false: intersection is further away
        yPoints = new double[] { 6, 0, -2, 7 };
        assertion(xPoints, yPoints, false);
    }

    public static void printArr(double[] list) {
        int i = 0;
        for(double d : list) {
            if(i > 0)
                System.out.print(" ");
            System.out.print(d);
            i++;
        }
        System.out.println();
    }
    public static void assertion(double[] xPoints, double[] yPoints, boolean assertion) {
//        printArr(xPoints);
//        printArr(yPoints);
//        System.out.print("test case " + (MyMath.testCase+1) + " ");
//        if(intersects(xPoints, yPoints) != assertion) {
//            System.out.println(" FAILURE");
//        }
//        else {
//            System.out.println(" SUCCESS");
//        }
    }
    public static boolean intersects(double[] xPoints, double[] yPoints){
        if(MyMath.linesIntersect(xPoints, yPoints)) {
            System.out.printf("%-17s", "intersects!");
            return true;
        }
        else {
            System.out.printf("%-17s", "doesn't intersect");
            return false;
        }
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

        Button randomize = new Button("Randomize Graph");
        nodes = new TextField("Nodes");
        edges = new TextField("Edges");
        Button fileSelector = new Button("Upload File");

        MenuButton menuButton = new MenuButton("Direction");
        MenuItem undirected = new MenuItem("Undirected");
        MenuItem directed = new MenuItem("Directed");
        menuButton.getItems().addAll(undirected, directed);

        Button runAlgorithm = new Button("Run Algorithm");
        Button rotate = new Button("Rotate 360");
        Button test = new Button("Setup Test");

        upper.getChildren().addAll(randomize, nodes, edges, fileSelector, menuButton, runAlgorithm, rotate);
        upper.getChildren().addAll(test);
        upper.setMinWidth(stage.getWidth());
        upper.setStyle("-fx-background-color: #c7c6c6");

        FXMLLoader canvas_fxml = new FXMLLoader(getClass().getResource("../../resources/fxml/canvas-graph.fxml"));
        Parent canvas_node = canvas_fxml.load();
        canvas_node.setStyle("-fx-background-color: #f0e6e6");
        root.getChildren().addAll(upper, canvas_node);

        Scene scene = new Scene(root);
        //CONTROL NODE SETUP
        setSize(nodes, edges);
        setTextFields(nodes, edges);
        setMenu(undirected, false);
        setMenu(directed, true);
        setFileSelector(fileSelector);
        setBuildType(randomize, true);
        startAlgorithm(runAlgorithm);
        setRotate(rotate);
        setScene(scene);
        //---------------------------------


        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FileParser.saveFile(gc.getGraph());
        super.stop();
    }

    public void setRotate(Button rotate) {
        rotate.setOnAction(e -> {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.printf("width: %f, height: %f\n", stage.getWidth(), stage.getHeight());
                    CanvasController.rotateGraphAveragePivot();
                    //CanvasController.rotateGraph(stage.getWidth(), stage.getHeight());
                }
            });
            thread.start();
        });
    }

    TextField nodes, edges;
    public void startAlgorithm(Button operation) {
        operation.setOnAction(e -> {
            Graph g = gc.getGraph();
            if(g == null)
                return;
            if(ac.getGraph() == null || g != ac.getGraph())
                ac.setGraph(g);
            Color
                traversing = Color.rgb(78, 210, 187, .6),
                visited = Color.rgb(216, 13, 13, .7),
                searching = Color.rgb(216, 99, 20, .9);
            ac.setUpGraph(GraphAlgorithms.OperationType.SEARCH,
                          GraphAlgorithms.SearchType.DFS,
                          GraphAlgorithms.GraphType.DIRECTED);
            ac.setColors(traversing, visited, searching);
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
                //gc.setRadius();
                gc.generateRandomGraph(getCount("nodes"), getCount("edges"));
                run(stage, true);
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
                Graph graph = FileParser.parseFile(filename);
                gc.setGraph(graph);
                System.out.println("reading file: " + filename);
                if(filename.contains(".cus"))
                    run(stage, false);
                else
                    run(stage, true);
            }catch(Exception ex) {
                ex.printStackTrace();
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

    public void run(Stage stage, boolean calculatePlacement) {
        if(gc == null)
            return;
        double w = stage.getWidth();
        double h = stage.getHeight();
        //CALCULATES GRAPH PLACEMENT
        if(!(gc.getGraph()).isAlreadyPlaced()) {
            gc.calculatePlacement();
        }
        gc.init();
        //gc.debug();
        //GET READY TO DRAW
        CanvasController.setGraphController(gc);
    }
    public void clearFields() {
        nodes.clear();
        edges.clear();
    }

    double zoom = 1.0;
    double delta = 0.03;
    public void setScene(Scene scene) {
        setScrolling(scene);
        //KEY LISTENERS
        scene.setOnKeyPressed(e -> {
            CanvasController.onKeyPressed(e.getCode());
        });
        scene.setOnKeyReleased(e -> {
            CanvasController.onKeyReleased(e.getCode());
        });
    }
    public void setScrolling(Scene scene) {
        //SCROLLING
        scene.setOnScroll(e -> {
            if(gc == null)
                return;
            boolean change = false;
            double rate = e.getDeltaY();
            if(rate > 0) { //scrolling up
                if(zoom >= 3.0)
                    zoom = 2.9;
                else
                    change = true;
                rate = delta;
            }
            else {
                if(zoom <= 0.6)
                    zoom = 0.7;
                else
                    change = true;
                rate = -delta;
            }
            zoom += rate;
            if(change) {
                gc.resizeGraph(rate);
            }
            CanvasController.repaint();
        });
    }
}
