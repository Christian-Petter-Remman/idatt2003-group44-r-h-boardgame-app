package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

/**
 * <h1>Jail</h1>
 *
 * Represents a jail tile in the Star game. When a player lands on this tile,
 * they are imprisoned for a fixed number of turns.
 */
public class Jail implements TileAttribute {

  private final int start;
  private final int jailTurns;

  /**
   * <h2>Constructor</h2>
   *
   * Initializes a jail tile with its starting position and the number of turns the player must remain in jail.
   *
   * @param start the tile index where the jail is located.
   * @param jailTurns the number of turns a player must stay in jail.
   */
  public Jail(int start, int jailTurns) {
    this.start = start;
    this.jailTurns = jailTurns;
  }

  /**
   * <h2>onLand</h2>
   *
   * Triggered when a player lands on the jail tile. Sets the player's jailed status and number of jail turns.
   *
   * @param player the player landing on the tile.
   * @param board the game board the player is on.
   */
  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.setJailed(jailTurns);
  }

  /**
   * <h2>getStart</h2>
   *
   * @return the tile index where the jail is placed.
   */
  public int getStart() {
    return start;
  }

  /**
   * <h2>getJailTurns</h2>
   *
   * @return the number of turns a player must stay in jail.
   */
  public int getJailTurns() {
    return jailTurns;
  }
}