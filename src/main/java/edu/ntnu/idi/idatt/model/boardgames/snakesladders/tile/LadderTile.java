package edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile;

public class LadderTile extends Tile {
  public LadderTile(int numberOfTile, int destination) {
    super(numberOfTile, destination);
    if (destination <= numberOfTile) {
      throw new IllegalArgumentException("A ladder must lead to a higher tile.");
    }
  }
}
