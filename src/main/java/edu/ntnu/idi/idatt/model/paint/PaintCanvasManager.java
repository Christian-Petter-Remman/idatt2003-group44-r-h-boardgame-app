package edu.ntnu.idi.idatt.model.paint;

import java.util.ArrayList;
import java.util.List;

public class PaintCanvasManager {

  public interface Observer {

    void onModelChanged();
  }

  private final List<Observer> observers = new ArrayList<>();
  private final List<Stroke> strokes = new ArrayList<>();

  public void addObserver(Observer o) {
    observers.add(o);
  }

  public void removeObserver(Observer o) {
    observers.remove(o);
  }

  private void notifyObservers() {
    for (Observer o : observers) {
      o.onModelChanged();
    }
  }

  public void addStroke(Stroke s) {
    strokes.add(s);
    notifyObservers();
  }

  public List<Stroke> getStrokes() {
    return new ArrayList<>(strokes);
  }
}
