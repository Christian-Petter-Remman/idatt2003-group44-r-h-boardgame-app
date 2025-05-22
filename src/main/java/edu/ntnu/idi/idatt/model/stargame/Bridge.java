package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

/**
 * <h1>Bridge</h1>
 *
 * Represents a bridge tile in the Star game. When a player lands on this tile,
 * they are instantly teleported from the start position to the end position.
 */
public class Bridge implements TileAttribute {

  private final int start;
  private final int end;

  /**
   * <h2>Constructor</h2>
   *
   * Initializes a bridge with a given start and end tile index.
   *
   * @param start the starting tile index of the bridge.
   * @param end the ending tile index the player should be moved to.
   */
  public Bridge(int start, int end) {
    this.start = start;
    this.end = end;
  }

  /**
   * <h2>onLand</h2>
   *
   * Triggered when a player lands on this tile. Instantly moves the player to the end tile.
   *
   * @param player the player landing on the tile.
   * @param board the game board where the player is located.
   */
  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.setPosition(end);
  }

  /**
   * <h2>getStart</h2>
   *
   * @return the start tile index of the bridge.
   */
  public int getStart() {
    return start;
  }

  /**
   * <h2>getEnd</h2>
   *
   * @return the end tile index of the bridge.
   */
  public int getEnd() {
    return end;
  }
}