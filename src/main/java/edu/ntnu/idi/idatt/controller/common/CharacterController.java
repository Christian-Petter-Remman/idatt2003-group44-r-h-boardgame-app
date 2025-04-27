package edu.ntnu.idi.idatt.controller.common;

public interface CharacterController {
  void selectCharacter(int playerIndex, String character);
  void setPlayerActive(int playerIndex, boolean active);
  void startGame();
}
