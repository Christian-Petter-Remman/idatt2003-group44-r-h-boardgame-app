package edu.ntnu.idi.idatt.model.common;

/**
 * <h1>TileAttribute</h1>
 *
 * Represents a behavior or effect that can be associated with a tile on the game board.
 * Implementations define what happens when a player lands on a tile with this attribute.
 */
public interface TileAttribute {

  /**
   * <h2>onLand</h2>
   *
   * Triggered when a player lands on a tile containing this attribute.
   *
   * @param player the player who landed on the tile.
   * @param board  the board the player is on.
   */
  void onLand(Player player, AbstractBoard board);
}