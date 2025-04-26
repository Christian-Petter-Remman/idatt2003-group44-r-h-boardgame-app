package edu.ntnu.idi.idatt.model.common.screens;

public interface CharacterController {
  void selectCharacter(int playerIndex, String character);
  void setPlayerActive(int playerIndex, boolean active);
  void startGame();
}
