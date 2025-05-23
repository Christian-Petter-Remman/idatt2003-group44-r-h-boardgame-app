package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

/**
 * <h1>Tunnel</h1>
 *
 * <p>Represents a tunnel tile attribute in the Star game. When a player lands on this tile, they
 * are
 * instantly moved to the defined end tile.
 */
public record Tunnel(int start, int end) implements TileAttribute {

  /**
   * <h2>Constructor</h2>
   * Creates a tunnel from the given start tile to the end tile.
   *
   * @param start The starting tile of the tunnel.
   * @param end   The destination tile where the tunnel leads.
   */
  public Tunnel {
  }

  /**
   * <h2>onLand</h2>
   * Triggered when a player lands on the tile with this attribute. Moves the player to the end
   * tile.
   *
   * @param player The player landing on the tile.
   * @param board  The game board.
   */
  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.setPosition(end);
  }

  /**
   * <h2>getStart.</h2>
   *
   * @return The starting tile index of the tunnel.
   */
  @Override
  public int start() {
    return start;
  }

  /**
   * <h2>getEnd.</h2>
   *
   * @return The destination tile index of the tunnel.
   */
  @Override
  public int end() {
    return end;
  }
}