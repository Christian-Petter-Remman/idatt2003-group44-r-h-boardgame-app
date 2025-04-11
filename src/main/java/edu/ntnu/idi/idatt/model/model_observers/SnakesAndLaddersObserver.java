package edu.ntnu.idi.idatt.model.model_observers;

public interface SnakesAndLaddersObserver extends GameObserver {
  void onLadderClimbed(int start, int end);
  void onSnakeSlipped(int start, int end);
}
