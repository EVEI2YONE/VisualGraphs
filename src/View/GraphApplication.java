package View;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;

public class GraphApplication {

    static JFrame frame;
    static int height = 750, width = 1000;

    public static void main(String[] args) {
        frame = new JFrame("Graph Application");
        frame.setSize(new DimensionUIResource(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JPanel options = new JPanel();
        GraphPanel panel = createGraphPanel();

        //frame.add(options);
        JScrollPane pane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(pane);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void init() {
        frame = new JFrame("Graph Application");
        frame.setSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //addPanel();
        //addMenu();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void addMenu() {

    }

    public static GraphPanel createGraphPanel() {
        GraphPanel panel = new GraphPanel();
        panel.setFocusable(true);
        panel.setBackground(Color.white);
        return panel;
    }

}
