package edu.ntnu.idi.idatt.model.model_observers;

public interface RuleSelectionViewObserver {
  void onDifficultyChanged(String difficulty);
  void onLadderCountChanged(int count);
  void onSnakeCountChanged(int count);
  void onRandomBoardSelected(int boardNumber);
  void onDiceCountChanged(int count);
}
