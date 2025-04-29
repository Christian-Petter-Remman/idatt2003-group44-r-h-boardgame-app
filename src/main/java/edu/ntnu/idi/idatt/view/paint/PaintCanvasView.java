package edu.ntnu.idi.idatt.view.paint;

import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PaintCanvasView {
  private Parent root;
  private final BorderPane layout = new BorderPane();
  private final VBox leftPanel = new VBox();

  private final Canvas canvas = new Canvas(800, 600);
  private final PaintCanvasController controller = new PaintCanvasController();

  public PaintCanvasView() {


    // Have this at the end
    layout.setCenter(createCanvas());
    layout.setLeft(createLeftPanel());

    this.root = layout;
  }

  private VBox createLeftPanel() {
    leftPanel.setStyle("-fx-background-color: #000");
    leftPanel.setPrefWidth(200);
    leftPanel.setPrefHeight(600);

    // Add buttons and other UI elements to the left panel here

    return leftPanel;
  }

  private Canvas createCanvas() {
    canvas.setStyle("-fx-background-color: #fff");
    canvas.setWidth(800);
    canvas.setHeight(600);


    return canvas;
  }

  public Parent getRoot() {
    return root;
  }
}
