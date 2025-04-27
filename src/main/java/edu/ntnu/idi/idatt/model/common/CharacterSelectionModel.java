package edu.ntnu.idi.idatt.model.common;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public class CharacterSelectionModel {

  private final ObservableList<String> availableCharacters = FXCollections.observableArrayList(
      "bowser", "peach", "mario", "toad", "charmander", "fish", "luigi", "yoshi", "rock", "snoopdogg"
  );

  private final Map<String, Integer> characterOwners = new HashMap<>();

  private final ObservableSet<String> selectedCharacters = FXCollections.observableSet();
  private final ObservableList<Boolean> activePlayers = FXCollections.observableArrayList(
      false, false, false, false
  );

  public CharacterSelectionModel() {
    // Initialize first two players as active
    activePlayers.set(0, Boolean.TRUE);
    activePlayers.set(1, Boolean.TRUE);
  }

  // Getters for observable properties
  public ObservableList<String> getAvailableCharacters() { return availableCharacters; }
  public ObservableSet<String> getSelectedCharacters() { return selectedCharacters; }
  public ObservableList<Boolean> getActivePlayers() { return activePlayers; }

  public Map<String, Integer> getCharacterOwners() {
    return characterOwners;
  }

  public void setCharacterOwner(String character, int playerIndex) {
    characterOwners.put(character, playerIndex);
  }

  public void removeCharacterOwner(String character) {
    characterOwners.remove(character);
  }
}
