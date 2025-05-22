package edu.ntnu.idi.idatt.controller.paint;

import edu.ntnu.idi.idatt.model.paint.PaintModel;
import edu.ntnu.idi.idatt.model.paint.Stroke;
import edu.ntnu.idi.idatt.view.paint.PaintCanvasView;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * <h1>PaintCanvasController</h1>
 * Controls interaction between the PaintModel and PaintCanvasView. Handles user input, drawing
 * logic, and synchronization between model and view.
 * AI: used to understand and partially generate methods for this whole class
 *
 * @author Oliver, Christian
 */
public class PaintCanvasController implements PaintModel.Observer {

  /**
   * <h2>ToolType</h2>
   * Enum representing the type of tool currently selected in the paint application.
   */
  public enum ToolType {
    PENCIL,
    ERASER
  }

  private final PaintModel model;
  private final PaintCanvasView view;
  private ToolType currentTool = ToolType.PENCIL;
  private List<Point2D> currentPoints;
  private Color currentColor = Color.BLACK;
  private double currentWidth = 2.0;

  /**
   * <h2>Constructor</h2>
   * Initializes controller and sets up listeners and bindings.
   *
   * @param model the paint model
   * @param view  the paint canvas view
   */
  public PaintCanvasController(PaintModel model, PaintCanvasView view) {
    this.model = model;
    this.view = view;
    model.addObserver(this);
    initView();
    onModelChanged();
  }

  /**
   * <h2>initView</h2>
   * Sets up all event listeners and bindings between UI components and controller logic.
   */
  private void initView() {
    final GraphicsContext gc = view.getCanvas().getGraphicsContext2D();

    view.getToolButtons().forEach((type, btn) ->
        btn.setOnAction(e -> currentTool = type)
    );

    view.getColorPicker().setOnAction(e ->
        currentColor = view.getColorPicker().getValue()
    );

    view.getSizeSlider().valueProperty().addListener((obs, oldVal, newVal) ->
        currentWidth = newVal.doubleValue()
    );

    view.getUndoButton().setOnAction(e -> model.undo());
    view.getRedoButton().setOnAction(e -> model.redo());
    view.getClearButton().setOnAction(e -> model.clear());

    view.getCanvas().setOnMousePressed(e -> {
      currentPoints = new ArrayList<>();
      currentPoints.add(new Point2D(e.getX(), e.getY()));
    });

    view.getCanvas().setOnMouseDragged(e -> {
      double x;
      double y;
      x = e.getX();
      y = e.getY();
      Point2D last = currentPoints.getLast();
      currentPoints.add(new Point2D(x, y));
      double lastX = last.getX();
      double lastY = last.getY();
      Color drawColor = (currentTool == ToolType.ERASER) ? Color.WHITE : currentColor;
      gc.setStroke(drawColor);
      gc.setLineWidth(currentWidth);
      gc.strokeLine(lastX, lastY, x, y);
    });

    view.getCanvas().setOnMouseReleased(e -> {
      Color strokeColor = (currentTool == ToolType.ERASER) ? Color.WHITE : currentColor;
      model.addStroke(new Stroke(currentPoints, strokeColor, currentWidth));
    });
  }

  /**
   * <h2>onModelChanged</h2>
   * Called when the model is updated. Redraws the canvas and updates undo/redo buttons.
   */
  @Override
  public void onModelChanged() {
    redrawCanvas();
    view.getUndoButton().setDisable(!model.canUndo());
    view.getRedoButton().setDisable(!model.canRedo());
  }

  /**
   * <h2>redrawCanvas</h2>
   * Clears and redraws all strokes from the model onto the canvas.
   */
  private void redrawCanvas() {
    final GraphicsContext gc = view.getCanvas().getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, view.getCanvas().getWidth(), view.getCanvas().getHeight());

    model.getStrokes().forEach(stroke -> {
      gc.setStroke(stroke.color());
      gc.setLineWidth(stroke.width());

      List<Point2D> points = stroke.points();
      for (int i = 1; i < points.size(); i++) {
        Point2D p0 = points.get(i - 1);
        Point2D p1 = points.get(i);
        gc.strokeLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
      }
    });
  }
}