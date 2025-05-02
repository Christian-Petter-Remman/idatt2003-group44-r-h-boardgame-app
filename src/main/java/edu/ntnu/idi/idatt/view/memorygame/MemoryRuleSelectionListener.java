package edu.ntnu.idi.idatt.view.memorygame;

import edu.ntnu.idi.idatt.model.common.memorygame.MemoryGameSettings;

public interface MemoryRuleSelectionListener {
  void onBack();
  void onSizeSelected(MemoryGameSettings.BoardSize size);
  void onContinue(String player1, String player2);

}
