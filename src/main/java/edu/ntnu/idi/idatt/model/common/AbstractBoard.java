package edu.ntnu.idi.idatt.model.common;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBoard {
  protected Map<Integer, Tile> tiles = new HashMap<>();

  public AbstractBoard(int size) {
    for (int i = 0; i < size; i++) {
      tiles.put(i, new Tile(i));
    }
  }

  public void initializeBoard() {
    for (int i = 1; i <= getSize(); i++) {
      addTile(new Tile(1));
    }
  }

  public Tile getTile(int number) {
    Tile tile = tiles.get(number);
    if (tile == null) {
      throw new IllegalArgumentException("Tile number " + number + " not found!");
    }
    return tile;
  }

  public void addTile(Tile tile) {
    tiles.put(tile.getNumber(), tile);
  }

  public abstract int getSize();
}
