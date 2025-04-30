package edu.ntnu.idi.idatt.model.common.memorygame;

import java.util.List;

public interface MemoryGameObserver {

  void onCardFlipped(int row, int col, CardState state);

  void onMatchFound(MemoryPlayer player, int newScore);

  void onTurnChanged(MemoryPlayer nextPlayer);

  void onGameStateChanged(GameState newState);

  void onGameFinished(List<MemoryPlayer> winners);
}
