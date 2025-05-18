package edu.ntnu.idi.idatt.model.paint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import java.util.List;

public record Stroke(List<Point2D> points, Color color, double width) {

}
