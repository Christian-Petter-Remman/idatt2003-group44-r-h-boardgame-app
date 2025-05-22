package edu.ntnu.idi.idatt.model.model_observers;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.stargame.Path;

/**
 * <h1>GameObserver</h1>
 *
 * Interface for observing key gameplay events in a board game.
 * This observer is used to react to player actions, game state changes,
 * score updates, and other significant events such as win conditions or path decisions.
 */
public interface GameObserver {

  /**
   * <h2>onDiceRolled</h2>
   *
   * Called when a player rolls the dice.
   *
   * @param player      The player who rolled the dice.
   * @param rollResult  The result of the dice roll.
   */
  void onDiceRolled(Player player, int rollResult);

  /**
   * <h2>onPlayerMoved</h2>
   *
   * Called when a player moves to a new tile.
   *
   * @param player       The player who moved.
   * @param oldPosition  The previous tile number.
   * @param newPosition  The new tile number.
   */
  void onPlayerMoved(Player player, int oldPosition, int newPosition);

  /**
   * <h2>onScoreChanged</h2>
   *
   * Called when a player's score is updated.
   *
   * @param player    The player whose score has changed.
   * @param newScore  The new score of the player.
   */
  void onScoreChanged(Player player, int newScore);

  /**
   * <h2>onPlayerTurnChanged</h2>
   *
   * Called when the turn passes to a new player.
   *
   * @param newCurrentPlayer  The player who now has the turn.
   */
  void onPlayerTurnChanged(Player newCurrentPlayer);

  /**
   * <h2>onPlayerWon</h2>
   *
   * Called when a player wins the game.
   *
   * @param winner  The player who won the game.
   */
  void onPlayerWon(Player winner);

  /**
   * <h2>onPathDecisionRequested</h2>
   *
   * Called when a player reaches a tile with a path decision.
   *
   * @param player  The player who must decide a path.
   * @param path    The path object with decision details.
   */
  void onPathDecisionRequested(Player player, Path path);

  /**
   * <h2>onStarRespawned</h2>
   *
   * Called when a star respawns on a tile.
   *
   * @param player    The player who triggered the respawn.
   * @param newTile   The tile number where the star appears.
   */
  void onStarRespawned(Player player, int newTile);

  /**
   * <h2>onGameSaved</h2>
   *
   * Called when the game is successfully saved.
   *
   * @param filePath  Path to the saved game file.
   */
  void onGameSaved(String filePath);
}