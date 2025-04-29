package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.observers.ModelObserver;
import edu.ntnu.idi.idatt.observers.ObservableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class Player implements ObservableModel {
  private final String name;
  private int position;
  private String character;

  private final List<ModelObserver> observers = new ArrayList<>();

  public Player(String name, String character, int position) {
    this.name = name;
    this.position = position;
    this.character = character;
  }

  public void setCharacter(String character) {
    this.character = character;
  }

  public String getCharacter() {
    return character;
  }

  public String getName() {
    return name;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    int oldPosition = this.position;
    this.position = position;
    notifyObservers("PLAYER_MOVED", new PositionChange(oldPosition, position));
  }

  public abstract int getStartPosition();

  public abstract boolean hasWon();

  public abstract <T> void move(int steps, T gameContext);

  @Override
  public String toString() {
    return name + "at position " + position;
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
    observers.forEach(obs -> obs.update(eventType, data));
  }

  public record PositionChange(int oldPosition, int newPosition) {}
}