package edu.ntnu.idi.idatt.controller.snakesandladders;

import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.filehandling.PlayerCsvHandler;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.CharacterSelectionObserver;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class SalCharacterSelectionController {
  private static final Logger logger = LoggerFactory.getLogger(SalCharacterSelectionController.class);

  private final List<CharacterSelectionObserver> observers = new ArrayList<>();
  private NavigationHandler navigationHandler;

  private final List<String> availableCharacters = Arrays.asList(
      "bowser", "peach", "mario", "toad", "charmander", "fish", "luigi", "yoshi", "rock", "snoopdogg"
  );

  private final Set<String> selectedCharacters = new HashSet<>();
  private final String[] playerNames = {"Player 1", "Player 2", "Player 3", "Player 4"};
  private final String[] playerCharacters = new String[4];
  private final boolean[] playerActive = {true, true, false, false};

  private String baseName;

  public void setNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public void registerObserver(CharacterSelectionObserver observer) {
    observers.add(observer);
  }

  public List<String> getAvailableCharacters() {
    return new ArrayList<>(availableCharacters);
  }

  public Set<String> getSelectedCharacters() {
    return new HashSet<>(selectedCharacters);
  }

  public void selectCharacter(int playerIndex, String characterName) {
    if (playerIndex < 0 || playerIndex >= playerNames.length) {
      logger.warn("Invalid player index: {}", playerIndex);
      return;
    }

    if (playerCharacters[playerIndex] != null) {
      selectedCharacters.remove(playerCharacters[playerIndex]);
    }

    playerCharacters[playerIndex] = characterName;
    selectedCharacters.add(characterName);

    notifyCharacterSelected(playerIndex, characterName);
    notifyAvailableCharactersChanged();

    logger.info("Player {} selected character: {}", playerIndex + 1, characterName);
  }

  public void setPlayerName(int playerIndex, String name) {
    if (playerIndex < 0 || playerIndex >= playerNames.length) {
      logger.warn("Invalid player index: {}", playerIndex);
      return;
    }

    playerNames[playerIndex] = name;
    notifyPlayerNameChanged(playerIndex, name);

    logger.info("Player {} name changed to: {}", playerIndex + 1, name);
  }

  public void setPlayerActive(int playerIndex, boolean isActive) {
    if (playerIndex < 0 || playerIndex >= playerNames.length) {
      logger.warn("Invalid player index: {}", playerIndex);
      return;
    }

    playerActive[playerIndex] = isActive;

    if (!isActive && playerCharacters[playerIndex] != null) {
      selectedCharacters.remove(playerCharacters[playerIndex]);
      playerCharacters[playerIndex] = null;
    }

    notifyPlayerActiveStatusChanged(playerIndex, isActive);
    notifyAvailableCharactersChanged();

    logger.info("Player {} active status changed to: {}", playerIndex + 1, isActive);
  }

  public boolean isCharacterAvailable(String characterName) {
    return !selectedCharacters.contains(characterName);
  }

  public void startGame() {
    if (!validateSelections()) {
      return;
    }

    try {
      List<Player> players = createPlayers();
      baseName = generateBaseName();
      String csvPath = "data/user-data/player-files/" + baseName + ".csv";

      savePlayersToFile(players, csvPath);

      if (navigationHandler != null) {
        navigationHandler.navigateTo("RULE_SELECTION");
      } else {
        logger.error("Navigation handler is not set");
      }
    } catch (Exception e) {
      logger.error("Error starting game: {}", e.getMessage());
      AlertUtil.showAlert("Error", "Failed to start the game: " + e.getMessage());
    }
  }

  public void navigateBack() {
    if (navigationHandler != null) {
      navigationHandler.navigateBack();
    } else {
      logger.error("Navigation handler is not set");
    }
  }

  public String getBaseName() {
    return baseName;
  }

  public List<Player> getPlayers() {
    return createPlayers();
  }

  private boolean validateSelections() {
    for (int i = 0; i < playerActive.length; i++) {
      if (playerActive[i] && (playerCharacters[i] == null || playerCharacters[i].isEmpty())) {
        AlertUtil.showAlert("Invalid Selection", "Player " + (i + 1) + " needs to select a character.");
        return false;
      }
    }
    return true;
  }

  private List<Player> createPlayers() {
    List<Player> players = new ArrayList<>();

    for (int i = 0; i < playerActive.length; i++) {
      if (playerActive[i] && playerCharacters[i] != null) {
        players.add(new SnakesAndLaddersPlayer(playerNames[i], playerCharacters[i], 0));
      }
    }

    return players;
  }

  private String generateBaseName() {
    return "SNL_" + LocalDate.now().toString().replace("-", "") + "_" + System.currentTimeMillis();
  }

  private void savePlayersToFile(List<Player> players, String fileName) throws IOException {
    PlayerCsvHandler playerCsvHandler = new PlayerCsvHandler();
    playerCsvHandler.saveToFile(players, fileName);
    logger.info("Saved {} players to file: {}", players.size(), fileName);
  }

  public List<Player> loadPlayersFromFile(String fileName) throws IOException, FileReadException, CsvFormatException {
    PlayerCsvHandler playerCsvHandler = new PlayerCsvHandler();
    return playerCsvHandler.loadFromFile(fileName);
  }

  private void notifyCharacterSelected(int playerIndex, String characterName) {
    for (CharacterSelectionObserver observer : observers) {
      observer.onCharacterSelected(playerIndex, characterName);
    }
  }

  private void notifyPlayerNameChanged(int playerIndex, String newName) {
    for (CharacterSelectionObserver observer : observers) {
      observer.onPlayerNameChanged(playerIndex, newName);
    }
  }

  private void notifyAvailableCharactersChanged() {
    for (CharacterSelectionObserver observer : observers) {
      observer.onAvailableCharactersChanged(new HashSet<>(availableCharacters), selectedCharacters);
    }
  }

  private void notifyPlayerActiveStatusChanged(int playerIndex, boolean isActive) {
    for (CharacterSelectionObserver observer : observers) {
      observer.onPlayerActiveStatusChanged(playerIndex, isActive);
    }
  }
}
