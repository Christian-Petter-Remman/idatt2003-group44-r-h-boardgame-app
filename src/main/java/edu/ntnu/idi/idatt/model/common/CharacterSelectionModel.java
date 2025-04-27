package edu.ntnu.idi.idatt.model.common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public class CharacterSelectionModel {
  private final ObservableList<String> availableCharacters = FXCollections.observableArrayList(
      "bowser", "peach", "mario", "toad", "charmander", "fish", "luigi", "yoshi", "rock", "snoopdogg"
  );

  private final ObservableSet<String> selectedCharacters = FXCollections.observableSet();
  private final ObservableList<Boolean> activePlayers = FXCollections.observableArrayList(
      true, true, false, false
  );
  private final ObservableList<String> playerNames = FXCollections.observableArrayList(
      "Player 1", "Player 2", "Player 3", "Player 4"
  );

  public ObservableList<String> getAvailableCharacters() { return availableCharacters; }
  public ObservableSet<String> getSelectedCharacters() { return selectedCharacters; }
  public ObservableList<Boolean> getActivePlayers() { return activePlayers; }
  public ObservableList<String> getPlayerNames() { return playerNames; }
}
