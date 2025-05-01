package edu.ntnu.idi.idatt.model.common.character_selection;

import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionCSVExporter implements CharacterSelectionObserver {

  private final CharacterSelectionManager manager;
  private List<String[]> playerCharacterData;

  public CharacterSelectionCSVExporter(CharacterSelectionManager manager) {
    this.manager = manager;
    this.playerCharacterData = new ArrayList<>();
    manager.addObserver(this);
  }

  @Override
  public void update() {
    playerCharacterData.clear(); // Clear previous data

    for (PlayerData player : manager.getPlayers()) {
      if (player.isActive() && player.getSelectedCharacter() != null) {
        String playerName = "Player " + player.getId();
        String characterName = player.getSelectedCharacter().getName();
        playerCharacterData.add(new String[]{playerName, characterName});
      }
    }
  }

  public List<String[]> getPlayerCharacterData() {
    return playerCharacterData;
  }
}