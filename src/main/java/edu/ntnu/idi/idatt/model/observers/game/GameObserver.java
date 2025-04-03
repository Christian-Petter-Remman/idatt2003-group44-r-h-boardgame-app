package edu.ntnu.idi.idatt.model.observers.game;

public interface GameObserver {
  void onGameStart();
  void onGameEnd();
  void onPlayerMove();
}
