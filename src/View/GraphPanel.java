package View;

import controllers.AlgorithmsController;
import controllers.GraphController;
import models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, Runnable {
    private GraphController graph_controller = new GraphController();
    private AlgorithmsController algorithms_controller;
    private static GraphPanel pin = null;
    //Initialize graph panel:
    //  Create new graph_controller which holds Graph instance
    //  Paints to display graph in its current state
    /*TODO:
        DON'T FORGET ABOUT THESE TWO METHODS (PLUS 1 CONSTRUCTOR) THAT CAN HAVE INFLUENCE ON THE REST OF THE PROGRAM
        1. GRAPHPANEL()
        2. GRAPHCONTROLLERINIT()
        3. ALGORITHMSCONTROLLERINIT()
     */

    public GraphPanel() {
        pin = this;
        addMouseListener(this);
        addMouseMotionListener(this);
        GraphControllerInit();
        AlgorithmsControllerInit();
        algorithms_controller.startOperation();
    }

    public void GraphControllerInit() {
        //graph_controller.readFile("C:\\Users\\azva_\\IdeaProjects\\VisualGraphs\\src\\resources\\text\\Input_Test1.txt");
        //graph_controller.generateRandomGraph(16);
//        graph_controller.debug();
//        graph_controller.setWidth(750);
//        graph_controller.setHeight(650);
//        graph_controller.setRadius(15);
//        graph_controller.init();
    }
    public void AlgorithmsControllerInit() {
        Graph g = graph_controller.getGraph();
        if(g == null)
            return;
        algorithms_controller = new AlgorithmsController(g);
        //algorithms_controller.setColors(Color.BLUE, Color.RED);
    }

    //  Draws vertex objects and edges: colors, font, drawing calculations can be done elsewhere
    public static void update(Vertex v, Color c) {
        /*
        Circle circle = (Circle) v.getValue();
        circle.setColor(c);
        pin.repaint();
         */
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*
        if(graph_controller.getVertices() != null && graph_controller.getEdges() != null) {
            for (Vertex v : graph_controller.getVertices()) {
                Circle c = (Circle) v.getValue();
                if (c != null) {
                    int radius = (int) graph_controller.getRadius();
                    g.setColor(c.getColor());
                    g.drawOval(c.getX() - radius, c.getY() - radius, radius * 2, radius * 2);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, radius));
                    g.drawString(v.getLabel(), c.getX() - radius / 4, c.getY() + radius / 4);
                }
            }
            for (Edge e : graph_controller.getEdges()) {
                g.setColor(Color.black);
                int x1 = e.getxStart();
                int y1 = e.getyStart();
                int x2 = e.getxEnd();
                int y2 = e.getyEnd();
                g.drawLine(x1, y1, x2, y2);
            }
        }
         */
    }

    @Override
    public void run() {
        algorithms_controller.startOperation();
    }

    //DRAG AND DROP VERTEX OBJECTS
    Circle selected = null;
    int index = -1;

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.printf("x: %d, y: %d\n", x, y);
        //selected = (Circle)graph_controller.findNode(x, y);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        selected = null;
        index = -1;
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(selected != null) {
            //selected.setX(e.getX());
            //selected.setY(e.getY());
            graph_controller.updateEdges();
        }
        repaint();
    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
