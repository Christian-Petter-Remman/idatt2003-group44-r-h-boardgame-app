package edu.ntnu.idi.idatt.model.observers.game;

public interface SnakesAndLaddersObserver extends GameObserver {
  void onLadderClimbed(int start, int end);
  void onSnakeSlipped(int start, int end);
}
