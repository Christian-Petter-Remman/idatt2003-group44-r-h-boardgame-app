package edu.ntnu.idi.idatt.model.paint;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class PaintModel {
  public interface Observer { void onModelChanged(); }

  private final List<Observer> observers = new ArrayList<>();
  private final List<Stroke> strokes = new ArrayList<>();
  private final Deque<Stroke> undoStack = new LinkedList<>();
  private final Deque<Stroke> redoStack = new LinkedList<>();

  public void addObserver(Observer o) { observers.add(o); }

  private void notifyObservers() {
    for (Observer o : observers) o.onModelChanged();
  }

  public void addStroke(Stroke s) {
    strokes.add(s);
    undoStack.push(s);
    redoStack.clear();
    notifyObservers();
  }

  public void undo() {
    if (!undoStack.isEmpty()) {
      Stroke s = undoStack.pop();
      strokes.remove(s);
      redoStack.push(s);
      notifyObservers();
    }
  }

  public void redo() {
    if (!redoStack.isEmpty()) {
      Stroke s = redoStack.pop();
      strokes.add(s);
      undoStack.push(s);
      notifyObservers();
    }
  }

  public void clear() {
    strokes.clear();
    undoStack.clear();
    redoStack.clear();
    notifyObservers();
  }

  public boolean canUndo() { return !undoStack.isEmpty(); }
  public boolean canRedo() { return !redoStack.isEmpty(); }

  public List<Stroke> getStrokes() {
    return new ArrayList<>(strokes);
  }
}
