package edu.ntnu.idi.idatt.model.model_observers;

import java.util.Set;

public interface CharacterSelectionObserver {
  void onCharacterSelected(int playerIndex, String characterName);
  void onPlayerNameChanged(int playerIndex, String newName);
  void onAvailableCharactersChanged(Set<String> availableCharacters, Set<String> selectedCharacters);
  void onPlayerActiveStatusChanged(int playerIndex, boolean isActive);
}
