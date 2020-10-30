package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GraphApplication {

    static JFrame frame;
    static int height = 750, width = 1000;

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        frame = new JFrame("Graph Application");
        frame.setSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPanel();
        addMenu();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void addMenu() {

    }

    public static void addPanel() {
        GraphPanel panel = new GraphPanel();
        panel.useMouseListener();
        panel.useMouseMotionListener();
        panel.setFocusable(true);
        panel.setBackground(Color.white);
        frame.add(new JScrollPane(panel));
        panel.run();
    }

}
