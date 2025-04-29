// PaintCanvasController.java
package edu.ntnu.idi.idatt.controller.paint;

import edu.ntnu.idi.idatt.model.paint.PaintModel;
import edu.ntnu.idi.idatt.model.paint.Stroke;
import edu.ntnu.idi.idatt.view.paint.PaintCanvasView;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PaintCanvasController implements PaintModel.Observer {
  public enum ToolType { PENCIL, ERASER }

  private final PaintModel model;
  private final PaintCanvasView view;

  private ToolType currentTool = ToolType.PENCIL;
  private List<Point2D> currentPoints;
  private Color currentColor = Color.BLACK;
  private double currentWidth = 2.0;

  public PaintCanvasController(PaintModel model, PaintCanvasView view) {
    this.model = model;
    this.view = view;
    model.addObserver(this);
    initView();
    onModelChanged();  // set initial undo/redo state
  }

  private void initView() {
    final GraphicsContext gc = view.getCanvas().getGraphicsContext2D();

    // 1) Tool toggles
    view.getToolButtons().forEach((type, btn) ->
        btn.setOnAction(e -> currentTool = type)
    );

    // 2) Color picker (only for pencil)
    view.getColorPicker().setOnAction(e ->
        currentColor = view.getColorPicker().getValue()
    );

    // 3) Width slider
    view.getSizeSlider().valueProperty().addListener((obs, oldVal, newVal) ->
        currentWidth = newVal.doubleValue()
    );

    // 4) Undo / Redo / Clear
    view.getUndoButton().setOnAction(e -> model.undo());
    view.getRedoButton().setOnAction(e -> model.redo());
    view.getClearButton().setOnAction(e -> model.clear());

    // 5) Freehand drawing
    view.getCanvas().setOnMousePressed(e -> {
      currentPoints = new ArrayList<>();
      currentPoints.add(new Point2D(e.getX(), e.getY()));
    });

    view.getCanvas().setOnMouseDragged(e -> {
      double x = e.getX(), y = e.getY();
      Point2D last = currentPoints.getLast();
      currentPoints.add(new Point2D(x, y));

      Color drawColor = (currentTool == ToolType.ERASER)
          ? Color.WHITE
          : currentColor;

      gc.setStroke(drawColor);
      gc.setLineWidth(currentWidth);
      gc.strokeLine(last.getX(), last.getY(), x, y);
    });

    view.getCanvas().setOnMouseReleased(e -> {
      Color strokeColor = (currentTool == ToolType.ERASER)
          ? Color.WHITE
          : currentColor;
      model.addStroke(new Stroke(currentPoints, strokeColor, currentWidth));
    });
  }

  @Override
  public void onModelChanged() {
    redrawCanvas();
    view.getUndoButton().setDisable(!model.canUndo());
    view.getRedoButton().setDisable(!model.canRedo());
  }

  private void redrawCanvas() {
    final GraphicsContext gc = view.getCanvas().getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(
        0, 0,
        view.getCanvas().getWidth(),
        view.getCanvas().getHeight()
    );
    for (Stroke s : model.getStrokes()) {
      gc.setStroke(s.getColor());
      gc.setLineWidth(s.getWidth());
      List<Point2D> pts = s.getPoints();
      for (int i = 1; i < pts.size(); i++) {
        Point2D p0 = pts.get(i - 1), p1 = pts.get(i);
        gc.strokeLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
      }
    }
  }
}
