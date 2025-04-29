package edu.ntnu.idi.idatt.view.paint;

import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController;
import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController.ToolType;
import edu.ntnu.idi.idatt.model.paint.PaintModel;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class PaintCanvasView {
  private final HBox root = new HBox();
  private final Canvas canvas = new Canvas();
  private final Button undoBtn = new Button("Undo");
  private final Button redoBtn = new Button("Redo");
  private final Button clearBtn = new Button("Clear");
  private final Button backBtn = new Button("← Back");
  private final ColorPicker colorPicker = new ColorPicker(javafx.scene.paint.Color.BLACK);
  private final Slider sizeSlider = new Slider(1, 50, 2);
  private final Map<ToolType, ToggleButton> toolButtons = new EnumMap<>(ToolType.class);

  public PaintCanvasView(PaintModel model) {
    // 1) Build UI
    VBox toolbar = createToolbar();
    StackPane canvasHolder = new StackPane(canvas);
    canvasHolder.getStyleClass().add("canvas-holder");
    HBox.setHgrow(canvasHolder, Priority.ALWAYS);

    canvas.widthProperty().bind(canvasHolder.widthProperty());
    canvas.heightProperty().bind(canvasHolder.heightProperty());

    root.getChildren().addAll(toolbar, canvasHolder);

    root.getStylesheets().add(
        Objects.requireNonNull(
            getClass().getResource("/css/PaintStyleSheet.css")
        ).toExternalForm()
    );

    root.getStyleClass().add("paint-root");

    new PaintCanvasController(model, this);

    getBackButton().setOnAction(e -> NavigationManager.getInstance().navigateBack());
  }

  private VBox createToolbar() {
    VBox box = new VBox(15);
    box.setPadding(new Insets(10));
    box.getStyleClass().add("toolbar");

    Label drawLabel = new Label("Drawing Tools");
    drawLabel.getStyleClass().add("section-label");
    ToggleButton pencil = makeToggle(ToolType.PENCIL, "Pencil");
    ToggleButton eraser = makeToggle(ToolType.ERASER, "Eraser");

    box.getChildren().addAll(
        drawLabel, pencil, eraser,
        new Separator(),
        undoBtn, redoBtn, clearBtn,
        new Separator(),
        new Label("Color:"), colorPicker,
        new Label("Width:"), sizeSlider
    );

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    box.getChildren().add(spacer);

    backBtn.getStyleClass().add("back-button");
    box.getChildren().add(backBtn);

    ToggleGroup group = new ToggleGroup();
    toolButtons.values().forEach(btn -> btn.setToggleGroup(group));
    pencil.setSelected(true);

    box.setPrefWidth(180);
    return box;
  }

  private ToggleButton makeToggle(ToolType type, String text) {
    ToggleButton btn = new ToggleButton(text);
    btn.getStyleClass().addAll("tool-button", type.name().toLowerCase() + "-button");
    toolButtons.put(type, btn);
    return btn;
  }


  public Parent getRoot() {
    return root;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public Button getUndoButton() {
    return undoBtn;
  }

  public Button getRedoButton() {
    return redoBtn;
  }

  public Button getClearButton() {
    return clearBtn;
  }

  public Button getBackButton() {
    return backBtn;
  }

  public ColorPicker getColorPicker() {
    return colorPicker;
  }

  public Slider getSizeSlider() {
    return sizeSlider;
  }

  public Map<ToolType, ToggleButton> getToolButtons() {
    return toolButtons;
  }
}
