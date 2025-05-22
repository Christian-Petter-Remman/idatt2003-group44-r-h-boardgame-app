package edu.ntnu.idi.idatt.model.paint;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>PaintModel</h1>
 *
 * <p>Manages the state of drawn strokes in a paint application. Supports undo, redo, and change
 * notification via observer pattern.
 */
public class PaintModel {

  /**
   * <h2>Observer</h2>
   *
   * <p>Functional interface for objects that wish to be notified when the model changes.
   */
  public interface Observer {
    /**
     * <h2>onModelChanged</h2>
     *
     * <p>Called when the model changes, such as after a stroke is added or removed.
     */
    void onModelChanged();
  }

  private final List<Observer> observers = new ArrayList<>();
  private final List<Stroke> strokes = new ArrayList<>();
  private final Deque<Stroke> undoStack = new LinkedList<>();
  private final Deque<Stroke> redoStack = new LinkedList<>();

  /**
   * <h2>addObserver</h2>
   *
   * <p>Registers an observer to receive model change notifications.
   *
   * @param o The observer to register.
   */
  public void addObserver(Observer o) {
    observers.add(o);
  }

  /**
   * <h2>removeObserver</h2>
   *
   * <p>Unregisters an observer.
   *
   * @param o The observer to remove.
   */
  public void removeObserver(Observer o) {
    observers.remove(o);
  }

  /**
   * <h2>notifyObservers</h2>
   *
   * <p>Notifies all registered observers of a model change.
   */
  private void notifyObservers() {
    for (Observer o : observers) {
      o.onModelChanged();
    }
  }

  /**
   * <h2>addStroke</h2>
   *
   * <p>Adds a new stroke and resets the redo history.
   *
   * @param s The stroke to add.
   */
  public void addStroke(Stroke s) {
    strokes.add(s);
    undoStack.push(s);
    redoStack.clear();
    notifyObservers();
  }

  /**
   * <h2>undo</h2>
   *
   * <p>Undoes the most recent stroke, if possible.
   */
  public void undo() {
    if (!undoStack.isEmpty()) {
      Stroke s = undoStack.pop();
      strokes.remove(s);
      redoStack.push(s);
      notifyObservers();
    }
  }

  /**
   * <h2>redo</h2>
   *
   * <p>Reapplies the most recently undone stroke, if possible.
   */
  public void redo() {
    if (!redoStack.isEmpty()) {
      Stroke s = redoStack.pop();
      strokes.add(s);
      undoStack.push(s);
      notifyObservers();
    }
  }

  /**
   * <h2>clear</h2>
   *
   * <p>Clears all strokes and resets undo/redo history.
   */
  public void clear() {
    strokes.clear();
    undoStack.clear();
    redoStack.clear();
    notifyObservers();
  }

  /**
   * <h2>canUndo.</h2>
   *
   * @return true if undo is possible, false otherwise.
   */
  public boolean canUndo() {
    return !undoStack.isEmpty();
  }

  /**
   * <h2>canRedo.</h2>
   *
   * @return true if redo is possible, false otherwise.
   */
  public boolean canRedo() {
    return !redoStack.isEmpty();
  }

  /**
   * <h2>getStrokes.</h2>
   *
   * @return a copy of the current list of strokes.
   */
  public List<Stroke> getStrokes() {
    return new ArrayList<>(strokes);
  }
}