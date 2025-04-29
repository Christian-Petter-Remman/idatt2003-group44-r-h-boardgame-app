package edu.ntnu.idi.idatt.model.model_observers;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.stargame.Path;

public interface GameObserver {
  void onDiceRolled(Player player, int rollResult);

  void onPlayerMoved(Player player, int oldPosition, int newPosition);

  void onScoreChanged(Player player, int newScore);

  void onPlayerTurnChanged(Player newCurrentPlayer);

  void onPlayerWon(Player winner);

  void onPathDecisionRequested(Player player, Path path);

  void onStarRespawned(Player player, int newTile);

  void onGameSaved(String filePath);
}