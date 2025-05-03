package edu.ntnu.idi.idatt.view.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;

public interface MemoryRuleSelectionListener {
  void onBack();
  void onSizeSelected(MemoryGameSettings.BoardSize size);
  void onContinue(String player1, String player2);

}
