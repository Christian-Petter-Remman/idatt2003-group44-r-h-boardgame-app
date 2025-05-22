package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

/**
 * <h1>Ladder</h1>
 *
 * <p>Represents a ladder on a Snakes and Ladders board. When a player lands on the start tile,
 * they
 * are moved to the end tile, effectively climbing the ladder.
 */
public record Ladder(int start, int end) implements TileAttribute {

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes a ladder with its start and end positions.
   *
   * @param start the starting tile number of the ladder
   * @param end   the ending tile number of the ladder
   */
  public Ladder {
  }

  /**
   * <h2>onLand</h2>
   *
   * <p>Moves the player to the end of the ladder when they land on the start tile.
   *
   * @param player the player landing on the ladder
   * @param board  the board where the action occurs
   */
  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.move(end - player.getPosition(), board);
  }

  /**
   * <h2>getStart.</h2>
   *
   * @return the starting tile number of the ladder
   */
  @Override
  public int start() {
    return start;
  }

  /**
   * <h2>getEnd.</h2>
   *
   * @return the ending tile number of the ladder
   */
  @Override
  public int end() {
    return end;
  }
}