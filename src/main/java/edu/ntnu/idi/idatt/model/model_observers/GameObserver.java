package edu.ntnu.idi.idatt.model.model_observers;

public interface GameObserver {
  void onGameStart();
  void onGameEnd();
  void onPlayerMove();
}
