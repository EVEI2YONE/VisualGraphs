package views.main.components;

import javafx.scene.control.MenuItem;
import models.GraphAlgorithms;
import models.GraphAlgorithms.GraphType;

import java.util.List;

public interface VisualAlgorithmInterface {

    public MenuItem getMenuItem();
    public void setOnAction();
    public boolean validateStructure();
    public boolean setupAlgorithm();
    public void runAlgorithm();
    public void build();
}
