package edu.ntnu.idi.idatt.model.paint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.List;

public class Stroke {
  private final List<Point2D> points;
  private final Color color;
  private final double width;

  public Stroke(List<Point2D> points, Color color, double width) {
    this.points = points;
    this.color = color;
    this.width = width;
  }

  public List<Point2D> getPoints() {
    return points;
  }

  public Color getColor() {
    return color;
  }

  public double getWidth() {
    return width;
  }
}
