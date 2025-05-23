package edu.ntnu.idi.idatt.model.paint;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


/**
 * <h1>Stroke</h1>
 *
 * <p>Represents a drawn stroke in a paint application, consisting of a series of points, a color,
 * and
 * a line width.
 */
public record Stroke(List<Point2D> points, Color color, double width) {

  /**
   * <h2>points</h2>
   *
   * <p>The list of points defining the stroke path.
   *
   * @return the list of {@link Point2D} points
   */
  @Override
  public List<Point2D> points() {
    return points;
  }

  /**
   * <h2>color</h2>
   *
   * <p>The color of the stroke.
   *
   * @return the {@link Color} used
   */
  @Override
  public Color color() {
    return color;
  }

  /**
   * <h2>width</h2>
   *
   * <p>The width of the stroke.
   *
   * @return the stroke width as a double
   */
  @Override
  public double width() {
    return width;
  }
}