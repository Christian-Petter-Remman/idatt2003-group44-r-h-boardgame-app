
package edu.ntnu.idi.idatt.view.paint;

import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController;
import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController.ToolType;
import edu.ntnu.idi.idatt.model.paint.PaintModel;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

public class PaintCanvasView {
  private final Stage stage;
  private final HBox root = new HBox();
  private final Canvas canvas = new Canvas();
  private final Button undoBtn = new Button("Undo");
  private final Button redoBtn = new Button("Redo");
  private final Button clearBtn = new Button("Clear");
  private final ColorPicker colorPicker = new ColorPicker(javafx.scene.paint.Color.BLACK);
  private final Slider sizeSlider = new Slider(1, 50, 2);
  private final Map<ToolType, ToggleButton> toolButtons = new EnumMap<>(ToolType.class);
  private final PaintCanvasController controller;

  public PaintCanvasView(PaintModel model, Stage stage) {
    this.stage = stage;
    controller = new PaintCanvasController(model, this);

    VBox toolbar = createToolbar();
    StackPane canvasHolder = new StackPane(canvas);
    canvasHolder.setStyle("-fx-background-color: white;");
    HBox.setHgrow(canvasHolder, Priority.ALWAYS);

    canvas.widthProperty().bind(canvasHolder.widthProperty());
    canvas.heightProperty().bind(canvasHolder.heightProperty());

    root.getChildren().addAll(toolbar, canvasHolder);
  }

  private VBox createToolbar() {
    VBox box = new VBox(15);
    box.setPadding(new Insets(10));
    box.getStyleClass().add("toolbar");


    Label drawLabel = new Label("Drawing Tools");
    drawLabel.getStyleClass().add("section-label");
    ToggleButton pencil = makeToggle(ToolType.PENCIL,  "Pencil");
    ToggleButton eraser = makeToggle(ToolType.ERASER,  "Eraser");

    box.getChildren().addAll(
        drawLabel, pencil, eraser,
        new Separator(),
        undoBtn, redoBtn, clearBtn,
        new Separator(),
        new Label("Color:"), colorPicker,
        new Label("Width:"), sizeSlider
    );

    ToggleGroup group = new ToggleGroup();
    toolButtons.values().forEach(btn -> btn.setToggleGroup(group));
    pencil.setSelected(true);

    box.setPrefWidth(180);
    return box;
  }

  private ToggleButton makeToggle(ToolType type, String text) {
    ToggleButton btn = new ToggleButton(text);
    btn.getStyleClass().add("tool-button");
    toolButtons.put(type, btn);
    return btn;
  }

  public Parent getRoot()        { return root; }
  public Scene  getScene() {
    Scene scene = new Scene(root);
    scene.getStylesheets().add(
        getClass().getResource("/css/PaintStyleSheet.css")
            .toExternalForm()
    );
    return scene;
  }
  public Canvas               getCanvas()      { return canvas; }
  public Button               getUndoButton()  { return undoBtn; }
  public Button               getRedoButton()  { return redoBtn; }
  public Button               getClearButton() { return clearBtn; }
  public ColorPicker          getColorPicker() { return colorPicker; }
  public Slider               getSizeSlider()  { return sizeSlider; }
  public Map<ToolType,ToggleButton> getToolButtons() { return toolButtons; }
  public Stage                getStage()       { return stage; }
}
