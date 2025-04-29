package edu.ntnu.idi.idatt.controller.paint;

import edu.ntnu.idi.idatt.model.paint.PaintModel;
import edu.ntnu.idi.idatt.model.paint.Stroke;
import edu.ntnu.idi.idatt.view.paint.PaintCanvasView;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class PaintCanvasController implements PaintModel.Observer {
  private final PaintModel model;
  private final PaintCanvasView view;

  private List<Point2D> currentPoints;
  private Color currentColor = Color.BLACK;
  private double currentWidth = 2.0;

  public PaintCanvasController(PaintModel model, PaintCanvasView view) {
    this.model = model;
    this.view = view;
    this.model.addObserver(this);
    initView();
  }

  private void initView() {
    GraphicsContext gc = view.getCanvas().getGraphicsContext2D();

    view.getCanvas().setOnMousePressed(e -> {
      currentPoints = new ArrayList<>();
      currentPoints.add(new Point2D(e.getX(), e.getY()));
    });

    view.getCanvas().setOnMouseDragged(e -> {
      Point2D last = currentPoints.getLast();
      currentPoints.add(new Point2D(e.getX(), e.getY()));
      gc.setStroke(currentColor);
      gc.setLineWidth(currentWidth);
      gc.strokeLine(last.getX(), last.getY(), e.getX(), e.getY());
    });

    view.getCanvas().setOnMouseReleased(e -> model.addStroke(new Stroke(currentPoints, currentColor, currentWidth)));
  }

  @Override
  public void onModelChanged() {
    redrawCanvas();
  }

  private void redrawCanvas() {
    GraphicsContext gc = view.getCanvas().getGraphicsContext2D();
    gc.clearRect(0, 0, view.getCanvas().getWidth(), view.getCanvas().getHeight());
    for (Stroke s : model.getStrokes()) {
      gc.setStroke(s.getColor());
      gc.setLineWidth(s.getWidth());
      List<Point2D> pts = s.getPoints();
      for (int i = 1; i < pts.size(); i++) {
        Point2D p0 = pts.get(i - 1);
        Point2D p1 = pts.get(i);
        gc.strokeLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
      }
    }
  }

  public void setColor(Color color) {
    this.currentColor = color;
  }

  public void setWidth(double width) {
    this.currentWidth = width;
  }
}