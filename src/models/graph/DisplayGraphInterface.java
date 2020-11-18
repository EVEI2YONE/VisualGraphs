package models.graph;

import javafx.scene.canvas.GraphicsContext;

public interface DisplayGraphInterface {
    public void displayShape(GraphicsContext graphicsContext);
    public void displayText(GraphicsContext graphicsContext);
    public void displayData(GraphicsContext graphicsContext);
}
