package edu.ntnu.idi.idatt.model.modelobservers;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.stargame.Path;

/**
 * <h1>GameObserver</h1>
 *
 * <p>Interface for observing key gameplay events in a board game. This observer is used to react to
 * player actions, game state changes, score updates, and other significant events such as win
 * conditions or path decisions.
 */
public interface GameObserver {

  /**
   * <h2>onScoreChanged</h2>
   *
   * <p>Called when a player's score is updated.
   *
   * @param player   The player whose score has changed.
   * @param newScore The new score of the player.
   */
  void onScoreChanged(Player player, int newScore);

  /**
   * <h2>onPathDecisionRequested</h2>
   *
   * <p>Called when a player reaches a tile with a path decision.
   *
   * @param player The player who must decide a path.
   * @param path   The path object with decision details.
   */
  void onPathDecisionRequested(Player player, Path path);

  /**
   * <h2>onStarRespawned</h2>
   *
   * <p>Called when a star respawns on a tile.
   *
   * @param player  The player who triggered the respawn.
   * @param newTile The tile number where the star appears.
   */
  void onStarRespawned(Player player, int newTile);

}