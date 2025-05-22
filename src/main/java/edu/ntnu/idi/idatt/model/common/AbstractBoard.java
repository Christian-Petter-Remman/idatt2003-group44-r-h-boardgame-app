package edu.ntnu.idi.idatt.model.common;

import java.util.HashMap;
import java.util.Map;

/**
 * <h1>AbstractBoard</h1>
 *
 * An abstract representation of a game board. Provides shared logic for storing and managing tiles.
 */
public abstract class AbstractBoard {

  protected Map<Integer, Tile> tiles = new HashMap<>();

  /**
   * <h2>Constructor</h2>
   * <p>
   * Initializes the board with a specified number of tiles.
   *
   * @param size The number of tiles the board should contain.
   */
  public AbstractBoard(int size) {
    for (int i = 0; i < size; i++) {
      tiles.put(i, new Tile(i));
    }
  }

  /**
   * <h2>initializeBoard</h2>
   * <p>
   * Reinitializes the board. Note: Adds new tiles all with number 1, potentially overwriting existing ones.
   */
  public void initializeBoard() {
    for (int i = 1; i <= getSize(); i++) {
      addTile(new Tile(1));
    }
  }

  /**
   * <h2>getTile</h2>
   * <p>
   * Retrieves the tile object with the given number.
   *
   * @param number The tile number to retrieve.
   * @return The corresponding {@link Tile} object.
   * @throws IllegalArgumentException if the tile does not exist.
   */
  public Tile getTile(int number) {
    Tile tile = tiles.get(number);
    if (tile == null) {
      throw new IllegalArgumentException("Tile number " + number + " not found!");
    }
    return tile;
  }

  /**
   * <h2>addTile</h2>
   * <p>
   * Adds a tile to the board.
   *
   * @param tile The {@link Tile} to be added.
   */
  public void addTile(Tile tile) {
    tiles.put(tile.getNumber(), tile);
  }

  /**
   * <h2>getSize</h2>
   * <p>
   * Gets the total number of tiles on the board.
   *
   * @return The size of the board.
   */
  public abstract int getSize();
}