package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.model.Tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Board {
  private List<Tile> tiles;

  public Board() {
    tiles = new ArrayList<>();
    for (int i = 1; i <= 100; i++) {
      tiles.add(new Tile(i)); // Initialize normal tiles
    }


    tiles.set(2, new Tile(3, 22));
    tiles.set(24, new Tile(25, 5));
  }

  public Tile getTile(int number) {
    return tiles.get(number - 1);
  }
}
