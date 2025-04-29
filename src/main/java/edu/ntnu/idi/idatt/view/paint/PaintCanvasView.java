package edu.ntnu.idi.idatt.view.paint;

import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController;
import edu.ntnu.idi.idatt.model.paint.PaintModel;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PaintCanvasView {
  private final BorderPane layout = new BorderPane();
  private final VBox leftPanel = new VBox();
  private final VBox rightPanel = new VBox();
  private final Canvas canvas = new Canvas(800, 600);
  private final PaintCanvasController controller;

  public PaintCanvasView(PaintModel model) {
    controller = new PaintCanvasController(model, this);
    layout.setLeft(createToolbox());
    layout.setCenter(canvas);
    layout.setRight(createPropertiesPanel());
  }

  private VBox createToolbox() {
    leftPanel.setStyle("-fx-background-color:#333");
    leftPanel.setPrefWidth(200);
    ToggleGroup tools = new ToggleGroup();
    ToggleButton pencil = new ToggleButton("Pencil");
    pencil.setToggleGroup(tools);
    pencil.setSelected(true);
    // ... add other tools to toggle group
    leftPanel.getChildren().addAll(pencil /*, ...*/);
    return leftPanel;
  }

  private VBox createPropertiesPanel() {
    rightPanel.setStyle("-fx-background-color:#222");
    rightPanel.setPrefWidth(200);
    ColorPicker colorPicker = new ColorPicker(javafx.scene.paint.Color.BLACK);
    colorPicker.setOnAction(e -> controller.setColor(colorPicker.getValue()));
    Slider sizeSlider = new Slider(1, 50, 2);
    sizeSlider.setShowTickLabels(true);
    sizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> controller.setWidth(newVal.doubleValue()));
    rightPanel.getChildren().addAll(colorPicker, sizeSlider);
    return rightPanel;
  }

  public Parent getRoot() {
    return layout;
  }

  public Canvas getCanvas() {
    return canvas;
  }
}
