package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Circle;
import models.Edge;
import models.Graph;
import models.Vertex;

import java.net.URL;
import java.util.ResourceBundle;

public class CanvasController implements Initializable {
    private GraphController gc;
    private AlgorithmsController ac;
    private static CanvasController pin;
    private static boolean built = false;
    @FXML Canvas canvas;

    public CanvasController() {
        pin = this;
        if(canvas == null)
            canvas = new Canvas(500, 500);
        System.out.println("ID: " + pin);
    }

    public void paintComp() {
        built = false;
        if(canvas == null)
            return;
        GraphicsContext g =
                canvas.getGraphicsContext2D();
        g.clearRect(0,0,canvas.getWidth(), gc.getHeight());
        System.out.println("Starting to paint");
        if(gc.getVertices() != null && gc.getEdges() != null) {


            for (Vertex v : gc.getVertices()) {
                Circle c = (Circle) v.getValue();
                if (c != null) {
                    int radius = (int) gc.getRadius();

                    g.setFill(c.getColor());
                    g.fillOval(c.getX() - radius, c.getY() - radius, radius * 2, radius * 2);
                    Font font = new Font("TimesRoman", radius);
                    g.setFont(font);
                    g.fillText(v.getLabel(), c.getX() - radius / 4, c.getY() + radius / 4);
                }
            }

            for (Edge e : gc.getEdges()) {
                g.setFill(Color.BLACK);
                int x1 = e.getxStart();
                int y1 = e.getyStart();
                int x2 = e.getxEnd();
                int y2 = e.getyEnd();
                g.strokeLine(x1, y1, x2, y2);
            }
        }
        System.out.println("Finished painting");
        built = true;
    }

    @FXML
    public void onMousePressed(MouseEvent mouseEvent) {

    }
    @FXML
    public void onDragOver(DragEvent event) {
        System.out.println("dragging ouse!");
    }

    public static void setGraphController(GraphController gc) {
        if(gc == null)
            return;
        pin.gc = gc;
        pin.ac = new AlgorithmsController(gc.getGraph());
        pin.paintComp();
    }
    public static void repaint() { pin.paintComp(); }
    public static boolean isBuilt() { return pin.built; }

    public static void setDimension(int width, int height) {
        pin.canvas.setWidth(width);
        pin.canvas.setHeight(height);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
