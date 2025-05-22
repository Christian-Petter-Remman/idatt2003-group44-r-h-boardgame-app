package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

/**
 * <h1>Star</h1>
 *
 * Represents a star tile in the Star game. When a player lands on this tile,
 * they are transported to a new tile (the end position).
 */
public class Star implements TileAttribute {

  private final int start;
  private final int end;

  /**
   * <h2>Constructor</h2>
   *
   * Initializes a star tile with a starting and ending tile number.
   *
   * @param start the tile where the star is initially placed
   * @param end   the destination tile the player is moved to
   */
  public Star(int start, int end) {
    this.start = start;
    this.end = end;
  }

  /**
   * <h2>getStart</h2>
   *
   * @return the starting tile index where the star appears
   */
  public int getStart() {
    return start;
  }

  /**
   * <h2>getEnd</h2>
   *
   * @return the destination tile index the player is sent to
   */
  public int getEnd() {
    return end;
  }

  /**
   * <h2>onLand</h2>
   *
   * Triggered when a player lands on the star tile.
   * Immediately transports the player to the end tile.
   *
   * @param player the player who landed on the tile
   * @param board  the game board
   */
  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.setPosition(end);
  }
}