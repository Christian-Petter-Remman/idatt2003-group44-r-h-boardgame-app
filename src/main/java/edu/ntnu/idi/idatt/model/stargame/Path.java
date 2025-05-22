package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

/**
 * <h1>Path</h1>
 *
 * <p>Represents a path tile that allows players to choose a direction between two destinations
 * (static
 * or dynamic) in the Star game.
 */
public record Path(int start, String direction, int endStatic, int endDynamic) implements
    TileAttribute {

  /**
   * <h2>Constructor</h2>
   *
   * <p>Creates a new Path with its origin, directional label, and possible destinations.
   *
   * @param start      the tile index where the path begins.
   * @param direction  the direction label (e.g., "left" or "right").
   * @param endStatic  the fixed destination.
   * @param endDynamic the variable destination based on player input.
   */
  public Path {
  }

  /**
   * <h2>getDirection.</h2>
   *
   * @return the direction string associated with this path.
   */
  @Override
  public String direction() {
    return direction;
  }

  /**
   * <h2>getStart.</h2>
   *
   * @return the starting tile index of the path.
   */
  @Override
  public int start() {
    return start;
  }

  /**
   * <h2>getEndStatic.</h2>
   *
   * @return the fixed (static) destination of the path.
   */
  @Override
  public int endStatic() {
    return endStatic;
  }

  /**
   * <h2>getEndDynamic.</h2>
   *
   * @return the dynamic destination of the path.
   */
  @Override
  public int endDynamic() {
    return endDynamic;
  }

  /**
   * <h2>onLand</h2>
   *
   * <p>Triggered when a player lands on this tile. In this implementation, the method is
   * intentionally
   * left blank since path logic is handled elsewhere.
   *
   * @param player the player landing on the tile.
   * @param board  the game board.
   */
  @Override
  public void onLand(Player player, AbstractBoard board) {
  }
}