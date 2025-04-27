package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.common.CharacterSelectionModel;
import javafx.beans.Observable;
import javafx.collections.SetChangeListener;
import java.util.Arrays;

public class CharacterSelectionController implements CharacterController {
  private final CharacterSelectionModel model;
  private final String[] playerCharacters = new String[4];

  public CharacterSelectionController(CharacterSelectionModel model) {
    this.model = model;
    Arrays.fill(playerCharacters, null);

    // Clean up selections when players are deactivated
    model.getActivePlayers().addListener((Observable observable) -> {
      for(int i = 0; i < model.getActivePlayers().size(); i++) {
        if(!model.getActivePlayers().get(i) && playerCharacters[i] != null) {
          selectCharacter(i, null);
        }
      }
    });
  }

  @Override
  public void selectCharacter(int playerIndex, String character) {
    if(!validatePlayerIndex(playerIndex)) return;

    // Remove previous selection
    String current = playerCharacters[playerIndex];
    if(current != null) {
      model.getSelectedCharacters().remove(current);
    }

    // Set new selection
    if(character != null && !model.getSelectedCharacters().contains(character)) {
      playerCharacters[playerIndex] = character;
      model.getSelectedCharacters().add(character);
    } else {
      playerCharacters[playerIndex] = null;
    }
  }

  @Override
  public void setPlayerActive(int playerIndex, boolean active) {
    if(validatePlayerIndex(playerIndex)) {
      model.getActivePlayers().set(playerIndex, active);
    }
  }

  @Override
  public boolean validateSelections() {
    for(int i = 0; i < model.getActivePlayers().size(); i++) {
      if(model.getActivePlayers().get(i) && playerCharacters[i] == null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void startGame() {
    if(validateSelections()) {
      // Start game logic
    }
  }

  private boolean validatePlayerIndex(int index) {
    return index >= 0 && index < playerCharacters.length;
  }
}
