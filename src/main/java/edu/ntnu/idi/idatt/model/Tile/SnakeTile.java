package edu.ntnu.idi.idatt.model.Tile;

public class SnakeTile {

  public static Tile createSnake(int tileNumber, int destination) {
    if (destination > tileNumber) {
      throw new IllegalArgumentException("A snake must lead to a lower tile.");
    }
    return new Tile(tileNumber, destination);
  }


}