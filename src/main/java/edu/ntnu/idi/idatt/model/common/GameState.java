package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.observers.ModelObserver;
import edu.ntnu.idi.idatt.observers.ObservableModel;
import java.util.ArrayList;
import java.util.List;

public class GameState implements ObservableModel {
  private final List<ModelObserver> observers = new ArrayList<>();

  public void setGameOver(boolean gameOver) {
    notifyObservers("GAME_OVER", gameOver);
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers(String eventType, Object data) {
    new ArrayList<>(observers).forEach(obs -> obs.update(eventType, data));
  }
}
