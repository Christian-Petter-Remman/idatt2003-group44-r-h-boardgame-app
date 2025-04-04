package edu.ntnu.idi.idatt.model.model_observers.game;

public interface GameObserver {
  void onGameStart();
  void onGameEnd();
  void onPlayerMove();
}
