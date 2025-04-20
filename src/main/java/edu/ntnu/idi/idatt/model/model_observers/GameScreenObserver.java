package edu.ntnu.idi.idatt.model.model_observers;

import edu.ntnu.idi.idatt.model.common.Player;

public interface GameScreenObserver {
  void onPlayerPositionChanged(Player player, int oldPosition, int newPosition);
  void onDiceRolled(int result);
  void onPlayerTurnChanged(Player currentPlayer);
  void onGameOver(Player winner);
  void onGameSaved(String filePath);
}
