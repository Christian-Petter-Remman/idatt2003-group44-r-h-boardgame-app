package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

/**
 * <h1>Star</h1>
 *
 * <p>Represents a star tile in the Star game. When a player lands on this tile, they are
 * transported
 * to a new tile (the end position).
 */
public record Star(int start, int end) implements TileAttribute {

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes a star tile with a starting and ending tile number.
   *
   * @param start the tile where the star is initially placed
   * @param end   the destination tile the player is moved to
   */
  public Star {
  }

  /**
   * <h2>getStart.</h2>
   *
   * @return the starting tile index where the star appears
   */
  @Override
  public int start() {
    return start;
  }

  /**
   * <h2>getEnd.</h2>
   *
   * @return the destination tile index the player is sent to
   */
  @Override
  public int end() {
    return end;
  }

  /**
   * <h2>onLand</h2>
   *
   * <p>Triggered when a player lands on the star tile. Immediately transports the player to the
   * end
   * tile.
   *
   * @param player the player who landed on the tile
   * @param board  the game board
   */
  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.setPosition(end);
  }
}