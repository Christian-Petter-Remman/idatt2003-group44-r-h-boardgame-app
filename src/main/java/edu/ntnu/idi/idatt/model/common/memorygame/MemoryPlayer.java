package edu.ntnu.idi.idatt.model.common.memorygame;


import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;

public class MemoryPlayer {

  private final PlayerData playerData;
  private int matchesFound = 0;

  public MemoryPlayer(PlayerData playerData) {
    this.playerData = playerData;
  }

  public PlayerData getPlayerData() {
    return playerData;
  }

  public int getMatchesFound() {
    return matchesFound;
  }

  public void incrementMatches() {
    matchesFound++;
  }

  public String getName() {
    return playerData.getSelectedCharacter().getName();
  }

  public String getIconPath() {
    return playerData.getSelectedCharacter().getImagePath();
  }
}

