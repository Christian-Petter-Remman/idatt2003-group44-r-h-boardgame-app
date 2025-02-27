package edu.ntnu.idi.idatt.model.Tile;

/**
 * Represents a snake tile in the Snakes and L
 * When a player lands on this tile, they move to a lower-numbered tile.
 */
public class SnakeTile extends Tile {

  /**
   * Constructor for a snake tile.
   * Ensures that the destination tile is a lower number than this tile.
   *
   * @param numberOfTile the number of this tile on the board
   * @param destination  the tile number to which the player is moved (must be lower)
   * @throws IllegalArgumentException if the destination is not lower than the tile number
   */
  public SnakeTile(int numberOfTile, int destination) {
    super(numberOfTile, destination);
    if (destination >= numberOfTile) {
      throw new IllegalArgumentException("A snake must lead to a lower tile.");
    }
  }
}

