package edu.ntnu.idi.idatt.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Tile</h1>
 *
 * <p>Represents a single tile on the game board. Each tile has a number and may have one or more
 * attributes that define its behavior when a player lands on it.
 */
public class Tile {

  private final int number;
  private final List<TileAttribute> attributes = new ArrayList<>();

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes a tile with a given number.
   *
   * @param number The tile's position on the board.
   */
  public Tile(int number) {
    this.number = number;
  }

  /**
   * <h2>getNumber.</h2>
   *
   * @return The numeric identifier of this tile.
   */
  public int getNumber() {
    return number;
  }

  /**
   * <h2>hasAttribute</h2>
   *
   * <p>Checks if this tile contains a specific type of attribute.
   *
   * @param clazz The class of the attribute to check for.
   * @return {@code true} if the attribute type exists on this tile, otherwise {@code false}.
   */
  public boolean hasAttribute(Class<? extends TileAttribute> clazz) {
    return attributes.stream().anyMatch(clazz::isInstance);
  }

  /**
   * <h2>getAttributes.</h2>
   *
   * @return A list of attributes associated with this tile.
   */
  public List<TileAttribute> getAttributes() {
    return attributes;
  }

  /**
   * <h2>addAttribute</h2>
   *
   * <p>Adds a new attribute to this tile.
   *
   * @param attribute The attribute to be added.
   */
  public void addAttribute(TileAttribute attribute) {
    attributes.add(attribute);
  }

  /**
   * <h2>onPlayerLanded</h2>
   *
   * <p>Triggers the behavior of all attributes on this tile when a player lands on it.
   *
   * @param player The player who landed on the tile.
   * @param board  The board the tile belongs to.
   */
  public void onPlayerLanded(Player player, AbstractBoard board) {
    attributes.forEach(attribute -> attribute.onLand(player, board));
  }
}