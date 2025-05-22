package edu.ntnu.idi.idatt.model.common.characterselection;

import edu.ntnu.idi.idatt.model.common.Observable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h1>CharacterSelectionManager</h1>
 *
 * <p>Manages the available characters and player selections during the character selection phase.
 * Implements the Observable pattern to notify views or handlers of state changes.
 */
public class CharacterSelectionManager implements Observable {

  private final List<CharacterSelectionData> availableCharacters = new ArrayList<>();
  private final List<PlayerData> players;
  private final List<CharacterSelectionObserver> observers = new ArrayList<>();
  private static CharacterSelectionManager instance;

  /**
   * <h2>getInstance</h2>
   * Singleton access to the CharacterSelectionManager instance.
   *
   * @return the singleton instance
   */
  public static CharacterSelectionManager getInstance() {
    if (instance == null) {
      instance = new CharacterSelectionManager();
    }
    return instance;
  }

  /**
   * <h2>Constructor</h2>
   * Initializes the character list and four default players.
   */
  public CharacterSelectionManager() {
    availableCharacters.add(
        new CharacterSelectionData("Bowser", "/player_icons/bowser.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("Charmander", "/player_icons/charmander.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("Fish", "/player_icons/fish.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("Luigi", "/player_icons/luigi.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("Mario", "/player_icons/mario.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("Peach", "/player_icons/peach.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("TheRock", "/player_icons/therock.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("SnoopDogg", "/player_icons/snoopdogg.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("Toad", "/player_icons/toad.png", false, null));
    availableCharacters.add(
        new CharacterSelectionData("Yoshi", "/player_icons/yoshi.png", false, null));

    players = new ArrayList<>();
    for (int i = 1; i <= 4; i++) {
      players.add(new PlayerData(i));
    }
  }

  /**
   * <h2>getAvailableCharacters.</h2>
   *
   * @return list of all available characters
   */
  public List<CharacterSelectionData> getAvailableCharacters() {
    return availableCharacters;
  }

  /**
   * <h2>getPlayers.</h2>
   *
   * @return list of all players
   */
  public List<PlayerData> getPlayers() {
    return players;
  }

  /**
   * <h2>getPlayerById</h2>
   * Finds a player by ID.
   *
   * @param playerId ID of the player
   * @return the PlayerData or null if not found
   */
  public PlayerData getPlayerById(int playerId) {
    return players.stream()
        .filter(p -> p.getId() == playerId)
        .findFirst()
        .orElse(null);
  }

  /**
   * <h2>selectCharacter</h2>
   * Assigns a character to the player, updating previous selection if needed.
   *
   * @param player    the player selecting a character
   * @param character the character to assign
   */
  public void selectCharacter(PlayerData player, CharacterSelectionData character) {
    if (player.getSelectedCharacter() != null) {
      player.getSelectedCharacter().setSelected(false);
      player.getSelectedCharacter().setSelectedBy(null);
    }

    character.setSelected(true);
    character.setSelectedBy(player);
    player.setSelectedCharacter(character);
    player.setCharacterIcon(character.getName());

    notifyObservers();
  }

  /**
   * <h2>activatePlayer</h2>
   * Marks the player as active.
   *
   * @param playerId ID of the player to activate
   */
  public void activatePlayer(int playerId) {
    Optional.ofNullable(getPlayerById(playerId))
        .ifPresent(player -> {
          player.setActive(true);
          notifyObservers();
        });
  }

  /**
   * <h2>deactivatePlayer</h2>
   * Deactivates a player and unselects their character (if any). Only players with ID > 2 can be
   * deactivated.
   *
   * @param playerId ID of the player to deactivate
   */
  public void deactivatePlayer(int playerId) {
    PlayerData player = getPlayerById(playerId);
    if (player != null && player.getId() > 2) {
      if (player.getSelectedCharacter() != null) {
        player.getSelectedCharacter().setSelected(false);
        player.getSelectedCharacter().setSelectedBy(null);
        player.setSelectedCharacter(null);
      }
      player.setActive(false);
      notifyObservers();
    }
  }

  /**
   * <h2>allActivePlayersHaveSelectedCharacters.</h2>
   *
   * @return true if all active players have selected characters, false otherwise
   */
  public boolean allActivePlayersHaveSelectedCharacters() {
    return players.stream()
        .filter(PlayerData::isActive)
        .allMatch(p -> p.getSelectedCharacter() != null);
  }

  /**
   * <h2>isCharacterTaken</h2>
   * Checks whether a character has already been selected.
   *
   * @param character the character to check
   * @return true if the character is taken
   */
  public boolean isCharacterTaken(CharacterSelectionData character) {
    return players.stream()
        .anyMatch(p -> character.equals(p.getSelectedCharacter()));
  }

  /**
   * <h2>addObserver</h2>
   * Registers an observer to receive updates.
   *
   * @param observer the observer to add
   */
  @Override
  public void addObserver(CharacterSelectionObserver observer) {
    observers.add(observer);
  }

  /**
   * <h2>removeObserver</h2>
   * Removes an observer from the update list.
   *
   * @param observer the observer to remove
   */
  @Override
  public void removeObserver(CharacterSelectionObserver observer) {
    observers.remove(observer);
  }

  /**
   * <h2>notifyObservers</h2>
   * Notifies all observers of a change in character selection state.
   */
  @Override
  public void notifyObservers() {
    observers.forEach(CharacterSelectionObserver::update);
  }
}