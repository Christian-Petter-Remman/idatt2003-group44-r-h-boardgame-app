package edu.ntnu.idi.idatt.model.Tile;

public class LadderTile {

  public static Tile createLadder(int tileNumber, int destination) {
    if (destination < tileNumber) {
      throw new IllegalArgumentException("A ladder must lead to a higher tile.");
    }
    return new Tile(tileNumber, destination);
  }

  public static Tile createSnake(int tileNumber, int destination) {
    if (destination > tileNumber) {
      throw new IllegalArgumentException("A snake must lead to a lower tile.");
    }
    return new Tile(tileNumber, destination);
  }


}
