package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

/**
 * <h1>Snake</h1>
 *
 * Represents a snake tile attribute in the Snakes and Ladders game.
 * When a player lands on this tile, they are moved backward to the end tile.
 */
public class Snake implements TileAttribute {

  private final int start;
  private final int end;

  /**
   * <h2>Snake</h2>
   *
   * Constructs a Snake with a start and end position.
   *
   * @param start the tile number where the snake begins
   * @param end   the tile number where the snake ends
   */
  public Snake(int start, int end) {
    this.start = start;
    this.end = end;
  }

  /**
   * <h2>onLand</h2>
   *
   * Executes the snake effect when a player lands on its starting tile,
   * moving them down to the end tile.
   *
   * @param player the player who landed on the tile
   * @param board  the game board
   */
  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.move(end - player.getPosition(), board);
  }

  /**
   * <h2>getStart</h2>
   *
   * @return the starting tile of the snake
   */
  public int getStart() {
    return start;
  }

  /**
   * <h2>getEnd</h2>
   *
   * @return the ending tile of the snake
   */
  public int getEnd() {
    return end;
  }
}