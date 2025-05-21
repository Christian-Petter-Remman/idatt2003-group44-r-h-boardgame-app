package edu.ntnu.idi.idatt.model.paint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class PaintModelTest {

  private PaintModel model;
  private Stroke stroke1;
  private Stroke stroke2;

  @BeforeEach
  void setUp() {
    model = new PaintModel();
    stroke1 = new Stroke(List.of(new Point2D(1, 2)), Color.RED, 3.0);
    stroke2 = new Stroke(List.of(new Point2D(5, 6)), Color.BLUE, 2.0);
  }

  @Test
  void testAddAndGetStrokes() {
    assertTrue(model.getStrokes().isEmpty());
    model.addStroke(stroke1);
    model.addStroke(stroke2);
    List<Stroke> strokes = model.getStrokes();
    assertEquals(2, strokes.size());
    assertEquals(stroke1, strokes.get(0));
    assertEquals(stroke2, strokes.get(1));
  }

  @Test
  void testUndoRedo() {
    model.addStroke(stroke1);
    model.addStroke(stroke2);
    assertTrue(model.canUndo());
    model.undo();
    List<Stroke> strokes = model.getStrokes();
    assertEquals(1, strokes.size());
    assertEquals(stroke1, strokes.getFirst());
    assertTrue(model.canRedo());
    model.redo();
    assertEquals(2, model.getStrokes().size());
    assertEquals(stroke2, model.getStrokes().get(1));
  }

  @Test
  void testClear() {
    model.addStroke(stroke1);
    model.addStroke(stroke2);
    model.clear();
    assertTrue(model.getStrokes().isEmpty());
    assertFalse(model.canUndo());
    assertFalse(model.canRedo());
  }

  @Test
  void testUndoWithNothingDoesNothing() {
    assertFalse(model.canUndo());
    assertDoesNotThrow(() -> model.undo());
    assertTrue(model.getStrokes().isEmpty());
  }

  @Test
  void testRedoWithNothingDoesNothing() {
    assertFalse(model.canRedo());
    assertDoesNotThrow(() -> model.redo());
    assertTrue(model.getStrokes().isEmpty());
  }

  @Test
  void testObserversAreNotified() {
    AtomicInteger notified = new AtomicInteger(0);
    PaintModel.Observer observer = notified::incrementAndGet;
    model.addObserver(observer);
    model.addStroke(stroke1);
    model.undo();
    model.redo();
    model.clear();
    assertEquals(4, notified.get());
    model.removeObserver(observer);
    model.addStroke(stroke2);
    assertEquals(4, notified.get());
  }
}
