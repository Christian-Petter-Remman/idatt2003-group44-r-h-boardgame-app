package edu.ntnu.idi.idatt.model.memorygame;

import java.util.List;

public interface MemoryGameObserver {

  void onBoardUpdated(MemoryBoardGame board);

  void onGameOver(List<MemoryPlayer> winners);
}
