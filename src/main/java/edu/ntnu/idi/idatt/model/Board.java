package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.model.Tile.LadderTile;
import edu.ntnu.idi.idatt.model.Tile.SnakeTile;
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

    tiles.set(1-1 , new LadderTile(1, 21));
    tiles.set(2-1  , new LadderTile(2, 22));
    tiles.set(3-1 , new LadderTile(3, 23));
    tiles.set(4-1 , new LadderTile(4, 24));
    tiles.set(5-1, new LadderTile(5, 25));
    tiles.set(6-1, new LadderTile(6, 16));
  }

  public Tile getTile(int number) {
    return tiles.get(number - 1);
  }
}
