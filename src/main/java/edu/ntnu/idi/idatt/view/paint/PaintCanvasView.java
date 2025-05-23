package edu.ntnu.idi.idatt.view.paint;

import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController;
import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController.ToolType;
import edu.ntnu.idi.idatt.model.paint.PaintModel;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * <h1>PaintCanvasView</h1>
 * Javafx view class for a paint/drawing application.
 *
 * <P>AI: active involvement as sparring partner
 * </P>
 *
 * <p>JavaFX view class for a simple paint/drawing application. Provides tools for selecting
 * brushes,
 * colors, line width, and undo/redo functionality.
 * </p>
 */
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

  /**
   * <h2>Constructor</h2>
   * Initializes the paint canvas view and binds it to a PaintModel.
   *
   * @param model the paint model to observe and interact with
   */
  public PaintCanvasView(PaintModel model) {
    VBox toolbar = createToolbar();
    StackPane canvasHolder = new StackPane(canvas);
    canvasHolder.getStyleClass().add("canvas-holder");
    HBox.setHgrow(canvasHolder, Priority.ALWAYS);

    canvas.widthProperty().bind(canvasHolder.widthProperty());
    canvas.heightProperty().bind(canvasHolder.heightProperty());

    root.getChildren().addAll(toolbar, canvasHolder);
    root.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/PaintStyleSheet.css")).toExternalForm()
    );
    root.getStyleClass().add("paint-root");

    new PaintCanvasController(model, this);

    backBtn.setOnAction(e -> NavigationManager.getInstance().navigateBack());
  }

  /**
   * <h2>createToolbar</h2>
   * Builds the toolbar on the left with drawing tools and controls.
   *
   * @return VBox containing all toolbar UI elements
   */
  private VBox createToolbar() {
    VBox box = new VBox(15);
    box.setPadding(new Insets(10));
    box.getStyleClass().add("toolbar");

    Label drawLabel = new Label("Drawing Tools");
    drawLabel.getStyleClass().add("section-label");

    ToggleButton pencil = makeToggle(ToolType.PENCIL, "Pencil");
    ToggleButton eraser = makeToggle(ToolType.ERASER, "Eraser");

    Separator sep1 = new Separator();
    Separator sep2 = new Separator();

    Label colorLabel = new Label("Color:");
    Label widthLabel = new Label("Width:");

    VBox.setVgrow(new Region(), Priority.ALWAYS);

    box.getChildren().addAll(
        drawLabel, pencil, eraser,
        sep1, undoBtn, redoBtn, clearBtn,
        sep2, colorLabel, colorPicker, widthLabel, sizeSlider,
        new Region(), backBtn
    );

    ToggleGroup group = new ToggleGroup();
    toolButtons.values().forEach(btn -> btn.setToggleGroup(group));
    pencil.setSelected(true);

    box.setPrefWidth(180);
    return box;
  }

  /**
   * <h2>makeToggle</h2>
   * Creates and styles a toggle button for a drawing tool.
   *
   * @param type the tool type it represents
   * @param text the button label
   * @return the configured toggle button
   */
  private ToggleButton makeToggle(ToolType type, String text) {
    ToggleButton btn = new ToggleButton(text);
    btn.getStyleClass().addAll("tool-button", type.name().toLowerCase() + "-button");
    toolButtons.put(type, btn);
    return btn;
  }

  /**
   * <h2>getRoot</h2>
   * Gets the root node for embedding in scene.
   *
   * @return the root HBox containing the full view
   */
  public Parent getRoot() {
    return root;
  }

  /**
   * <h2>getCanvas</h2>
   * gets the drawing canvas.
   *
   * @return the drawing canvas
   */
  public Canvas getCanvas() {
    return canvas;
  }

  /**
   * <h2>getUndoButton</h2>
   * gets the undo button.
   *
   * @return the Undo button
   */
  public Button getUndoButton() {
    return undoBtn;
  }

  /**
   * <h2>getRedoButton</h2>
   * gets the redo button.
   *
   * @return the Redo button
   */
  public Button getRedoButton() {
    return redoBtn;
  }

  /**
   * <h2>getClearButton</h2>
   * gets the clear button.
   *
   * @return the Clear button
   */
  public Button getClearButton() {
    return clearBtn;
  }

  /**
   * <h2>getColorPicker</h2>
   * gets the color picker for stroke color.
   *
   * @return the ColorPicker for stroke color
   */
  public ColorPicker getColorPicker() {
    return colorPicker;
  }

  /**
   * <h2>getSizeSlider</h2>
   * gets the slider for stroke width.
   *
   * @return the slider for stroke width
   */
  public Slider getSizeSlider() {
    return sizeSlider;
  }

  /**
   * <h2>getToolButtons</h2>
   * gets the map of tool buttons.
   *
   * @return map of tool type to toggle buttons
   */
  public Map<ToolType, ToggleButton> getToolButtons() {
    return toolButtons;
  }
}