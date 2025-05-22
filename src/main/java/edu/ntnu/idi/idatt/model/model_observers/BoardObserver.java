package edu.ntnu.idi.idatt.model.model_observers;

import edu.ntnu.idi.idatt.model.common.Player;

/**
 * <h1>BoardObserver</h1>
 *
 * Observer interface for responding to events related to the game board,
 * such as rendering completion, player movement, and special tile activations.
 */
public interface BoardObserver {

  /**
   * <h2>onBoardRendered</h2>
   *
   * Called when the board has finished rendering.
   */
  void onBoardRendered();

  /**
   * <h2>onPlayerMoved</h2>
   *
   * Called when a player moves from one position to another.
   *
   * @param player the player who moved
   * @param fromPosition the original tile position
   * @param toPosition the new tile position
   */
  void onPlayerMoved(Player player, int fromPosition, int toPosition);

  /**
   * <h2>onSpecialTileActivated</h2>
   *
   * Called when a player lands on a special tile, such as a ladder or snake.
   *
   * @param tileNumber the tile number that was activated
   * @param destination the destination tile after activation
   * @param isLadder true if the tile is a ladder, false if it is a snake or other type
   */
  void onSpecialTileActivated(int tileNumber, int destination, boolean isLadder);
}