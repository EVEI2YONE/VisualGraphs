package controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import models.Circle;
import models.Edge;
import models.Graph;
import models.Vertex;

public class CanvasController {
    private GraphController gc;
    private static CanvasController pin;
    private Circle selected = null;
    @FXML Canvas canvas;

    public CanvasController() {
        pin = this;
        if(canvas == null)
            canvas = new Canvas(500, 500);
    }

    public void paintComp() {
        if(canvas == null)
            return;
        canvas.setWidth(gc.getWidth());
        canvas.setHeight(gc.getHeight());
        GraphicsContext g =
                canvas.getGraphicsContext2D();
        g.clearRect(0,0,canvas.getWidth(), gc.getHeight());
        if(gc.getVertices() != null && gc.getEdges() != null) {
            for (Vertex v : gc.getVertices()) {
                Circle c = (Circle) v.getValue();
                //draw vertex objects (circles)
                if (c != null) {
                    int radius = (int) gc.getRadius();
                    g.setStroke(c.getColor());
                    g.strokeOval(c.getX() - radius, c.getY() - radius, radius * 2, radius * 2);
                    Font font = new Font("TimesRoman", radius);
                    g.setFont(font);
                    g.setStroke(Color.BLACK);
                    g.fillText(v.getLabel(), c.getX() - radius / 4, c.getY() + radius / 4);
                }
            }

            double angle = 45;
            double cosA = Math.cos(angle);
            double sinA = Math.sin(90.0-angle);
            for (Edge e : gc.getEdges()) {
                g.setFill(e.getColor());
                g.setStroke(e.getColor());
                int x1, y1, x2, y2;
                x1 = (int) e.getxStart();
                y1 = (int) e.getyStart();
                x2 = (int) e.getxEnd();
                y2 = (int) e.getyEnd();
                g.strokeLine(x1, y1, x2, y2);
                if(gc.getGraph().isDirected() && e.isDirected()) {
                    double r = 10,//gc.getRadius() * .5,
                          ux = e.getUHorizontal(),
                          uy = e.getUVertical(),
                           h = r * sinA,
                          hy = h * ux,
                          hx = h * uy,
                         r_d = r * cosA,
                       theta = Math.atan(uy/ux),
                       alpha = 90.0-theta,
                        r_dx = r_d * ux,
                        r_dy = r_d * uy,

                    x3, y3, x4, y4;
                    x3 = x2 + -1 *(- r_dx - hx);
                    y3 = y2 + -1 *(- r_dy + hy);
                    x4 = x2 + -1 *(- r_dx + hx);
                    y4 = y2 + -1 *(- r_dy - hy);
                    double[] start = new double[3],
                               end = new double[3];
                    start[0] = x3;
                    start[1] = x4;
                    start[2] = x2;
                    end[0] = y3;
                    end[1] = y4;
                    end[2] = y2;

                    Polygon arrow = new Polygon(x3, y3, x4, y4, x2, y2);
                    g.fillPolygon(start, end, 3);
                }
            }
        }
    }

    public static void setGraphController(GraphController gc) {
        if(gc == null)
            return;
        pin.gc = gc;
        pin.paintComp();
    }
    public static void repaint() { pin.paintComp(); }

    public static void setDimension(int width, int height) {
        pin.canvas.setWidth(width);
        pin.canvas.setHeight(height);
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        if(selected == null)
            return;
        System.out.println("dragging over");
        selected.setX((int)mouseEvent.getX());
        selected.setY((int)mouseEvent.getY());
        gc.updateEdges();
        //repaint();
        paintComp();
    }
    public void onMousePressed(MouseEvent mouseEvent) {
        if(gc == null)
            return;
        System.out.println("drag entered");
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        selected = (Circle)gc.findNode(x, y);
    }
}
