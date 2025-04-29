package edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.observers.ModelObserver;
import edu.ntnu.idi.idatt.observers.ObservableModel;
import java.util.ArrayList;
import java.util.List;

public class Tile implements ObservableModel {

  private final int numberOfTile;
  private final int destination;
  private final boolean isSpecialTile;

  // Observer list
  private final List<ModelObserver> observers = new ArrayList<>();

  public Tile(int numberOfTile) {
    this.numberOfTile = numberOfTile;
    this.destination = -1;
    this.isSpecialTile = false;
  }

  public Tile(int numberOfTile, int destination) {
    this.numberOfTile = numberOfTile;
    this.destination = destination;
    this.isSpecialTile = true;
  }

  // ObservableModel implementation
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

  public void onPlayerLanded(Player player) {
    notifyObservers("PLAYER_LANDED", player);
  }

  public int getNumberOfTile() {
    return numberOfTile;
  }

  public int getDestination() {
    return destination;
  }

  public boolean hasSnakeOrLadder() {
    return isSpecialTile;
  }

  @Override
  public String toString() {
    if (hasSnakeOrLadder()) {
      return "Tile " + numberOfTile + " -> " + destination;
    } else {
      return "Tile " + numberOfTile;
    }
  }
}
