package edu.ntnu.idi.idatt.model.model_observers;

import edu.ntnu.idi.idatt.model.common.Player;

/**
 * <h1>GameScreenObserver</h1>
 *
 * Interface for observing visual updates on the game screen.
 * Used by the GUI to respond to changes in player positions, dice rolls,
 * turn changes, game end conditions, and save events.
 */
public interface GameScreenObserver {

  /**
   * <h2>onPlayerPositionChanged</h2>
   *
   * Called when a player's position on the board has changed.
   *
   * @param player       The player whose position changed.
   * @param oldPosition  The previous tile number.
   * @param newPosition  The new tile number.
   */
  void onPlayerPositionChanged(Player player, int oldPosition, int newPosition);

  /**
   * <h2>onDiceRolled</h2>
   *
   * Called when the dice is rolled and a result is available.
   *
   * @param result  The outcome of the dice roll.
   */
  void onDiceRolled(int result);

  /**
   * <h2>onPlayerTurnChanged</h2>
   *
   * Called when the active turn shifts to a different player.
   *
   * @param currentPlayer  The player who now has the turn.
   */
  void onPlayerTurnChanged(Player currentPlayer);

  /**
   * <h2>onGameOver</h2>
   *
   * Called when the game ends and a winner is determined.
   *
   * @param winner  The player who won the game.
   */
  void onGameOver(Player winner);

  /**
   * <h2>onGameSaved</h2>
   *
   * Called when the game has been successfully saved to a file.
   *
   * @param filePath  The file path where the game was saved.
   */
  void onGameSaved(String filePath);
}