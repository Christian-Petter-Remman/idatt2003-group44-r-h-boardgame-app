package edu.ntnu.idi.idatt.model.modelobservers;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.stargame.Path;

/**
 * <h1>PathDecisionObserver</h1>
 *
 * <p>Interface for observing when a player must make a directional decision on a path tile in the
 * game. Typically used in games with branching tile options such as StarGame.
 */
public interface PathDecisionObserver {

  /**
   * <h2>onPathDecisionRequested</h2>
   *
   * <p>Called when a player lands on a path tile and must choose a direction.
   *
   * @param player The player who must make the decision.
   * @param path   The path object that defines available directions.
   */
  void onPathDecisionRequested(Player player, Path path);
}