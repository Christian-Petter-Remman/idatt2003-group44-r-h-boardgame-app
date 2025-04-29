package edu.ntnu.idi.idatt.observers;

public interface ObservableModel {
  void addObserver(ModelObserver observer);
  void removeObserver(ModelObserver observer);
  void notifyObservers(String eventType, Object data);

}

