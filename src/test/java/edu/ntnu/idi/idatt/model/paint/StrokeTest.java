package edu.ntnu.idi.idatt.model.paint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StrokeTest {

  @Test
  void testStrokeConstructorAndGetters() {
    List<Point2D> points = List.of(new Point2D(1, 2), new Point2D(3, 4));
    Color color = Color.BLUE;
    double width = 5.0;
    Stroke stroke = new Stroke(points, color, width);

    assertEquals(points, stroke.points());
    assertEquals(color, stroke.color());
    assertEquals(width, stroke.width());
  }

  @Test
  void testStrokeEqualsAndHashCode() {
    List<Point2D> points1 = List.of(new Point2D(1, 2));
    List<Point2D> points2 = List.of(new Point2D(1, 2));
    Stroke s1 = new Stroke(points1, Color.RED, 2.0);
    Stroke s2 = new Stroke(points2, Color.RED, 2.0);
    Stroke s3 = new Stroke(points1, Color.BLACK, 2.0);

    assertEquals(s1, s2);
    assertEquals(s1.hashCode(), s2.hashCode());
    assertNotEquals(s1, s3);
  }
}
