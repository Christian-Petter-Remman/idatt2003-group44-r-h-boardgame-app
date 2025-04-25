package edu.ntnu.idi.idatt.model.tile;

/**
 * Represents a single tile on the snakes and ladders game board.
 */
public class Tile {
  private final int numberOfTile;
  private final int destination;
  private final boolean isSpecialTile;

  /**
   * Constructor for a normal tile without ladders or snakes.
   *
   * @param numberOfTile the number of this tile on the board
   */
  public Tile(int numberOfTile) {
    this.numberOfTile = numberOfTile;
    this.destination = -1;
    this.isSpecialTile = false;
  }

  /**
   * Constructor for a tile with a ladder or snake.
   *
   * @param numberOfTile the number of this tile on the board
   * @param destination the tile number to which the player is moved (ladder/snake)
   */
  public Tile(int numberOfTile, int destination) {
    this.numberOfTile = numberOfTile;
    this.destination = destination;
    this.isSpecialTile = true;
  }

  /**
   * Checks if the tile has a ladder or a snake.
   *
   * @return true if this tile has a destination, false otherwise
   */
  public boolean hasSnakeOrLadder() {
    return isSpecialTile;
  }

  /**
   * Gets the destination tile of a ladder or snake.
   *
   * @return the destination tile number, or -1 if none
   */
  public int getDestination() {
    return destination;
  }

  /**
   * Gets the number of this tile.
   *
   * @return the tile number
   */
  public  int getNumberOfTile() {
    return numberOfTile;
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
