package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.common.CharacterSelectionModel;
import java.util.Arrays;
import java.util.stream.IntStream;

public class CharacterSelectionController implements CharacterController {
  private final CharacterSelectionModel model;
  private final String[] playerCharacters = new String[4];

  public CharacterSelectionController(CharacterSelectionModel model) {
    this.model = model;
    Arrays.fill(playerCharacters, null);
  }

  @Override
  public void selectCharacter(int playerIndex, String character) {
    playerCharacters[playerIndex] = character;
    if (!validatePlayerIndex(playerIndex)) return;

    // Remove previous selection
    String current = model.getSelectedCharacters().stream()
        .filter(c -> getPlayerForCharacter(c) == playerIndex)
        .findFirst()
        .orElse(null);

    if (current != null) {
      model.getSelectedCharacters().remove(current);
    }

    // Add new selection
    if (character != null && model.getSelectedCharacters().add(character)) {
      model.getSelectedCharacters().add(character);
    }
  }

  @Override
  public void setPlayerActive(int playerIndex, boolean active) {
    if (!validatePlayerIndex(playerIndex)) return;

    model.getActivePlayers().set(playerIndex, active);

    // Clear selection if deactivating
    if (!active) {
      String current = model.getSelectedCharacters().stream()
          .filter(c -> getPlayerForCharacter(c) == playerIndex)
          .findFirst()
          .orElse(null);

      if (current != null) {
        model.getSelectedCharacters().remove(current);
      }
    }
  }

  @Override
  public void startGame() {
    if (validateSelections()) {
      // Handle game start logic
    }
  }

  public boolean validateSelections() {
    return IntStream.range(0, model.getActivePlayers().size())
        .filter(i -> model.getActivePlayers().get(i))
        .allMatch(i -> model.getSelectedCharacters().stream()
            .anyMatch(c -> getPlayerForCharacter(c) == i));
  }

  private boolean validatePlayerIndex(int index) {
    return index >= 0 && index < model.getActivePlayers().size();
  }


  private int getPlayerForCharacter(String character) {
    // Implementation to track character-player association
    return 0; // Replace with actual logic
  }
}
