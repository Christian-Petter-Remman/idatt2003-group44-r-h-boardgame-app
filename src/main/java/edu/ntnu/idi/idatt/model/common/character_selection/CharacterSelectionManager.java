package edu.ntnu.idi.idatt.model.common.character_selection;

import edu.ntnu.idi.idatt.model.common.Observable;
import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionManager implements Observable {
  private final List<CharacterSelectionData> availableCharacters = new ArrayList<>();
  private final List<PlayerData> players;
  private final List<CharacterSelectionObserver> observers = new ArrayList<>();
  private static CharacterSelectionManager instance;

  public static CharacterSelectionManager getInstance() {
    if (instance == null) {
      instance = new CharacterSelectionManager();
    }
    return instance;
  }

  public CharacterSelectionManager() {
    availableCharacters.add(new CharacterSelectionData("Bowser", "/player_icons/bowser.png", false, null));
    availableCharacters.add(new CharacterSelectionData("Charmander", "/player_icons/charmander.png", false, null));
    availableCharacters.add(new CharacterSelectionData("Fish", "/player_icons/fish.png", false, null));
    availableCharacters.add(new CharacterSelectionData("Luigi", "/player_icons/luigi.png", false, null));
    availableCharacters.add(new CharacterSelectionData("Mario", "/player_icons/mario.png", false, null));
    availableCharacters.add(new CharacterSelectionData("Peach", "/player_icons/peach.png", false, null));
    availableCharacters.add(new CharacterSelectionData("The Rock", "/player_icons/the rock.png", false, null));
    availableCharacters.add(new CharacterSelectionData("Snoop Dogg", "/player_icons/snoopdogg.png", false, null));
    availableCharacters.add(new CharacterSelectionData("Toad", "/player_icons/toad.png", false, null));
    availableCharacters.add(new CharacterSelectionData("Yoshi", "/player_icons/yoshi.png", false, null));

    players = new ArrayList<>();
    for (int i = 1; i <= 4; i++) {
      players.add(new PlayerData(i));
    }
  }


  public List<CharacterSelectionData> getAvailableCharacters() {
    return availableCharacters;
  }

  public List<PlayerData> getPlayers() {
    return players;
  }

  public PlayerData getPlayerById(int playerId) {
    for (PlayerData player : players) {
      if (player.getId() == playerId) {
        return player;
      }
    }
    return null;
  }

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

  public void activatePlayer(int playerId) {
    PlayerData player = getPlayerById(playerId);
    if (player != null) {
      player.setActive(true);
      notifyObservers();
    }
  }

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

  public boolean isCharacterTaken(CharacterSelectionData character) {
    return players.stream().anyMatch(p -> p.getSelectedCharacter() != null && p.getSelectedCharacter().equals(character));
  }

  @Override
  public void addObserver(CharacterSelectionObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(CharacterSelectionObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (CharacterSelectionObserver observer : observers) {
      observer.update();
    }
  }

}
